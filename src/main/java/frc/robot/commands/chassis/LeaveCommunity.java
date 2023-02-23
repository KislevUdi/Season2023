// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.chassis;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.chassis.Chassis;
import frc.robot.subsystems.chassis.utils.ChassisUtils.Zone;
import frc.robot.subsystems.chassis.utils.TrajectoryGenerator;

public class LeaveCommunity extends CommandBase {
    private final Chassis chassis;
    private Command command;

    public static enum TopOrBottom {
        TOP, BOTTOM;
    }

    private TopOrBottom topOrBottom;

    /** Creates a new LeaveCommunity. */
    public LeaveCommunity(Chassis chassis, TopOrBottom topOrBottom) {
        this.chassis = chassis;
        this.topOrBottom = topOrBottom;

        addRequirements(chassis);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        command = new InstantCommand();
        TrajectoryGenerator generator = new TrajectoryGenerator(Alliance.Blue);

        Zone zone = Zone.fromRobotLocation(chassis.getPose().getTranslation());
        if (topOrBottom == TopOrBottom.TOP) {
            switch (zone) {
                case COMMUNITY_MIDDLE:
                case COMMUNITY_BOTTOM:
                    generator.add(new Pose2d(new Translation2d(2.06, 4.89), Rotation2d.fromDegrees(180)),
                            Rotation2d.fromDegrees(0));
                case COMMUNITY_TOP:
                    generator.add(new Pose2d(new Translation2d(5.6, 4.89), Rotation2d.fromDegrees(180)),
                            Rotation2d.fromDegrees(0));
                default:
                    break;
            }
        } else {
            switch (zone) {
                case COMMUNITY_MIDDLE:
                case COMMUNITY_TOP:
                    generator.add(new Pose2d(new Translation2d(2.5, 0.7), Rotation2d.fromDegrees(180)),
                            Rotation2d.fromDegrees(0));
                case COMMUNITY_BOTTOM:
                    generator.add(new Pose2d(new Translation2d(5.6, 0.7), Rotation2d.fromDegrees(180)),
                            Rotation2d.fromDegrees(0));
                default:
                    break;
            }
        }
        command = chassis.createPathFollowingCommand(false, generator.generate(chassis.getPose()));
        command.initialize();
    }

    @Override
    public void execute() {
        command.execute();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        command.end(interrupted);
        chassis.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return command.isFinished();
    }
}