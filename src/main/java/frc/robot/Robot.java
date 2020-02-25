/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick; 
//import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup; 
import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive; 
// import edu.wpi.first.wpilibj.CameraServer;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; 
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.networktables.NetworkTableInstance;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Robot extends TimedRobot {
  private DifferentialDrive drive; 
  private DifferentialDrive shooterleft;
  private DifferentialDrive shooterright;
  private DoubleSolenoid shifter;
  private Joystick joy;
  private static final int axisThrottle = 1; // stick 1, l, y 
  private static final int axisSteer = 0; // stick 1, l, x
  private static final int buttonA = 0; // button a

  // private DoubleSolenoid shifter;

  // public Robot() {
  //     shifter = new DoubleSolenoid(0, 1); // (forward channel, reverse channel)
  // }

  // public void shiftIn() {
  //     shifter.set(DoubleSolenoid.Value.kReverse);
  // }

  // public void shiftOut() {
  //     shifter.set(DoubleSolenoid.Value.kForward);
  // }

  @Override
  public void robotInit() {
    /**
     * SPARK MAX controllers are intialized over CAN by constructing a CANSparkMax
     * object
     * 
     * The CAN ID, which can be configured using the SPARK MAX Client, is passed as
     * the first parameter
     * 
     * The motor type is passed as the second parameter. Motor type can either be:
     * com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless
     * com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushed
     * 
     * The example below initializes four brushless motors with CAN IDs 1 and 2.
     * Change these parameters to match your setup
     */
    CANSparkMax motorrearleft = new CANSparkMax(4, MotorType.kBrushless);
    CANSparkMax motorrearright = new CANSparkMax(2, MotorType.kBrushless);
    CANSparkMax motorfrontright = new CANSparkMax(1, MotorType.kBrushless);
    CANSparkMax motorfrontleft = new CANSparkMax(3, MotorType.kBrushless);

    CANSparkMax motorshooterleft = new CANSparkMax(5, MotorType.kBrushless);
    CANSparkMax motorshooterright = new CANSparkMax(6, MotorType.kBrushless);

    // pnumatic gear shifter
    shifter = new DoubleSolenoid(0, 1);

    drive = new DifferentialDrive(new SpeedControllerGroup(motorrearleft, motorfrontleft), 
        new SpeedControllerGroup(motorrearright, motorfrontright));
    shooterleft = new DifferentialDrive(motorshooterleft, motorshooterleft);
    shooterright = new DifferentialDrive(motorshooterright, motorshooterright);

    joy = new Joystick(0);

    // NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight"); 
    // NetworkTableEntry tx = table.getEntry("tx");
    // NetworkTableEntry ty = table.getEntry("ty");
    // NetworkTableEntry ta = table.getEntry("ta");

    // //read values periodically
    // double x = tx.getDouble(0.0);
    // double y = ty.getDouble(0.0);
    // double area = ta.getDouble(0.0);

    // //post to smart dashboard periodically
    // SmartDashboard.putNumber("LimelightX", x);
    // SmartDashboard.putNumber("LimelightY", y);
    // SmartDashboard.putNumber("LimelightArea", area);
  
  }

  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }

  @Override 
  public void teleopPeriodic() {
    drive.arcadeDrive(joy.getRawAxis(axisThrottle), -joy.getRawAxis(axisSteer));

    shifter.set(joy.getRawButton(buttonA) ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);

   if (joy.getRawButton(buttonA)) {
     shooterleft.arcadeDrive(1, 0);
     shooterright.arcadeDrive(-1, 0);
   } else {
      shooterleft.arcadeDrive(0, 0);
      shooterright.arcadeDrive(0, 0);
    }
  }
}
