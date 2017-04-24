package org.yakindu.scr.callhandling;
import org.yakindu.scr.ITimer;

public class CallHandlingStatemachine implements ICallHandlingStatemachine {

	protected class SCIUserImpl implements SCIUser {
	
		private SCIUserOperationCallback operationCallback;
		
		public void setSCIUserOperationCallback(
				SCIUserOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}
		private boolean acceptCall;
		
		public void raiseAcceptCall() {
			acceptCall = true;
		}
		
		private boolean dismissCall;
		
		public void raiseDismissCall() {
			dismissCall = true;
		}
		
		protected void clearEvents() {
			acceptCall = false;
			dismissCall = false;
		}
	}
	
	protected SCIUserImpl sCIUser;
	
	protected class SCIPhoneImpl implements SCIPhone {
	
		private SCIPhoneOperationCallback operationCallback;
		
		public void setSCIPhoneOperationCallback(
				SCIPhoneOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}
		private boolean incomingCall;
		
		public void raiseIncomingCall() {
			incomingCall = true;
		}
		
		private long duration;
		
		public long getDuration() {
			return duration;
		}
		
		public void setDuration(long value) {
			this.duration = value;
		}
		
		private long count;
		
		public long getCount() {
			return count;
		}
		
		public void setCount(long value) {
			this.count = value;
		}
		
		protected void clearEvents() {
			incomingCall = false;
		}
	}
	
	protected SCIPhoneImpl sCIPhone;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_Idle,
		main_region_IncomingCall,
		main_region_ActiveCall,
		main_region_DismissCall,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[3];
	public CallHandlingStatemachine() {
		sCIUser = new SCIUserImpl();
		sCIPhone = new SCIPhoneImpl();
	}
	
