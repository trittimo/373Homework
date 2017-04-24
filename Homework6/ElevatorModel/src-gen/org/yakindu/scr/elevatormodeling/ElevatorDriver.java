package org.yakindu.scr.elevatormodeling;

public class ElevatorDriver {
	public static void main(String[] args) {
		ElevatorModelingStatemachine sm = new ElevatorModelingStatemachine();
		
		IElevatorModelingStatemachine.SCIElevator elevator = sm.getSCIElevator();
		IElevatorModelingStatemachine.SCIUser user = sm.getSCIUser();
		
		elevator.setSCIElevatorOperationCallback(new ElevatorCallback());
		
		sm.init();
		sm.enter();
		
		System.out.println("Calling to floor 1");
		user.raiseCallElevator1();
		sm.runCycle();
		
		System.out.println("Calling to floor 2");
		user.raiseCallElevator2();
		sm.runCycle();
		
		System.out.println("Calling to floor 3");
		user.raiseCallElevator3();
		sm.runCycle();
		
		System.out.println("Calling to floor 2");
		user.raiseCallElevator2();
		sm.runCycle();
		
		System.out.println("Calling to floor 1");
		user.raiseCallElevator1();
		sm.runCycle();
		
		System.out.println("Hitting emergency stop");
		user.raiseEmergencyStop();
		sm.runCycle();
		
		
		System.out.println("Attempting call to floor 1");
		user.raiseCallElevator1();
		sm.runCycle();
		
		System.out.println("Emergency override");
		user.raiseEmergencyOverride();
		sm.runCycle();
		
		System.out.println("Calling to floor 1");
		user.raiseCallElevator1();
		sm.runCycle();
		
		sm.exit();
	}
}
