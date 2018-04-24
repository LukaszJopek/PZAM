package gcode.stateMachines;

import java.util.Map;

import gcode.GCodeProperties;
import imageProcessing.Geometry;
import imageProcessing.Geometry.Axis;
import utils.GCodeUtils;
import utils.Point2D;

public class Printing implements State {
	private int slice;
	private Geometry geometry;
	private Point2D nextPointPosition;
	private double segmentLength;

	@Override
	public StateType getState() {
		return StateType.PR;
	}

	@Override
	public float getE() {
		return (float) (GCodeProperties.filamentShiftConst * segmentLength);
	}

	@Override
	public float getF() {
		return GCodeProperties.printingSpeed;
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
	public String generateCGodeCommand() {
		float zmm = geometry.getPositionInMM(slice, Axis.OZ);
		return GCodeUtils.createCommand(nextPointPosition.getxMM(),nextPointPosition.getyMM(),zmm, getE(), getF());
	}

	@Override
	public void setSlice(int slice) {
		this.slice = slice;		
	}
	@Override
	public State getNextState(Map<StateType, State> stateList, EventType eventType) {
		switch (eventType) {
		case NEW_POINT:
			return stateList.get(StateType.PR);
		case NEW_PATH : 
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
		this.segmentLength = nextPointPosition.getDistance(previousPoint);
		return nextPointPosition.getDistance(previousPoint);
	}

}
