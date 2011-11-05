/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.PWM;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class TPARobot extends IterativeRobot {
   
    RobotDrive theRobotDrive;   // Robot Drive Variable
    Joystick theRightStick;     // Right joystick
    Joystick theLeftStick;      // Left joystick
    PWM theRightPWM;            // Right E4P Motion Sensor
    PWM theLeftPWM;             // Left E4P Motion Sensor
    
    // Drive mode selection
    int theDriveMode;                           // The actual drive mode that is currently selected.
    static final int UNINITIALIZED_DRIVE = 0;   // Value when no drive mode is selected
    static final int ARCADE_DRIVE = 1;          // Value when arcade mode is selected 
    static final int TANK_DRIVE = 2;            // Value when tank drive is selected
    
    /*--------------------------------------------------------------------------*/
    /*
     * Author:  Daniel Hughes
     * Date:    10/29/2011 (Daniel Hughes)
     * Purpose: TPARobot Constructor
     * Inputs:  None
     * Outputs: None
     */
    public void TPARobot(){
        
        // Create a robot using standard right/left robot drive on PWMS 1 and 2
        theRobotDrive = new RobotDrive(1,2);
        
        // Define joysticks being used at USB port #1 and USB port #2 on the Drivers Station
	theRightStick = new Joystick(1);
	theLeftStick = new Joystick(2);
        
        // Initialize the Drive Mode to Uninitialized
        theDriveMode = UNINITIALIZED_DRIVE;
        
        // Defines two PWM (E4P Motion Sensor) at ports 3 and 4
        theRightPWM = new PWM(3);
        theLeftPWM = new PWM(4);
    }
    /*--------------------------------------------------------------------------*/
    
    /*
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // Actions which would be performed once (and only once) upon initialization of the
	// robot would be put here.

	System.out.println("RobotInit() completed.\n");
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        // feed the user watchdog at every period when in autonomous
	Watchdog.getInstance().feed();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
        // feed the user watchdog at every period when in autonomous
        Watchdog.getInstance().feed();
        
        setDriveMode();
        if (isNeutral(theRightStick)){  // If no signal
            brake(theLeftPWM.getSpeed(), theRightPWM.getSpeed()); // Brake the Robot
        }
		if (theRightStick.getZ() > 0) { 
		/*This Z-Value (0) is temporary since I was not able to 
		  find/obtain any information on the range of Z-Values of the Z-Axis*/
			SlowSpeedMode(theLeftPWM,theRightPWM);
		}
		if (theRightStick.getZ() == 0) {
		/*This Z-Value (0) is temporary since I was not able to 
		  find/obtain any information on the range of Z-Values of the Z-Axis*/
			MediumSpeedMode(theLeftPWM,theRightPWM);
        }
        
        
    }
    
    /*--------------------------------------------------------------------------*/
    /*
     * Author:  Daniel Hughes
     * Date:    10/29/2011 (Daniel Hughes)
     * Purpose: To determine the appropriate drive mode based on the "Z" wheel on 
     *          the right joystick. If the "Z" wheel is up (negative) Robot is put
     *          in arcade mode. Otherwise, robot is put in tank mode.
     * Inputs:  None
     * Outputs: None
     */    
    public void setDriveMode(){
        
        // determine if tank or arcade mode, based upon position of "Z" wheel on kit joystick
        if (theRightStick.getZ() <= 0) {    // Logitech Attack3 has z-polarity reversed; up is negative
            // use arcade drive
            theRobotDrive.arcadeDrive(theRightStick, false);	// drive with arcade style (use right stick)
            if (theDriveMode != ARCADE_DRIVE) {
                // if newly entered arcade drive, print out a message
                System.out.println("Arcade Drive\n");
                theDriveMode = ARCADE_DRIVE;
            }
        } else {
            // use tank drive
            theRobotDrive.tankDrive(theLeftStick, theRightStick);	// drive with tank style
            if (theDriveMode != TANK_DRIVE) {
                // if newly entered tank drive, print out a message
                System.out.println("Tank Drive\n");
                theDriveMode = TANK_DRIVE;
            }
        }
    }
    /*--------------------------------------------------------------------------*/
    
    /*--------------------------------------------------------------------------*/
    /*
     * Author:  Marissa Beene
     * Date:    10/30/2011 (Marissa Beene)
     * Purpose: To use the motors to brake the robot. Takes the speed from the 
     *          each motor and sends the reverse signal back.
     * Inputs:  Double aSpeedRight - the speed of the right motor
     *          Double aSpeedLeft - the speed of the left motor
     * Outputs: None
     */
    
    public void brake(double aSpeedLeft, double aSpeedRight){
        theRobotDrive.tankDrive(-aSpeedLeft, -aSpeedRight); //drive the robot at opposite values
        }
    /*--------------------------------------------------------------------------*/
    
    
    /*--------------------------------------------------------------------------*/
    
    /*
     * Author:  Marissa Beene
     * Date:    10/30/11
     * Purpose: To determine if there is no signal in arcade mode. If there is no 
     *          signal on the joystick, it will return true, otherwise, it will 
     *          return false.
     * Inputs:  Joystick aStick  - the driving joystick
     * Outputs: Boolean - returns true if the joystick is not sending a signal
     */
    public boolean isNeutral(Joystick aStick){
        if(aStick.getY() == 0 && aStick.getX() == 0){ //there is no input
            return true;
        }
        else{
            return false;
        }
    }
    /*--------------------------------------------------------------------------*/
	
	
	/* ----------------------------------------------------------------------------------------------*/
	/* Author:   Sumbhav Sethia
	 * Date:     11/03/2011 (Sumbhav Sethia)
	 * Function: MediumSpeedMode
	 * Purpose:  To set the max speed in either dircetion 
	 *           to approximately two-thirds the robot's maximum speed.
	 * Inputs:   aPWMLeft, aPWNRight - The PWM machines attached to the motors
     * Outputs:  None
	*/
	 public void MediumSpeedMode (PWM aPWMLeft, PWM aPWMRight) {
		aPWMLeft.setBounds (213,129,128,127,85);
        aPWMRight.setBounds (213,129,128,127,85);		
		/*Sets the max speed, deadband max, center speed(off speed), deadband min, 
		and minimum speed respectively. Apparently, the speed can also be as an
		integer between 0 and 255, with 0 being off, 1-127 being reverse, 
		128 being neutral, and 128-255 being forward. I am not sure about whether 
		my deadband numbers work or not, or whether a deadband is absolutely necessary, 
		but Daniel can help with that. A deadband is basically a point at which no action occurs,
		or there is no voltage.
		Here is the location of the setBounds method:
			Inside FRC Classes Folder, then doc/javadoc/edu/wpi/first/wpilibj/PWM */
	 }
	/*----------------------------------------------------------------------------------------------*/
	 
	/* ----------------------------------------------------------------------------------------------*/
	/* Author:   Sumbhav Sethia
	 * Date:     11/03/2011 (Sumbhav Sethia)
	 * Function: SlowSpeedMode
	 * Purpose:  To set the max speed in either dircetion 
	 *           to approximately one-third the robot's maximum speed.
	 * Inputs:   aPWMLeft, aPWNRight - The PWM machines attached to the motors
	 * Outputs:  None
	 */
	 public void SlowSpeedMode (PWM aPWMLeft, PWM aPWMRight) {
		aPWMLeft.setBounds (171,129,128,127,43);
		aPWMRight.setBounds (171,129,128,127,43);
		/*Sets the max speed, deadband max, center speed(off speed), deadband min, 
		and minimum speed respectively. Apparently, the speed can also be as an
		integer between 0 and 255, with 0 being off, 1-127 being reverse, 
		128 being neutral, and 128-255 being forward. I am not sure about whether 
		my deadband numbers work or not, or whether a deadband is absolutely necessary, 
		but Daniel can help with that. A deadband is basically a point at which no action occurs,
		or there is no voltage.
		Here is the location of the setBounds method:
			Inside FRC Classes Folder, then doc/javadoc/edu/wpi/first/wpilibj/PWM */
	 }
	/*----------------------------------------------------------------------------------------------*/
}
