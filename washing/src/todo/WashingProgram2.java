package todo;

import done.AbstractWashingMachine;

public class WashingProgram2 extends WashingProgram {

	protected WashingProgram2(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void wash() throws InterruptedException {
		// Program 2 White wash: 
		
		// Lock the hatch, 
		myMachine.setLock(true);
		
		//Like program 1, but with a 15 minute pre-wash in 40◦C.
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, WaterController.LEVEL_HALF));
		// wait til filled
		mailbox.doFetch();
		// stop filling
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, WaterController.LEVEL_EMPTY));
		
		// start spining
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		
		// heat to 40◦C, 
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, TemperatureController.WHITE_TEMP_INITIAL));
		// wait 'til heated
		mailbox.doFetch();
		
		//keep the temperature for 30 minutes, 
		sleep((long) (15 * 60 * 1000 / mySpeed));
		
		// kill heating before rinse
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, TemperatureController.IDLE_TEMP));
		
		// drain, 
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, WaterController.LEVEL_EMPTY));
		// wait til empty
		mailbox.doFetch();
		
		// The main wash should be performed in 90◦C.
		
		// let water into the machine,
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, WaterController.LEVEL_HALF));
		// wait til filled
		mailbox.doFetch();
		// stop filling
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, WaterController.LEVEL_EMPTY));
		
		// start spining
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		
		// heat to 60◦C, 
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, TemperatureController.WHITE_TEMP));
		// wait 'til heated
		mailbox.doFetch();
		
		//keep the temperature for 30 minutes, 
		sleep((long) (30 * 60 * 1000 / mySpeed));
		
		// kill heating before rinse
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, TemperatureController.IDLE_TEMP));
		
		// drain, 
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, WaterController.LEVEL_EMPTY));
		// wait til empty
		mailbox.doFetch();

		// rinse 5 times 2 minutes in cold water, 
		for (int i = 0; i < 5; i++) {
			// fill
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, WaterController.LEVEL_HALF));
			
			// wait 'til filled
			mailbox.doFetch();
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, WaterController.LEVEL_EMPTY));
			
			// rinse for 2 min
			sleep((long) (2 * 60 * 1000 / mySpeed));
			
			// empty
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, WaterController.LEVEL_EMPTY));
			
			// wait 'til empty
			mailbox.doFetch();
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, WaterController.LEVEL_EMPTY));
		}
		
		// centrifuge for 5 minutes
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));
		sleep((long) (5 * 60 * 1000 / mySpeed));
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		
		// and unlock the hatch.
		myMachine.setLock(false);

	}

}
