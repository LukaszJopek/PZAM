package gcode.stateMachines;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gcode.GCodeMovementCommands;
import imageProcessing.Geometry;
import imageProcessing.Geometry.Axis;
import utils.GCodeUtils;
import utils.Point2D;

public class EndState implements State{
	private double lastRectract = 0;
	private float currentE = 0;
	private int slice;
	private Geometry geometry;
	private Point2D nextPointPosition;
	private float segmentLength;
	@Override
	public StateType getState() {
		return StateType.END;
	}

	@Override
	public float getE() {
		return 0;
	}

	@Override
	public float getF() {
		return 0;
	}

	@Override
	public float getZShift() {
		return 0;
	}

	public void setSegmentLength(float segmentLength) {
		this.segmentLength = segmentLength;
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
		commands.add(GCodeUtils.createCommand(GCodeMovementCommands.Comment, " END STATE "));
		commands.add("M104 S0 T0");
		commands.add("M109 S0 T0");
		return commands;
	}

	@Override
	public void setSlice(int slice) {
		this.slice = slice;		
	}
	@Override
	public State getNextState(Map<StateType, State> stateList, EventType eventType) {
		switch (eventType) {
		case NEW_POINT:
			return stateList.get(StateType.ERROR);
		case NEW_PATH : 
			return stateList.get(StateType.END);
		case LAYER_END :
			return stateList.get(StateType.END);
		case MODEL_END:
			return stateList.get(StateType.END);
		default:
			return stateList.get(StateType.ERROR);
		}
	}
	@Override
	public double getDistance(Point2D previousPoint) {	
		return nextPointPosition.getDistanceinMM(previousPoint);
	}

	@Override
	public void setPreviousPoint(Point2D previousPoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPreviousState(State prevState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrentE(float currentE) {
		this.currentE = currentE;
		
	}

	@Override
	public State getClone() {
		State state = new EndState();
		state.setCurrentE(currentE);
		state.setGeometry(geometry);
		state.setNextPoint(nextPointPosition);
		state.setLastRectract(lastRectract);
		return state;
	}
	@Override
	public void setLastRectract(double filamentRetract) {
		this.lastRectract = lastRectract;
		
	}

	@Override
	public double getLastRectract() {
		return lastRectract;
	}

}
