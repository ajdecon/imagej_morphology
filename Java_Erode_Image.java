import java.awt.*;
import ij.*;
import ij.process.*;
import ij.plugin.*;
import ij.plugin.filter.*;
import ij.gui.*;

public class Java_GrayErode implements PluginFilter {

	ImagePlus imp;

	public void setup(String arg, ImagePlus imp) {

		if (arg.equals("about")) {
			showAbout();
			return;
		}
		this.imp = imp;
		return DOES_8G;

}
