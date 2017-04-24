package org.yakindu.scr.elevatormodeling;

import org.yakindu.scr.elevatormodeling.IElevatorModelingStatemachine.SCIElevatorOperationCallback;

public class ElevatorCallback implements SCIElevatorOperationCallback {

	@Override
	public void stop(long floor) {
		System.out.println("Elevator is stopping at floor " + floor);
	}

	@Override
	public void emergency() {
		System.out.println("Elevator is in emergency stop mode");
	}

	@Override
	public void emergencyOverride() {
		System.out.println("Elevator will resume normal service");
	}

}
