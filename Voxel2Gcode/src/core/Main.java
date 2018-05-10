package core;

import java.io.IOException;

import gcode.GCodeGenerator;
import gcode.GCodeProperties;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		//Image image2 = new Image( "D:\\MetaMaterialy\\3DPrinting\\mmf\\lattice3D.raw",  100,  100,  100, 8);
       	//Image image2 = new Image( "D:\\PL\\dydaktyka\\GIT\\Voxel2Gcode\\data\\lattice3D.raw",  70,  70,  60, 8);
       	//Image image2 = new Image( "D:\\MetaMaterialy\\soczewkaPlaska15x15\\lattice3D.raw",  610,  610,  70, 8);
    	Image image2 = new Image( "D:\\MetaMaterialy\\Lens 9x9\\CorrectedLattice3D.raw",  360,  360,  68, 8);
    	//Image image2 = new Image( "D:\\MetaMaterialy\\Lens 9x9\\Test3DModel.raw",  100,  100,  10, 8);
       	generateGcode(image2);
       	
		MainGuiClass mainGuiClass = new MainGuiClass();
		mainGuiClass.createGUI();
       	

       	
       	System.out.println("Done.");
	
}
	
	private static void generateGcode(Image image) {
		GCodeProperties gCodeProperties = new GCodeProperties();
		gCodeProperties.setParameterE(0.2345f);
		gCodeProperties.setParameterF(200.0f);
		GCodeGenerator gcodeGenerator = new GCodeGenerator(image, gCodeProperties);
		gcodeGenerator.execute();
		
	}
	//private void 
	

}
