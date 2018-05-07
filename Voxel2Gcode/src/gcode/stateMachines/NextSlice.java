package gcode.stateMachines;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gcode.GCodeMovementCommands;
import gcode.GCodeProperties;
import imageProcessing.Geometry;
import imageProcessing.Geometry.Axis;
import utils.GCodeUtils;
import utils.Point2D;

public class NextSlice implements State{
	float currentE;
	private State prevState = null;
	private int slice;
	private Geometry geometry;
	private Point2D previousPoint;
	private Point2D nextPointPosition;
	private float zShift = 0;

	@Override
	public StateType getState() {
		return StateType.NS;
	}

	@Override
	public float getE() {
		return currentE;
	}

	@Override
	public float getF() {
		return GCodeProperties.HighSpeed;
	}

	@Override
	public float getZShift() {
		return zShift;
	}
	
	public void setZShift(float z) {
		zShift = z;
	}

	@Override
	public void setSlice(int slice) {
		this.slice = slice;		
	}

	@Override
	public void setNextPoint(Point2D position) {
		this.nextPointPosition = position;	
	}

	@Override
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public List<String> generateCGodeCommand() {
		List<String> commands = new ArrayList<String>();
		//commands.add(GCodeUtils.createCommand(GCodeMovementCommands.Comment, " Jump to new slice"));
		Double zmm = (double) slice; /*(double) geometry.convertPositionInMM((float) slice, Axis.OZ,0);*/
		System.out.println("Z = "+zmm);
		commands.add(GCodeUtils.createCommand(GCodeMovementCommands.G1,(Double)nextPointPosition.getxMM(),(Double)nextPointPosition.getyMM(),zmm, null, getF()));

		return commands;
	}
	@Override
	public State getNextState(Map<StateType, State> stateList, EventType eventType) {
		switch (eventType) {
		case NEW_POINT:
			return stateList.get(StateType.PAP);
		case NEW_PATH : 
			return stateList.get(StateType.NS);
		case LAYER_END :
			return stateList.get(StateType.NS);
		case MODEL_END:
			return stateList.get(StateType.END);
		case LAST_POINT:
			return stateList.get(StateType.PFM);
		default:
			return stateList.get(StateType.ERROR);
		}
	}
	@Override
	public double getDistance(Point2D previousPoint) {	
		return nextPointPosition.getDistance(previousPoint);
	}

	@Override
	public void setPreviousPoint(Point2D previousPoint) {
		this.previousPoint = previousPoint;
		
	}

	@Override
	public void setPreviousState(State prevState) {
		this.prevState = prevState;	
	}

	@Override
	public void setCurrentE(float currentE) {
		this.currentE = currentE;
		
	}

	@Override
	public State getClone() {
		State state = new NextSlice();
		state.setCurrentE(currentE);
		state.setGeometry(geometry);
		state.setNextPoint(nextPointPosition);
		return state;
	}

}
