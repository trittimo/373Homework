package client;

import org.yakindu.scr.TimerService;
import org.yakindu.scr.callhandling.CallHandlingStatemachine;
import org.yakindu.scr.callhandling.ICallHandlingStatemachine;

public class CallHandlingDriver {

	public static void main(String[] args) throws InterruptedException {
		CallHandlingStatemachine sm = new CallHandlingStatemachine();
		sm.setTimer(new TimerService());
		
		ICallHandlingStatemachine.SCIUser user = sm.getSCIUser();
		ICallHandlingStatemachine.SCIPhone phone = sm.getSCIPhone();
		
		user.setSCIUserOperationCallback(new UserCallback());
		phone.setSCIPhoneOperationCallback(new PhoneCallback());		
		
		// Initialize and start the state machine
		sm.init();
		sm.enter();
		
		// Phone starts ringing
		phone.raiseIncomingCall();

		// Keep the phone ringing for a while:
        for (int i = 0; i < 40; i++) {
            Thread.sleep(200);
            sm.runCycle();
        }
		
		// User accepts call
		user.raiseAcceptCall();
		sm.runCycle();
		
        // Keep the phone conversation busy for a while:
        for (int i = 0; i < 50; i++) {
            Thread.sleep(200);
            sm.runCycle();
        }
		
		// User dismisses call        
		user.raiseDismissCall();
		sm.runCycle();
		
		// Wait until the sm goes to the Idle state
        for (int i = 0; i < 15; i++) {
            Thread.sleep(200);
            sm.runCycle();
        }

        // Let's print out
        System.out.println("Ring Count: " + phone.getCount());
        System.out.println("Duration: " + phone.getDuration());

        // Since it is a non-terminating state machine, let's kill the JVM
        System.exit(0);
	}
}
