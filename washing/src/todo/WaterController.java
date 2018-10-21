package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
	// TODO: add suitable attributes
	public static double LEVEL_FULL 	= 1;
	public static double LEVEL_HALF 	= 0.5;
	public static double LEVEL_EMPTY 	= 0;
	
	private AbstractWashingMachine myMachine;
	private int mode = WaterEvent.WATER_IDLE;
	private double targetLevel;
	private RTThread source;
	private boolean acked;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); 
		// TODO: replace with suitable period
		myMachine = mach;
	}

	public void perform() {
		// TODO: implement this method
		WaterEvent event = (WaterEvent) mailbox.tryFetch();
		if (event != null) {
			mode = event.getMode();
			targetLevel = event.getLevel();
			source = (RTThread) event.getSource();
			acked = mode == WaterEvent.WATER_IDLE;
		}
		
		double currentLevel = myMachine.getWaterLevel();
		
		switch (mode) {
		case WaterEvent.WATER_IDLE:
			myMachine.setDrain(false);
			myMachine.setFill(false);
			break;
		case WaterEvent.WATER_DRAIN:
			myMachine.setDrain(true);
			myMachine.setFill(false);
			if (currentLevel == WaterController.LEVEL_EMPTY && !acked) {
				source.putEvent(new AckEvent(this));
				acked = true;
				System.out.println("Water sent ack: drained");
			}
			break;
		case WaterEvent.WATER_FILL:
			myMachine.setDrain(false);
			if (currentLevel < targetLevel)
				myMachine.setFill(true);
			else {
				myMachine.setFill(false);
				if (!acked) {
					source.putEvent(new AckEvent(this));
					acked = true;
					System.out.println("Water sent ack: filled");
				}
			}
			break;
		default:
			System.out.println("Saved for debug purposes");
			break;
		}
	}
}
