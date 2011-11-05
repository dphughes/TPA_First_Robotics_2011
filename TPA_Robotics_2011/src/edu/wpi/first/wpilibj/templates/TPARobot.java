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
import edu.wpi.first.wpilibj.Encoder;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class TPARobot extends IterativeRobot {
   
    RobotDrive theRobotDrive;                   // Robot Drive Variable
    Joystick theRightStick;                     // Right joystick
    Joystick theLeftStick;                      // Left joystick
    Encoder theRightEncoder;                    // Right E4P Motion Sensor
    Encoder theLeftEncoder;                     // Left E4P Motion Sensor
    static final double STOP_VALUE = 0.1;      // The max of the range recognized as zero
    
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
        
        // Defines two E4P Motion Sensors at ports 1,2,3,4
        theLeftEncoder = new Encoder(1,2);
        theRightEncoder = new Encoder(3,4);
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
        brakeOnNeutral();
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
    
    
    /*--------------------------------------------------------------------------*/
    
    /*
     * Author:  Marissa Beene
     * Date:    11/5/11
     * Purpose: To brake the robot if there is no signal in arcade mode. If the 
     *          wheel is not considered stopped, it will read the direction that the wheel
     *          is turning and send it the stop value in the other direction.
     * Inputs:  None
     * Outputs: None
     */
    
    public void brakeOnNeutral(){
        double theLeftSpeedOutput = 0;
        double theRightSpeedOutput = 0;
        if(isNeutral(theRightStick)){
            if(!theLeftEncoder.getStopped()){
                if(theLeftEncoder.getDirection()){
                    theLeftSpeedOutput = STOP_VALUE;
                }
                else{
                    theLeftSpeedOutput = -STOP_VALUE;
                }
            }
            if(!theRightEncoder.getStopped()){
                if(theRightEncoder.getDirection()){
                    theRightSpeedOutput = STOP_VALUE;
                }
                else{
                    theRightSpeedOutput = -STOP_VALUE;
                }
            }
        brake(theLeftSpeedOutput, theRightSpeedOutput);
        }
    }
    
    /*--------------------------------------------------------------------------*/
}
