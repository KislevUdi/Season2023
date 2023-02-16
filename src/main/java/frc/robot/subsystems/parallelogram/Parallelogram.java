package frc.robot.subsystems.parallelogram;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.parallelogram.CalibrateParallelogram;
import frc.robot.commands.parallelogram.GoToAngle;
import frc.robot.commands.parallelogram.GoToHeight;
import frc.robot.commands.parallelogram.ResetCalibrate;

/**
 * Paralellogram subsystem.
 */
public class Parallelogram extends SubsystemBase {

    private TalonFX motor;
    private SimpleMotorFeedforward feedForwardVelocity;
    private ArmFeedforward armFeedForward;
    private DigitalInput magneticDigitalInput;
    private boolean isBrake;
    private PigeonIMU gyro;
    private double offset;

    /**
     * constructs a new parallelogram
     */
    public Parallelogram() {
        SmartDashboard.putNumber("wangle", 90);

        motor = new TalonFX(ParallelConstants.PORT_NUMBER_PARALLEL_MOTOR);
        magneticDigitalInput = new DigitalInput(ParallelConstants.PORT_DIGITAL_INPUT);
        feedForwardVelocity = new SimpleMotorFeedforward(ParallelConstants.KS_VELOCITY, ParallelConstants.KV_VELOCITY);
        armFeedForward = new ArmFeedforward(ParallelConstants.ARM_FEED_FORWARD_KS,
                ParallelConstants.ARM_FEED_FORWARD_KG, ParallelConstants.ARM_FEED_FORWARD_KV);

        motor.setInverted(ParallelConstants.MOTOR_INVERT_TYPE);
        gyro = new PigeonIMU(ParallelConstants.GYRO_PORT_NUMBER);

        motor.config_kP(0, ParallelConstants.KP_POSITION);
        motor.config_kI(0, ParallelConstants.KI_POSITION);
        motor.config_kD(0, ParallelConstants.KD_POSITION);

        isBrake = false;

        resetPosition(90);

        SmartDashboard.putData("Parallelogram/Calibrate Parallelogram",
                new CalibrateParallelogram(this).andThen(new ResetCalibrate(this)));
        // SmartDashboard.putData("set angle", new InstantCommand(() -> {
        // setAngle(SmartDashboard.getNumber("wanted angle", 0));
        // }));
        SmartDashboard.putData("Parallelogram/Go to 90",
                new GoToAngle(this, 90));

        SmartDashboard.putData("Parallelogram/Go to end",
                new GoToAngle(this, 119.17));

        // SmartDashboard.putData("reset 90",
        // new InstantCommand(()-> resetPosition(90)));

        SmartDashboard.putData("Parallelogram/Go to height",
                new GoToHeight(this, ParallelConstants.PARALLEL_LENGTH, true));

        // SmartDashboard.putData("Parallelogram/Go to 90", new InstantCommand(() -> {

        // }));
        SmartDashboard.putData(this);
    }

    /**
     * Sets power to the paralellogram's motor.
     * 
     * @param power Desired power.
     */
    public void setPower(double power) {
        motor.set(ControlMode.PercentOutput, power);
    }

    /**
     * Sets velocity to parallelogram's motor
     * 
     * @param velocity the velocity we want the motor have.
     */
    public void setVelocity(double velocity) {
        motor.set(ControlMode.Velocity, velocity / 10 * ParallelConstants.PULSE_PER_METER,
                DemandType.ArbitraryFeedForward, feedForwardVelocity.calculate(velocity));
    }

    /**
     * Gets the velocity.
     * 
     * @return the velocity.
     */
    public double getVelocity() {
        return motor.getSelectedSensorVelocity() * 10 / ParallelConstants.PULSE_PER_METER;
    }

    /**
     * Sets the position of the arm.
     * 
     * @param angle is where we want the parallelogram to be.
     */
    public void setAngle(double angle) {
        motor.set(ControlMode.Position, angle * ParallelConstants.PULSE_PER_ANGLE, DemandType.ArbitraryFeedForward,
                armFeedForward.calculate(Utils.toRads(angle), 0));
    }

    // TODO: Upon seeing the ArmFeedForward, I think we should try no feed forward
    // first! ~ Noya

    /**
     * Gets the angle of the arm.
     * 
     * @return the arm's angle (after calculating with offset).
     */
    public double getAngle() {
        return gyro.getPitch() - this.offset;
    }

    // public double getOffset(){
    // return gyro.getPitch() - 90 ;
    // }

    /*
     * Resets the offset for the gyro.
     */
    public void resetPositionWithOffset() {
        this.offset = gyro.getPitch() - ParallelConstants.DIGITAL_INPUT_ANGLE;
    }

    /**
     * Sets motor neutral mode to brake.
     */
    public void setBrake() {
        motor.setNeutralMode(NeutralMode.Brake);
        isBrake = true;
    }

    /**
     * Sets motor neutral mode to coast.
     */
    public void setCoast() {
        motor.setNeutralMode(NeutralMode.Coast);
        isBrake = false;
    }

    /**
     * Gets the digital input from the magnet on the parallelogram.
     * 
     * @return whether the arm reached the magnet or not (boolean).
     */
    public boolean getDigitalInput() {
        return !magneticDigitalInput.get();
    }

    /**
     * Sets the current position/angle of the arm as the digital input angle (max
     * angle?).
     */
    public void resetPosition() {
        motor.setSelectedSensorPosition(ParallelConstants.DIGITAL_INPUT_ANGLE * ParallelConstants.PULSE_PER_ANGLE);
    }

    public void resetPosition(double angle) {
        motor.setSelectedSensorPosition(angle * ParallelConstants.PULSE_PER_ANGLE);
    }

    /**
     * checks if the motor's neutral mode is set on brake or coast.
     * 
     * @return the neutral mode of the motor (true = brake).
     */
    public boolean isBrake() {
        return isBrake;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("encoder", motor::getSelectedSensorPosition, null);
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("parallelogram/is Digital Input", getDigitalInput());
        SmartDashboard.putBoolean("parallelogram/is Brake", isBrake);

        SmartDashboard.putNumber("parallelogram/Arm angle", getAngle());
        SmartDashboard.putNumber("parallelogram/pulli velocity", getVelocity());

    }

}
