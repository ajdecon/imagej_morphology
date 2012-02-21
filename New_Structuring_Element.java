import java.awt.*;
import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import org.ajdecon.morphology.*;

public class New_Structuring_Element implements PlugIn {

	public void run(String arg) {

		String[] available = {"circle","rectangle","square","line","ring"};
		GenericDialog inputs = structDialog(available);
		inputs.showDialog();
		if (inputs.wasCanceled()) return;

		String seName = inputs.getNextChoice();
		int size1 = (int)inputs.getNextNumber();
		int size2 = (int)inputs.getNextNumber();
		boolean bgWhite = inputs.getNextBoolean();
		
		StructElement se;
		if (seName.equals(available[0])) {
			se = new StructElement(StructElement.CIRCLE,size1,bgWhite);
		} else if (seName.equals(available[1])) {
			se = new StructElement(StructElement.RECT,size1,size2,bgWhite);
		} else if (seName.equals(available[2])) {
			se = new StructElement(StructElement.SQUARE,size1,bgWhite);
		} else if (seName.equals(available[3])) {
			se = new StructElement(StructElement.LINE,size1,size2,bgWhite);
		} else {
			se = new StructElement(StructElement.RING,size1,size2,bgWhite);
		}

		ImagePlus e = se.getImage();
		e.show();
	}

	private GenericDialog structDialog(String[] choices) {
        
		GenericDialog result = new GenericDialog("New Structuring Element");
		result.addMessage("New structuring element\n"+
			"Size 1 is the radius of a circle, the width of a rectangle or "+
			"square, the length of a line or the inner radius of a ring.\n"+
			"Size 2 is the height of a rectangle, the angle of a line in degrees, "+
			"or the outer radius of a ring.\nSizes of lines, squares and rectangles must be odd numbers!");
		result.addChoice("Choose an element:",choices,choices[0]);
		result.addNumericField("Size 1:", 1.0, 0);
		result.addNumericField("Size 2:", 1.0, 0);
		result.addCheckbox("Background is white:",true);

		return result;		
	}
}
