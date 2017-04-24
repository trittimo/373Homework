package client;

import org.yakindu.scr.callhandling.ICallHandlingStatemachine;

public class PhoneCallback implements ICallHandlingStatemachine.SCIPhoneOperationCallback {

	@Override
	public void ring(long count) {
		System.out.println("Ring " + count);
	}

}
