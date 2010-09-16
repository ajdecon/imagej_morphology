import java.awt.*;
import ij.*;
import ij.process.*;
import ij.plugin.*;
import ij.plugin.filter.*;
import ij.gui.*;
import java.lang.Math;

/* 
 * Java_Otsu_Threshold
 * ImageJ PlugInFilter which implements Otsu's method for
 * thresholding an image.  Works with 8-Bit gray-scale images.
 *
 * Otsu's method: maximize inter-class variance between object and
 * background.  Define function to be optimized:
 *   Var = w_1(t)*w_2(t)*[\mu_1(t) - \mu_2(t)]**2
 *
 * Algorithm: (wikipedia)
 *   1. Compute histogram and probabilities of each gray level.
 *   2. Set up initial w_i and \mu_i
 *   3. Step through all possible thresholds t=1,...
 *      1. Update w_i and \mu_i
 *      2. Compute Var
 *   4. Desired threshold corresponds to maximum Var.
 *
 */

public class Otsu_Threshold implements PlugInFilter {
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		}
		this.imp = imp;
		return DOES_8G;
	}

	// run
	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[])ip.getPixels();
		int numPixels = pixels.length;
		
		int bestThreshold = 0;
		double bestValue = 0.0;
		double currentValue = 0.0;

		int[] histogram = computeHist(pixels);
		
		// Maximize otsu function.
		for (int i = 0; i < 255; i++) {
			currentValue = otsu(histogram, i);
			if (currentValue > bestValue) {
				bestValue = currentValue;
				bestThreshold = i;
			}
		}

	//	IJ.showMessage("Best Threshold", Integer.toString(bestThreshold));

		// Perform thresholding operation.
		int width=ip.getWidth();
		int height=ip.getHeight();
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				if (ip.get(x,y)<bestThreshold) {
					ip.set(x,y,0);
				} else {
					ip.set(x,y,255);
				}
			}
		}

	} // end run

	// compute histogram of pixel array.
	private int[] computeHist(byte[] pixels) {
		int numPixels = pixels.length;
		int[] histogram = new int[256];
		int i,j;
		for (i = 0; i <= 255; i++) { histogram[i] = 0; }
		for (i = 0; i <= 255; i++) {
			for (j = 0; j < numPixels; j++) {
				if ( pixels[j] == i ) {
					histogram[i]+=1;
				}
			}
		}
		return histogram;

	} // end computeHist

	// Otsu function.
	private double otsu(int[] histogram, int t) {
		
		double meanBg = 0; double meanFg = 0; long numBg = 0;
		long numFg = 0;
		double probBg, probFg;
		double variance;
		int i;

		// compute background mean
		for (i = 0; i < t; i++) {
			numBg += histogram[i];
			meanBg += i*histogram[i];			
		}
		
		if (numBg>0) { meanBg = meanBg / numBg; }

		// compute foreground mean
		for (i = t; i < 256; i++) {
			numFg += histogram[i];
			meanFg += i*histogram[i];
		}
		if (numFg>0) { meanFg = meanFg / numFg; }

		// Compute otsu function.
		probBg = (double)numBg/(numBg+numFg);
		probFg = 1.0 - probBg;
		variance = probBg*probFg* Math.pow((meanBg-meanFg),2);

		return variance;
	}

	private void showAbout() {
		IJ.showMessage("About Otsu Threshold", "Performs an Otsu threshold on 8-bit images.");
	}

}
