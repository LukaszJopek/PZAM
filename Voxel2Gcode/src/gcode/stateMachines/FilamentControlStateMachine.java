package gcode.stateMachines;

import java.util.HashMap;
import java.util.Map;

import imageProcessing.Geometry;
import utils.Point2D;

public class FilamentControlStateMachine {

	private static FilamentControlStateMachine filamentControlStateMachine = null;
	private State state = null;
	private Map<StateType, State> stateList;
	private Geometry geometry;
	private Point2D previousPoint = null;
	
	private FilamentControlStateMachine() {
		this.stateList = createStateList();
		this.state = stateList.get(StateType.FM);
		this.previousPoint = new Point2D(0,0);
	}
	public static FilamentControlStateMachine getIntance() {
		if (filamentControlStateMachine == null) {
			filamentControlStateMachine = new FilamentControlStateMachine();
		}
		return filamentControlStateMachine;
	}

	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	private Map<StateType, State> createStateList(){
		Map<StateType,State> stateList = new HashMap<StateType,State>();
		stateList.put(StateType.FM, new FreeMove());
		stateList.put(StateType.PFM, new PrepareFreeMove());
		stateList.put(StateType.PAP, new PrepareAndPrinting());
		stateList.put(StateType.PR, new Printing());
		stateList.put(StateType.NS, new NextSlice());
		stateList.put(StateType.END, new EndState());
		stateList.put(StateType.ERROR, new ErrorState());		
		return stateList;
	}
	public String generateNewCommnand(EventType eventType, Point2D nextPoint, int slice) {
		StateType prevState = state.getState();
		state = state.getNextState(stateList, eventType);
		if (state.getState() == StateType.ERROR) {
			System.err.println("Blad stanu. Proba przejscia ze stanu "+prevState.toString()+" pod wplywem wydzarzenia: "+eventType.toString());
			return "";
		}		
		state.setGeometry(geometry);
		state.setNextPoint(nextPoint);
		state.getDistance(previousPoint);
		state.setSlice(slice);
		previousPoint = nextPoint;
		return state.generateCGodeCommand();
	}
	
	public StateType getState() {
		return state.getState();
	}

}
