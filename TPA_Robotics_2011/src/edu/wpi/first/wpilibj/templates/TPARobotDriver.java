/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author Gennaro
 */
public class TPARobotDriver extends RobotDrive {
    
    public static double kDefaultMinOutput=0.1; //Min value for the Speed Multiplier
    public static double kDefaultMaxOutput=1;   //Max value for the Speed Multiplier
    
    //Constructors:
    TPARobotDriver(final int leftMotorChannel, final int rightMotorChannel){
        super(leftMotorChannel, rightMotorChannel);
    }
    
    TPARobotDriver(final int frontLeftMotor, final int rearLeftMotor,
                   final int frontRightMotor, final int rearRightMotor) {
        super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    }
    
    TPARobotDriver(SpeedController leftMotor, SpeedController rightMotor) {
        super(leftMotor, rightMotor);
    }
    
    TPARobotDriver(SpeedController frontLeftMotor, SpeedController rearLeftMotor,
                   SpeedController frontRightMotor, SpeedController rearRightMotor) {
        super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    }
    /*--------------------------------------------------------------------------*/
    
    /*
     * Author:  Gennaro De Luca
     * Date:    11/26/11 (Gennaro De Luca)
     * Purpose: Tests the input value to make sure that it is a safe value, then
     *          sets that multiplier.
     * Inputs:  double aMaxSpeed - wanted value for the speed multiplier 
     * Outputs: None
     */
    
    public void setMaxSpeed(double aMaxSpeed) {
        
        if(aMaxSpeed<kDefaultMinOutput)  {      //Tests to see if value is less than min allowed
            aMaxSpeed=kDefaultMinOutput;
        }
        else if(aMaxSpeed>kDefaultMaxOutput) {  //Tests to see if value is more than max allowed
            aMaxSpeed=kDefaultMaxOutput;
        }
        m_maxOutput=aMaxSpeed;                  //sets the multiplier to be the tested value
    }
   /*--------------------------------------------------------------------------*/   
}
