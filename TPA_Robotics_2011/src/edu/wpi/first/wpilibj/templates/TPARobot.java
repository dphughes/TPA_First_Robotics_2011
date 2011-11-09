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
     * 11/07/2011 - jd - Added comment to test github merge.
     */
    public void TPARobot(){
        
        // Create a robot using standard right/left robot drive on PWMS 1 and 2
        theRobotDrive = new RobotDrive(1,2);
        
        // Define joysticks being used at USB port #1 and USB port #2 on the Drivers Station
	theRightStick = new Joystick(1);
	theLeftStick = new Joystick(2);
        
        // Initialize the Drive Mode to Uninitialized
        theDriveMode = UNINITIALIZED_DRIVE;
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
    
}
