package core;

import gcode.stateMachines.EventType;
import gcode.stateMachines.FilamentControlStateMachine;
import imageProcessing.Geometry;
import utils.Point2D;

public class Tests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImageInfo imageInfo  = new ImageInfo("",150,150,150,8);
		Geometry geometry = new Geometry(imageInfo, 0.01f, 0.01f, 0.01f);
		FilamentControlStateMachine fsm = FilamentControlStateMachine.getIntance();
		fsm.setGeometry(geometry);
		String s = fsm.generateNewCommnand(EventType.NEW_POINT, new Point2D(0.33, 0.45), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.NEW_POINT, new Point2D(23, 11), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.NEW_POINT, new Point2D(23, 15), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.NEW_PATH, new Point2D(24, 24), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.NEW_POINT, new Point2D(67, 93), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.NEW_POINT, new Point2D(670, 930), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.NEW_POINT, new Point2D(672, 931), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.NEW_PATH, new Point2D(800, 800), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.NEW_POINT, new Point2D(802, 802), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.NEW_POINT, new Point2D(804, 804), 0);
		System.out.println("Command = "+s);
		s = fsm.generateNewCommnand(EventType.LAYER_END, new Point2D(804, 804), 0);
		System.out.println("Command = "+s);
	}

}
