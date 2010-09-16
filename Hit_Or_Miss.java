import java.awt.*;
import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import org.ajdecon.morphology.*;

public class Hit_Or_Miss implements PlugIn {

	public void run(String arg) {

		String[] available = imageList();
		GenericDialog inputs = erosionDialog(available);
		inputs.showDialog();
		if (inputs.wasCanceled()) return;

		String objectName = inputs.getNextChoice();
		String seName = inputs.getNextChoice();
		String seName2 = inputs.getNextChoice();
		boolean bgWhite = inputs.getNextBoolean();

		ImagePlus object = ij.WindowManager.getImage(objectName);
		ImagePlus seImage = ij.WindowManager.getImage(seName);
		ImagePlus seImage2 = ij.WindowManager.getImage(seName2);
		StructElement se = new StructElement(seImage, bgWhite);
		StructElement se2 = new StructElement(seImage2, bgWhite);

		ImagePlus e = Morphology.hitOrMiss(object,se,se2);
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
		GenericDialog result = new GenericDialog("Hit or miss transform");
		result.addMessage("Hit Or Miss Transform\n" +
			"For each pixel, determine if its foreground for the first\n" +
			"structuring element and its background for the second\n" + 
			"do not overlap.  If this is true, set equal to the foreground.\n" +
			"Otherwise, set equal to the background.\n\n" + 
			"Equivalent to a logical AND between an erosion with the first element\n" +
			"and an erosion of the image inverse with the second.");
		result.addChoice("Object image:",imageList,imageList[0]);
		result.addChoice("Structuring element 1:",imageList,imageList[0]);
		result.addChoice("Structuring element 2:",imageList,imageList[0]);
		result.addCheckbox("Background is white:",true);

		return result;		

	}
}