	public void init() {
		this.initialized = true;
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
		sCIPhone.setDuration(0);
		
		sCIPhone.setCount(0);
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		}
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		enterSequence_main_region_default();
	}
	
	public void exit() {
		exitSequence_main_region();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCIUser.clearEvents();
		sCIPhone.clearEvents();
		for (int i=0; i<timeEvents.length; i++) {
			timeEvents[i] = false;
		}
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case main_region_Idle:
			return stateVector[0] == State.main_region_Idle;
		case main_region_IncomingCall:
			return stateVector[0] == State.main_region_IncomingCall;
		case main_region_ActiveCall:
			return stateVector[0] == State.main_region_ActiveCall;
		case main_region_DismissCall:
			return stateVector[0] == State.main_region_DismissCall;
		default:
			return false;
		}
	}
	
	/**
	* Set the {@link ITimer} for the state machine. It must be set
	* externally on a timed state machine before a run cycle can be correct
	* executed.
	* 
	* @param timer
	*/
	public void setTimer(ITimer timer) {
		this.timer = timer;
	}
	
	/**
	* Returns the currently used timer.
	* 
	* @return {@link ITimer}
	*/
	public ITimer getTimer() {
		return timer;
	}
	
	public void timeElapsed(int eventID) {
		timeEvents[eventID] = true;
	}
	
	public SCIUser getSCIUser() {
		return sCIUser;
	}
	
	public SCIPhone getSCIPhone() {
		return sCIPhone;
	}
	
	private boolean check_main_region_Idle_tr0_tr0() {
		return sCIPhone.incomingCall;
	}
	
	private boolean check_main_region_IncomingCall_tr0_tr0() {
		return sCIUser.acceptCall;
	}
	
	private boolean check_main_region_IncomingCall_tr1_tr1() {
		return sCIUser.dismissCall;
	}
	
	private boolean check_main_region_IncomingCall_lr1_lr1() {
		return timeEvents[0];
	}
	
	private boolean check_main_region_ActiveCall_tr0_tr0() {
		return sCIUser.dismissCall;
	}
	
	private boolean check_main_region_ActiveCall_lr1_lr1() {
		return timeEvents[1];
	}
	
	private boolean check_main_region_DismissCall_tr0_tr0() {
		return timeEvents[2];
	}
	
	private void effect_main_region_Idle_tr0() {
		exitSequence_main_region_Idle();
		enterSequence_main_region_IncomingCall_default();
	}
	
	private void effect_main_region_IncomingCall_tr0() {
		exitSequence_main_region_IncomingCall();
		enterSequence_main_region_ActiveCall_default();
	}
	
	private void effect_main_region_IncomingCall_tr1() {
		exitSequence_main_region_IncomingCall();
		enterSequence_main_region_DismissCall_default();
	}
	
	private void effect_main_region_IncomingCall_lr1_lr1() {
		sCIPhone.operationCallback.ring(sCIPhone.getCount());
		
		sCIPhone.setCount(sCIPhone.getCount() + 1);
	}
	
	private void effect_main_region_ActiveCall_tr0() {
		exitSequence_main_region_ActiveCall();
		enterSequence_main_region_DismissCall_default();
	}
	
	private void effect_main_region_ActiveCall_lr1_lr1() {
		sCIUser.operationCallback.speak();
		
		sCIPhone.setDuration(sCIPhone.getDuration() + 1);
	}
	
	private void effect_main_region_DismissCall_tr0() {
		exitSequence_main_region_DismissCall();
		enterSequence_main_region_Idle_default();
	}
	
	/* Entry action for state 'IncomingCall'. */
	private void entryAction_main_region_IncomingCall() {
		timer.setTimer(this, 0, 2 * 1000, true);
		
		sCIPhone.setCount(0);
	}
	
	/* Entry action for state 'ActiveCall'. */
	private void entryAction_main_region_ActiveCall() {
		timer.setTimer(this, 1, 1 * 1000, true);
		
		sCIPhone.setDuration(0);
	}
	
	/* Entry action for state 'DismissCall'. */
	private void entryAction_main_region_DismissCall() {
		timer.setTimer(this, 2, 2 * 1000, false);
	}
	
	/* Exit action for state 'IncomingCall'. */
	private void exitAction_main_region_IncomingCall() {
		timer.unsetTimer(this, 0);
	}
	
	/* Exit action for state 'ActiveCall'. */
	private void exitAction_main_region_ActiveCall() {
		timer.unsetTimer(this, 1);
	}
	
	/* Exit action for state 'DismissCall'. */
	private void exitAction_main_region_DismissCall() {
		timer.unsetTimer(this, 2);
	}
	
	/* 'default' enter sequence for state Idle */
	private void enterSequence_main_region_Idle_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Idle;
	}
	
	/* 'default' enter sequence for state IncomingCall */
	private void enterSequence_main_region_IncomingCall_default() {
		entryAction_main_region_IncomingCall();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_IncomingCall;
	}
	
	/* 'default' enter sequence for state ActiveCall */
	private void enterSequence_main_region_ActiveCall_default() {
		entryAction_main_region_ActiveCall();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_ActiveCall;
	}
	
	/* 'default' enter sequence for state DismissCall */
	private void enterSequence_main_region_DismissCall_default() {
		entryAction_main_region_DismissCall();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_DismissCall;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* Default exit sequence for state Idle */
	private void exitSequence_main_region_Idle() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state IncomingCall */
	private void exitSequence_main_region_IncomingCall() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_IncomingCall();
	}
	
	/* Default exit sequence for state ActiveCall */
	private void exitSequence_main_region_ActiveCall() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_ActiveCall();
	}
	
	/* Default exit sequence for state DismissCall */
	private void exitSequence_main_region_DismissCall() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_DismissCall();
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case main_region_Idle:
			exitSequence_main_region_Idle();
			break;
		case main_region_IncomingCall:
			exitSequence_main_region_IncomingCall();
			break;
		case main_region_ActiveCall:
			exitSequence_main_region_ActiveCall();
			break;
		case main_region_DismissCall:
			exitSequence_main_region_DismissCall();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state Idle. */
	private void react_main_region_Idle() {
		if (check_main_region_Idle_tr0_tr0()) {
			effect_main_region_Idle_tr0();
		}
	}
	
	/* The reactions of state IncomingCall. */
	private void react_main_region_IncomingCall() {
		if (check_main_region_IncomingCall_tr0_tr0()) {
			effect_main_region_IncomingCall_tr0();
		} else {
			if (check_main_region_IncomingCall_tr1_tr1()) {
				effect_main_region_IncomingCall_tr1();
			} else {
				if (check_main_region_IncomingCall_lr1_lr1()) {
					effect_main_region_IncomingCall_lr1_lr1();
				}
			}
		}
	}
	
	/* The reactions of state ActiveCall. */
	private void react_main_region_ActiveCall() {
		if (check_main_region_ActiveCall_tr0_tr0()) {
			effect_main_region_ActiveCall_tr0();
		} else {
			if (check_main_region_ActiveCall_lr1_lr1()) {
				effect_main_region_ActiveCall_lr1_lr1();
			}
		}
	}
	
	/* The reactions of state DismissCall. */
	private void react_main_region_DismissCall() {
		if (check_main_region_DismissCall_tr0_tr0()) {
			effect_main_region_DismissCall_tr0();
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_Idle_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case main_region_Idle:
				react_main_region_Idle();
				break;
			case main_region_IncomingCall:
				react_main_region_IncomingCall();
				break;
			case main_region_ActiveCall:
				react_main_region_ActiveCall();
				break;
			case main_region_DismissCall:
				react_main_region_DismissCall();
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
}
