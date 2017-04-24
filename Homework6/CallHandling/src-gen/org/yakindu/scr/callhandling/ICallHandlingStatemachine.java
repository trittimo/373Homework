package org.yakindu.scr.callhandling;

import org.yakindu.scr.IStatemachine;
import org.yakindu.scr.ITimerCallback;

public interface ICallHandlingStatemachine extends ITimerCallback,IStatemachine {

	public interface SCIUser {
	
		public void raiseAcceptCall();
		
		public void raiseDismissCall();
		
		public void setSCIUserOperationCallback(SCIUserOperationCallback operationCallback);
	
	}
	
	public interface SCIUserOperationCallback {
	
		public void speak();
		
	}
	
	public SCIUser getSCIUser();
	
	public interface SCIPhone {
	
		public void raiseIncomingCall();
		
		public long getDuration();
		
		public void setDuration(long value);
		
		public long getCount();
		
		public void setCount(long value);
		
		public void setSCIPhoneOperationCallback(SCIPhoneOperationCallback operationCallback);
	
	}
	
	public interface SCIPhoneOperationCallback {
	
		public void ring(long count);
		
	}
	
	public SCIPhone getSCIPhone();
	
}
