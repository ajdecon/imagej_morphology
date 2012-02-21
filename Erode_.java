import java.awt.*;
import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import org.ajdecon.morphology.*;

public class Erode_ implements PlugIn {

	public void run(String arg) {
		String[] available = imageList();
		GenericDialog inputs = erosionDialog(available);
		inputs.showDialog();
		if (inputs.wasCanceled()) return;

		String objectName = inputs.getNextChoice();
		String seName = inputs.getNextChoice();
		boolean bgWhite = inputs.getNextBoolean();

		ImagePlus object = ij.WindowManager.getImage(objectName);
		ImagePlus seImage = ij.WindowManager.getImage(seName);
		StructElement se = new StructElement(seImage, bgWhite);

		ImagePlus e = Morphology.erode(object,se);
		e.show();
		return;
	}

	private String[] imageList() {
		int numImages = ij.WindowManager.getImageCount();
		String[] result = new String[numImages];
		for (int n=1; n<=numImages; n++) {
			ImagePlus im = ij.WindowManager.getImage(
					ij.WindowManager.getNthImageID(n));
			result[n-1] = im.getTitle();
		}
		return result;
	}

	private GenericDialog erosionDialog(String[] imageList) {
		GenericDialog result = new GenericDialog("Erode Image");
		result.addMessage("Image erosion\n" +
			"For each pixel, compare the neighborhood centered on\n " +
			"that pixel to a structuring element, and create a new set\n" +
			"consisting of the image pixels matching that element. Assign the\n" +
			"target pixel's new value to be the value of that set closest to the\n" +
			"background.\nThe structuring element may be any binary image, or\n" +
			"created using the New_Structuring_Element plugin.");
		result.addChoice("Object image:",imageList,imageList[0]);
		result.addChoice("Structuring element:",imageList,imageList[0]);
		result.addCheckbox("Background is white:",true);
		return result;		
	}
}
