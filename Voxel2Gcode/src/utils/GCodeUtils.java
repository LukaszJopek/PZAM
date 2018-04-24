package utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import gcode.GCodeBlock;
import gcode.GCodeMovementCommands;
import gcode.GCodeProperties;

public class GCodeUtils {
 public static String createNewLineCommand(float xmm, float ymm, float zmm, GCodeProperties gCodeProperties) {
	 String gcodeCommand = GCodeMovementCommands.G0.toString();
	 return gcodeCommand + " X"+xmm+" Y"+ymm+" Z"+zmm+" E"+gCodeProperties.getParameterE()+" F"+gCodeProperties.getParameterF();
 }
public static void gCodeWriterOld(List<List<GCodeBlock>> slices, List<GCodeBlock> header,  GCodeProperties gCodeProperties) {
	System.out.println("generating output g-code file...");	
	PrintWriter writer;
	try {
		writer = new PrintWriter(gCodeProperties.getOutPutFilename(), "UTF-8");
		System.out.println("generating header of file...");
		for (GCodeBlock gcodeBlock : header) {
			writer.println(gcodeBlock.getCGodeCommand());
		}
		System.out.println("exporting of movement commands...");
		for (List<GCodeBlock> slice : slices) {
			for (GCodeBlock gcodeBlock : slice) {
			writer.println(gcodeBlock.getCGodeCommand());
			}
		}
		writer.close();
		System.out.println("done.");
	} catch (FileNotFoundException | UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static void gCodeWriter(List<List<String>> slices, List<GCodeBlock> header,  GCodeProperties gCodeProperties) {
	System.out.println("generating output g-code file...");	
	PrintWriter writer;
	try {
		writer = new PrintWriter(gCodeProperties.getOutPutFilename(), "UTF-8");
		System.out.println("generating header of file...");
		for (GCodeBlock gcodeBlock : header) {
			writer.println(gcodeBlock.getCGodeCommand());
		}
		System.out.println("exporting of movement commands...");
		for (List<String> slice : slices) {
			for (String gCodeCommand : slice) {
			writer.println(gCodeCommand);
			}
		}
		writer.close();
		System.out.println("done.");
	} catch (FileNotFoundException | UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static String InitialMove(float xmm, float ymm, float zmm, GCodeProperties gCodeProperties) {
	String gcodeCommand = GCodeMovementCommands.G1.toString();
	return gcodeCommand + " X"+xmm+" Y"+ymm+" Z"+zmm+" E"+0+" F"+gCodeProperties.getParameterF();
}
public static String createPrintingCommand(float xmm, float ymm, float zmm, GCodeProperties gCodeProperties) {
	return createNewLineCommand(xmm, ymm, zmm, gCodeProperties);
}
public static String createCommand(double xmm, double ymm, double zmm, float E, float F) {
	String gcodeCommand = GCodeMovementCommands.G1.toString();
	return gcodeCommand + " X"+xmm+" Y"+ymm+" Z"+zmm+" E"+E+" F"+F;
}

}
