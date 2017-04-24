package org.yakindu.scr.elevatormodeling;

import org.yakindu.scr.IStatemachine;

public interface IElevatorModelingStatemachine extends IStatemachine {

	public interface SCIUser {
	
		public void raiseCallElevator1();
		
		public void raiseCallElevator2();
		
		public void raiseCallElevator3();
		
		public void raiseEmergencyStop();
		
		public void raiseEmergencyOverride();
		
	}
	
	public SCIUser getSCIUser();
	
	public interface SCIElevator {
	
		public long getFloor();
		
		public void setFloor(long value);
		
		public void setSCIElevatorOperationCallback(SCIElevatorOperationCallback operationCallback);
	
	}
	
	public interface SCIElevatorOperationCallback {
	
		public void stop(long floor);
		
		public void emergency();
		
		public void emergencyOverride();
		
	}
	
	public SCIElevator getSCIElevator();
	
}
