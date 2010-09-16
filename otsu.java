import ij.*;
import ij.process.*;
import java.lang.Math;

public class otsu {

	public static void main(String[] args) {
		
		// Get input file and convert to 8-bit for otsu thresholding
		ImagePlus in = IJ.openImage(args[0]);
		ImageConverter ic = new ImageConverter(in);
		ic.convertToGray8();
		ImageProcessor ip = in.getProcessor();

		// Perform otsu thresholding.
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
		
		// Write output file.
		IJ.save(in, args[1]);
	}

	// compute histogram of pixel array.
	private static int[] computeHist(byte[] pixels) {
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
	private static double otsu(int[] histogram, int t) {
		
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
	
}
