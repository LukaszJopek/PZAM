package gcode.stateMachines;

import java.util.Map;

import imageProcessing.Geometry;
import imageProcessing.Geometry.Axis;
import utils.GCodeUtils;
import utils.Point2D;

public class ErrorState implements State{
	private int slice;
	private Geometry geometry;
	private Point2D nextPointPosition;
	private float segmentLength;
	@Override
	public StateType getState() {
		return StateType.ERROR;
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
	public String generateCGodeCommand() {
		return "";
	}

	@Override
	public void setSlice(int slice) {
		this.slice = slice;		
	}
	@Override
	public State getNextState(Map<StateType, State> stateList, EventType eventType) {
		return null;
	}
	@Override
	public double getDistance(Point2D previousPoint) {	
		return 0.0;
	}

}
