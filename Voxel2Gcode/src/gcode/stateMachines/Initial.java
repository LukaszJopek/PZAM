package gcode.stateMachines;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gcode.GCodeMovementCommands;
import gcode.GCodeProperties;
import imageProcessing.Geometry;
import utils.GCodeUtils;
import utils.Point2D;

public class Initial implements State{
	private State prevState = null;
	private Point2D nextPointPosition;

	@Override
	public void setSlice(int slice) {
		
	}

	@Override
	public void setPreviousPoint(Point2D previousPoint) {
		
	}

	@Override
	public void setNextPoint(Point2D position) {
		this.nextPointPosition = position;
		
	}

	@Override
	public void setGeometry(Geometry geometry) {
		
	}

	@Override
	public StateType getState() {
		return StateType.INITIAL;
	}

	@Override
	public float getE() {
		return 0;
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
	public List<String> generateCGodeCommand() {
		List<String> commands = new ArrayList<String>();
		commands.add(GCodeUtils.createCommand(GCodeMovementCommands.G1,(Double)nextPointPosition.getxMM(),(Double)nextPointPosition.getyMM(),null, null, null));
		return commands;
	}

	@Override
	public double getDistance(Point2D previousPoint) {
		return 0;
	}

	@Override
	public State getNextState(Map<StateType, State> stateList, EventType eventType) {
		switch (eventType) {
		case INITIAL:
			return stateList.get(StateType.INITIAL);
		case NEW_POINT:
			return stateList.get(StateType.ERROR);
		case NEW_PATH : 
			return stateList.get(StateType.PAP);
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
	public void setPreviousState(State prevState) {
		this.prevState = prevState;
	}

	@Override
	public void setCurrentE(float currentE) {
		// TODO Auto-generated method stub
		
	}

}
