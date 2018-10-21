package todo;

import done.*;

public class WashingController implements ButtonListener {	
	// TODO: add suitable attributes
	private AbstractWashingMachine theMachine;
	private double theSpeed;
	private TemperatureController temp;
	private SpinController spin;
	private WaterController water;
	private WashingProgram program;
	
	
    public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		// TODO: implement this constructor
    	this.theMachine = theMachine;
    	this.theSpeed = theSpeed;
    	temp = new TemperatureController(theMachine, theSpeed);
    	spin = new SpinController(theMachine, theSpeed);
    	water = new WaterController(theMachine, theSpeed);
    	
    	temp.start();
    	spin.start();
    	water.start();
    	
    	program = new WashingProgram3(theMachine, theSpeed, temp, water, spin);
    	program.terminate();
    }

    public void processButton(int theButton) {
    	System.out.println(theButton);
    	if(!program.isAlive() && theButton != 0) {
    		program = buildProgram(theButton);
    		program.start();
    	} else if (theButton == 0) {
    		program.interrupt();
    	} else {
    		System.out.println("Error in processButton");
    	}
		// TODO: implement this method
    }
    
    public WashingProgram buildProgram(int program) {
    	switch (program) {
		case 1:
			return new WashingProgram1(theMachine, theSpeed, temp, water, spin);
		case 2:
			return new WashingProgram2(theMachine, theSpeed, temp, water, spin);
		case 3:
			return new WashingProgram3(theMachine, theSpeed, temp, water, spin);
		default:
			return null;
		}
    }
}
