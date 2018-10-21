package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	// TODO: add suitable attributes
	private AbstractWashingMachine myMachine;
	private int mode;
	private int direction;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (60 * 1000/speed)); // TODO: replace with suitable period
		myMachine = mach;
		direction = AbstractWashingMachine.SPIN_LEFT;
	}

	public void perform() {
		// TODO: implement this method
		SpinEvent event = (SpinEvent) mailbox.tryFetch();
		if (event != null) {
			mode = event.getMode();
		}
		
		switch (mode) {
		case SpinEvent.SPIN_OFF:
			myMachine.setSpin(AbstractWashingMachine.SPIN_OFF);
			break;
		case SpinEvent.SPIN_SLOW:
			myMachine.setSpin(direction);
			break;
		case SpinEvent.SPIN_FAST:
			if (myMachine.getWaterLevel() > 0) 
				myMachine.setSpin(AbstractWashingMachine.SPIN_OFF);
			else
				myMachine.setSpin(AbstractWashingMachine.SPIN_FAST);
			break;
		default:
			System.out.println("Saving this for debug purposes");
			break;
		}
		
		if (direction == AbstractWashingMachine.SPIN_LEFT)
			direction = AbstractWashingMachine.SPIN_RIGHT;
		else if (direction == AbstractWashingMachine.SPIN_RIGHT)
			direction = AbstractWashingMachine.SPIN_LEFT;
	}
}
