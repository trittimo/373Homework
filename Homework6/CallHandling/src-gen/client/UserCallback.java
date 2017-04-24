package client;

import org.yakindu.scr.callhandling.ICallHandlingStatemachine;

public class UserCallback implements ICallHandlingStatemachine.SCIUserOperationCallback {

	@Override
	public void speak() {
		System.out.println("Blah ...");
	}
	
}
