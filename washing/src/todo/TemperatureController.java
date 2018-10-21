package todo;


import se.lth.cs.realtime.*;

import org.omg.CORBA.PRIVATE_MEMBER;

import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	// TODO: add suitable attributes
	public static int IDLE_TEMP 			= 0;
	public static int WHITE_TEMP_INITIAL 	= 40;
	public static int COLOR_TEMP 			= 60;
	public static int WHITE_TEMP 			= 90;
	
	private AbstractWashingMachine myMachine;
	private int mode = TemperatureEvent.TEMP_IDLE;
	private double targetTemp;
	private RTThread source;
	private boolean acked;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (10000/speed)); // TODO: replace with suitable period
		myMachine = mach;
	}

	public void perform() {
		// TODO: implement this method
		TemperatureEvent event = (TemperatureEvent) mailbox.tryFetch();
		if (event != null) {
			mode = event.getMode();
			targetTemp = event.getTemperature();
			source = (RTThread) event.getSource();
			acked = mode == TemperatureEvent.TEMP_IDLE;
		}
		
		switch (mode) {
		case TemperatureEvent.TEMP_IDLE:
			myMachine.setHeating(false);
			break;
		case TemperatureEvent.TEMP_SET:
			double currentTemp = myMachine.getTemperature();
			if (currentTemp >= targetTemp - 0.7) {
				myMachine.setHeating(false);
				if (!acked) {
					source.putEvent(new AckEvent(this));
					acked = true;
					System.out.println("Temp sent ack");
				}
			} else if (currentTemp <= targetTemp - 1.6 && myMachine.getWaterLevel() > WaterController.LEVEL_EMPTY) {
				myMachine.setHeating(true);
			}
			break;
		default:
			System.out.println("Saving this for debug purposes");
			break;
		}
	}
}
