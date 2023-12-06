// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.xrp.XRPMotor;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  //Instead of using the CTRE motor controller objects, we are going to use the XRP ones.
  //private final XRPDrivetrain m_drivetrain = new XRPDrivetrain();

  // Instatiate your robot variables here.@
  // Motors
  private XRPMotor leftMotor = new XRPMotor(0);
  private XRPMotor rightMotor = new XRPMotor(1);

  // The XRP has onboard encoders that are hardcoded
  // to use DIO pins 4/5 and 6/7 for the left and right
  private final Encoder leftDriveEncoder = new Encoder(4, 5);
  private final Encoder rightDriveEncoder = new Encoder(6, 7);


  private XRPMotor armMotor = new XRPMotor(2);
  // The Motor 3 and 4 Encoders are on DIO 8,9 and 10,11 respectively.
  private final Encoder armEncoder = new Encoder(8,9);
  //;private final Encoder m_rightEncoder = new Encoder(6, 7);


  private XRPMotor rollerMotor = new XRPMotor(3);

  // There are no compressors or Pneumatics on the XRP
  // Drivetrain
  private DifferentialDrive drive = new DifferentialDrive(leftMotor,rightMotor);
  
  // Joysticks
  private PS4Controller driverJoystick = new PS4Controller(0);

  // unit conversion
  // These constants can be found in the XRP Drivetrain Class that comes in the WPILib Example Project
  /*
   * private static final double kGearRatio =
      (30.0 / 14.0) * (28.0 / 16.0) * (36.0 / 9.0) * (26.0 / 8.0); // 48.75:1
  private static final double kCountsPerMotorShaftRev = 12.0;
  private static final double kCountsPerRevolution = kCountsPerMotorShaftRev * kGearRatio; // 585.0
  private static final double kWheelDiameterInch = 2.3622; // 60 mm

  They use some built-in convenience features to get ticks to inches, feet, or meters.
   */

  // Here is the formula they use    = gearRatio / ticks per revolution * wheel size * Pi / inches per foot
  // private final double kDriveTick2Feet = 1.0 / 4096 * 6 * Math.PI / 12;
  // So our formula is...
  private double kDriveTick2Feet = 585 * 2.3622 * Math.PI /12;
  private final double kArmTick2Deg = 360.0 / 512 * 26 / 42 * 18 / 60 * 18 / 84;


  private double startTime;


  /**
   *  function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Arm Encoder Value", armEncoder.get() * kArmTick2Deg);
    SmartDashboard.putNumber("Left Drive Encoder Value", leftDriveEncoder.get() * kDriveTick2Feet);
    SmartDashboard.putNumber("Right Drive Encoder Value", rightDriveEncoder.get() * kDriveTick2Feet);
  }

  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void autonomousPeriodic() {
    double time = Timer.getFPGATimestamp();
    System.out.println(time- startTime);

    if (time - startTime < 3){
      leftMotor.set(0.6);
      rightMotor.set(-0.6);
      //m_drivetrain.arcadeDrive(.6, 0);
    } else {
      leftMotor.set(0);
      rightMotor.set(0);
      //m_drivetrain.arcadeDrive(0, 0);
    }
  }

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    double speed = -driverJoystick.getRawAxis(1); //With the XRP, we can turn it up to 11 without hurting anything.
    double turn = driverJoystick.getRawAxis(0);

    double left = speed + turn;
    double right = speed - turn;
    //m_drivetrain.arcadeDrive(speed,turn);
    leftMotor.set(left);
    rightMotor.set(-right);

    //Intake
    rollerMotor.set(driverJoystick.getR2Axis());
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}

