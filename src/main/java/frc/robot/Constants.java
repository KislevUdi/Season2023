// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.utils.Rectangle;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final double JOYSTICK_DEADBAND = 0.1; // the deadband for the joysticks
    public static final double JOYSTICK_ANGLE_DEADBAND = 0.2; // the deadband for the angle of the joysticks
    public static final double JOYSTICK_IDLE_DEADBAND = 0.3; // the deadband to check if the joystick is idle

    public static final Rectangle RAMP = new Rectangle(2.91, 1.51, 4.85, 3.98); // in meters, blue alliance
    public static final Rectangle OPEN_AREA = new Rectangle(4.85, 0.0, 11.69, 8.02); // in meters, blue alliance
    public static final Rectangle ENTRANCE_BOTTOM = new Rectangle(2.91, 0.0, 4.85, 1.51); // in meters, blue alliance
    public static final Rectangle ENTRANCE_TOP = new Rectangle(2.91, 3.98, 4.85, 5.49); // in meters, blue alliance
    public static final Rectangle COMMUNITY_BOTTOM = new Rectangle(0.0, 0.0, 2.91, 1.51); // in meters, blue alliance
    public static final Rectangle COMMUNITY_TOP = new Rectangle(0.0, 3.98, 2.91, 5.49); // in meters, blue alliance
    public static final Rectangle COMMUNITY_MIDDLE = new Rectangle(0, 1.51, 2.91, 3.98); // in meters, blue alliance
    public static final Rectangle LOADING_ZONE = new Rectangle(11.69, 5.55, 16.54, 8.02); // in meters, blue alliance

    public static final double FIELD_WIDTH = 16.54; // in meters
    public static final double FIELD_HEIGHT = 8.02; // in meters

    /**
     * The Vision constants.
     */
    public static final class VisionConstants {
        public static final NetworkTable LIMELIGHT_TABLE = NetworkTableInstance.getDefault().getTable("limelight");

        public static final NetworkTableEntry HAS_TARGET_ENTRY = LIMELIGHT_TABLE.getEntry("tv"); // double not boolean
        
        /**
         * An array of doubles with the following values:
         * [0] - meters from the middle of the field in the x axis
         * [1] - meters from the middle of the field in the y axis
         * [2] - meters from the the field carpet in the z axis
         * [3] - roll in degrees
         * [4] - pitch in degrees
         * [5] - yaw in degrees
         */
        public static final NetworkTableEntry ROBOT_POSE_ENTRY = LIMELIGHT_TABLE.getEntry("botpose");
        public static final NetworkTableEntry LIMELIGHT_LATENCY_ENTRY = LIMELIGHT_TABLE.getEntry("tl"); // in ms
        public static final double CAPTURE_LATENCY = 11; // ms
    }
}
