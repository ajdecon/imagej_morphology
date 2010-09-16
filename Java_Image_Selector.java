import java.awt.*;
import ij.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import ij.process.*;
import ij.gui.*;

public class Java_Image_Selector implements PlugIn {

	public void run(String arg) {

		int numImages = ij.WindowManager.getImageCount();
		String[] imageList = new String[numImages];

		for(int n = 1; n <= numImages; n++) {
			ImagePlus im = ij.WindowManager.getImage(
					ij.WindowManager.getNthImageID(n));
			String imname = im.getTitle();
			imageList[n-1] = imname;
		}

		GenericDialog imChooser = new GenericDialog("Choose an image");
		imChooser.addChoice("Images",imageList,imageList[0]);
		imChooser.showDialog();
		if (imChooser.wasCanceled()) return;
		String choice = imChooser.getNextChoice();

		ij.WindowManager.setWindow(ij.WindowManager.getFrame(choice));

	}

}
