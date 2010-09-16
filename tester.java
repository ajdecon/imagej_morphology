import ij.*;
import ij.process.*;
import org.ajdecon.morphology.*;

public class tester {

	public static void main(String[] args) {

		System.out.println("Generating source image.");
		ByteProcessor bp = new ByteProcessor(50,50);
		// BG white.
		for (int x=0;x<50;x++) {
			for (int y=0;y<50;y++) {
				bp.set(x,y,255);
			}
		}
		IJ.save(new ImagePlus(" ",bp),"white.tif");
		bp.fillOval(10,10,10,10);

//		ImagePlus source = new ImagePlus("source",bp);
		ImagePlus source = IJ.openImage("./biggray.tif");
		System.out.println("Generating structuring element.");
		StructElement se = new StructElement(StructElement.CIRCLE,3,true);

		System.out.println("Operating.");
		ImagePlus d = Morphology.gradient(source,se);

		System.out.println("Saving");
		IJ.save(d,"result.tif");
		IJ.save(source,"source.tif");
		IJ.save(se.getImage(), "se.tif");
	}
}
