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
import edu.wpi.first.wpilibj.Jaguar;

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
    Jaguar theArmMotor;                         // The Motor Controlling the Arm
    static final boolean DEBUG = false;         // Debug Trigger
    static final double STOP_VALUE = 0.1;       // Value drive motors are sent when stopping
    static final double ARM_SPEED = 0.5;        // Value arm motor is sent
    
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
        
    }
    /*--------------------------------------------------------------------------*/
    
    /*
     * Author:  Daniel Hughes
     * Date:    11/12/2011
     * Purpose: Robot Initialization Function. This function is run once when the
     *          robot is first started up and should be used for any initialization
     *          code.
     * Inputs:  None
     * Outputs: None
     */

    public void robotInit() {
        // Create a drive system using standard right/left robot drive on PWMS 1 and 2
        theRobotDrive = new RobotDrive(1,2);
        if (DEBUG == true){
            System.out.println("TheRobotDrive constructed successfully");
        }
        
        // Define joysticks being used at USB port #1 and USB port #2 on the Drivers Station
	theRightStick = new Joystick(1);
	theLeftStick = new Joystick(2);
        if (DEBUG == true){
           System.out.println("The Joysticks constructed successfully"); 
        }
        
        // Initialize the Drive Mode to Uninitialized
        theDriveMode = UNINITIALIZED_DRIVE;
        
        // Defines two E4P Motion Sensors at ports 1,2,3, and 4
        theLeftEncoder = new Encoder(1,2);
        theRightEncoder = new Encoder(3,4);
        if (DEBUG == true){
            System.out.println("The Encoders constructed successfully");
        }
        
        // Initialize a Motor connected to a Jaguar Speed Controller at port 5
        theArmMotor = new Jaguar(5);
        if (DEBUG == true){
            System.out.println("The arm motor constructed successfully");
        }

        if (DEBUG == true){
	System.out.println("RobotInit() completed.\n");
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        // feed the user watchdog at every period when in autonomous
	Watchdog.getInstance().feed();
    }

    /*
     * Author:  Team
     * Date:    11/12/2011
     * Purpose: This function is called periodically during operator control. Allows
     *          for teleoperated control of the robot.
     * Inputs:  None
     * Outputs: None
     */
    public void teleopPeriodic() {
        
        // Feed the user watchdog at every period when in autonomous
        Watchdog.getInstance().feed();
        if (DEBUG == true){
            System.out.println("Teleop Periodic Watchdog Fed");
        }
        
        // Determine whether arcade drive or tank drive is in use
        setDriveMode();
        if (DEBUG == true){
            System.out.println("setDriveMode called");
        }
        
        // Brake the robot when no signal is sent
        brakeOnNeutral();
        if (DEBUG == true){
            System.out.println("brakeOnNeutral called");
        }
        
        // Move the Arm
        moveArm();
        if (DEBUG == true){
        System.out.println("moveArm called");
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
            if (DEBUG == true){
                System.out.println("theRightStick.getZ called" );
            }
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
     * Purpose: To determine if there is no signal. First determines the drive
     *          mode and discards the left stick if in arcade mode. If there is 
     *          no signal to the drive train, it will return true, otherwise it 
     *          will return false
     * Inputs:  Joystick aRightStick  - the right joystick
     *          Joystick aLeftStick - the left joystick
     * Outputs: Boolean - returns true if the drive train is not sent a signal
     */
    
    public boolean isNeutral(Joystick aRightStick, Joystick aLeftStick){
        if (DEBUG == true){
            System.out.println("isNeutral Called");
        }
        if(theDriveMode == ARCADE_DRIVE){ //if arcade drive
            if (DEBUG == true){
                System.out.println("Arcade Drive Recognized by isNeutral");
            }
            if(aRightStick.getY() == 0 && aRightStick.getX() == 0){ //there is no input
                return true;
            }
            else{
                return false;
            }
        }
        else if(theDriveMode == TANK_DRIVE){ //if tank drive
            if (DEBUG == true){
                System.out.println("Tank Drive Recognized by isNeutral");
            }
            if(aRightStick.getY() == 0 && aLeftStick.getY() == 0){
                return true;
            }
            else{
                return false;
            }
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
        
        double theLeftSpeedOutput = 0; // value the left motor will be sent
        double theRightSpeedOutput = 0; // value the right motor will be sent
        
        if (DEBUG == true){
            System.out.println("brakeOnNeutral called");
        }
        
        if(isNeutral(theRightStick, theLeftStick)){ // if no signal is sent to the robot
            
            // get the direction of the left motor and store the stop value vector to theLeftSpeedOutput
            if(!theLeftEncoder.getStopped()){
                if(theLeftEncoder.getDirection()){
                    theLeftSpeedOutput = STOP_VALUE;
                }
                else{
                    theLeftSpeedOutput = -STOP_VALUE;
                }
            }
            
            // get the direction of the right motor and store a stop value vector to theRightSpeedOutput
            if(!theRightEncoder.getStopped()){
                if(theRightEncoder.getDirection()){
                    theRightSpeedOutput = STOP_VALUE;
                }
                else{
                    theRightSpeedOutput = -STOP_VALUE;
                }
            }
        // brake the robot at the value of the stop value
        brake(theLeftSpeedOutput, theRightSpeedOutput);
        }
    }
    
    /*--------------------------------------------------------------------------*/
    
    /*--------------------------------------------------------------------------*/
   
    /*
     * Author:  Marissa Beene
     * Date:    11/19/11
     * Purpose: To move the arm with a motor. When the joystick is pushed forward, 
     *          the motor will run at ARM_SPEED. When the joystick is pulled backward,
     *          the motor will run in the other direction. If tank drive is currently
     *          in use, the arm will be deactivated.
     * Inputs:  None
     * Outputs: None
     */
    
    public void moveArm (){
        // Do not use arm if both joysticks used on drive system
        if(theDriveMode == TANK_DRIVE){
            theArmMotor.set(0.0);
        }
        if (theDriveMode != TANK_DRIVE){
            // if joystick pushed forward, run the motor forward
            if (theLeftStick.getY() > 0){ 
                theArmMotor.set (ARM_SPEED);
            }
            
            // if joystick pushed backward, run the motor backward
            if (theLeftStick.getY() < 0){ 
                theArmMotor.set (-ARM_SPEED);
            }
            
            // if joystick not moved, don't run motor
            if (theLeftStick.getY() == 0){
                theArmMotor.set(0.0);
            }
        }
    }
    /*--------------------------------------------------------------------------*/
    
}

