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

public class FreeMove implements State{
	private double lastRectract = 0;
	private float currentE = 0;
	private State prevState = null;
	private int slice;
	private Geometry geometry;
	private Point2D previousPoint;
	private Point2D nextPointPosition;

	@Override
	public StateType getState() {
		return StateType.FM;
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
		return 0;
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
		//commands.add(GCodeUtils.createCommand(GCodeMovementCommands.Comment, " Free Move "));
		commands.add(GCodeUtils.createCommand(GCodeMovementCommands.G0,(Double)nextPointPosition.getxMM(),(Double)nextPointPosition.getyMM(),null,null,null));
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
			return stateList.get(StateType.PAP);
		case NEW_PATH : 
			return stateList.get(StateType.ERROR);
		case LAST_POINT:
			return stateList.get(StateType.PFM);
		case LAYER_END :
			return stateList.get(StateType.NS);
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
		State state = new FreeMove();
		state.setCurrentE(currentE);
		state.setGeometry(geometry);
		state.setNextPoint(nextPointPosition);
		state.setLastRectract(lastRectract);
		return state;
	}
	@Override
	public void setLastRectract(double filamentRetract) {
		this.lastRectract = filamentRetract;
		
	}

	@Override
	public double getLastRectract() {
		return lastRectract;
	}

}
