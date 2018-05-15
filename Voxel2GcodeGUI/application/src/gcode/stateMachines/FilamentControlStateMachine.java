package application.src.gcode.stateMachines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.src.imageProcessing.Geometry;
import application.src.utils.Point2D;


public class FilamentControlStateMachine {

	private static FilamentControlStateMachine filamentControlStateMachine = null;
	private State state = null;
	private Map<StateType, State> stateList;
	private Geometry geometry;
	private Point2D previousPoint = null;
	private float currentE = 0;
	
	private FilamentControlStateMachine() {
		this.stateList = createStateList();
		this.state = stateList.get(StateType.INITIAL);
		this.previousPoint = new Point2D(0,0);
		 currentE = 0;
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
		stateList.put(StateType.INITIAL, new Initial());
		stateList.put(StateType.FM, new FreeMove());
		stateList.put(StateType.PFM, new PrepareFreeMove());
		stateList.put(StateType.PAP, new PrepareAndPrinting());
		stateList.put(StateType.PR, new Printing());
		stateList.put(StateType.NS, new NextSlice());
		stateList.put(StateType.END, new EndState());
		stateList.put(StateType.ERROR, new ErrorState());		
		return stateList;
	}
	public List<String> generateNewCommnand(EventType eventType, Point2D nextPoint, int slice) {
		//geometry.convertPositionInMM(nextPoint);
		StateType prevStateType = state.getState();
		State prevState = state.getClone();
		state = state.getNextState(stateList, eventType);
		currentE = prevState.getE();
		if (state.getState() == StateType.ERROR) {
			System.err.println("Blad stanu. Proba przejscia ze stanu "+prevStateType.toString()+" pod wplywem wydzarzenia: "+eventType.toString());
			return new ArrayList<String>();
		}	
		System.out.println("FSM: "+prevStateType.toString()+" --> "+state.getState().name());
		state.setCurrentE(prevState.getE());
		state.setPreviousState(prevState);
		state.setGeometry(geometry);
		state.setPreviousPoint(previousPoint);
		state.setNextPoint(nextPoint);
		state.getDistance(previousPoint);
		state.setSlice(slice);
		state.setLastRectract(prevState.getLastRectract());
		previousPoint = nextPoint;
		return state.generateCGodeCommand();
	}
	
	public StateType getState() {
		return state.getState();
	}

}
