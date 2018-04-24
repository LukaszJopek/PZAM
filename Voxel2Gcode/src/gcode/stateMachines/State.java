package gcode.stateMachines;

import java.util.Map;

import imageProcessing.Geometry;
import utils.Point2D;

public interface State {
	public void setSlice(int slice);
	public void setNextPoint(Point2D position);
	public void setGeometry(Geometry geometry);
	public StateType getState();
	public float getE();
	public float getF();
	public float getZShift();
	public String generateCGodeCommand();
	public double getDistance(Point2D previousPoint);
	public State getNextState(Map<StateType, State> stateList, EventType eventType);
}
