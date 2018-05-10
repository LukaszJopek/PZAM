package utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import gcode.GCodeBlock;
import gcode.GCodeMovementCommands;
import gcode.GCodeProperties;
import gcode.stateMachines.EventType;
import gcode.stateMachines.FilamentControlStateMachine;
import gcode.stateMachines.StateType;
import imageProcessing.Geometry.Axis;

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

public static String createCommand(GCodeMovementCommands G, Double xmm, Double ymm, Double zmm, Float E, Float F) {
	String gcodeCommand = "";
	if(G != null) {
		gcodeCommand = G.toString();
	}
	if(xmm != null) {
		gcodeCommand = gcodeCommand + " X"+GridUtils.getPositionInMM(xmm, Axis.OX)+ " ";
	}
	if(ymm != null) {
		gcodeCommand = gcodeCommand + " Y"+GridUtils.getPositionInMM(ymm, Axis.OY)+ " ";
	}
	if(zmm != null) {
		gcodeCommand = gcodeCommand + " Z"+GridUtils.getPositionInMM(zmm, Axis.OZ)+ " ";
	}
	if(E != null) {
		gcodeCommand = gcodeCommand + " E"+E+ " ";
	}
	if(F != null) {
		gcodeCommand = gcodeCommand + " F"+F+ " ";
	}
	return gcodeCommand;
}
public static String createCommand(GCodeMovementCommands G, String comment) {
	return "; "+comment;
}

public static List<String> endPrintSequence() {
	FilamentControlStateMachine filamentControlStateMachine = FilamentControlStateMachine.getIntance();
	if (filamentControlStateMachine.getState().equals(StateType.END)) {
		System.out.println("Ending g-code command sequence is already set. Nothing to do. Return.");
		return new ArrayList<String>();
	}
	System.out.println("Creating ending g-code commnads sequence.");
	return filamentControlStateMachine.generateNewCommnand(EventType.MODEL_END, new Point2D(0,0), 0);
	
}
}
