package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotor;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class Drive extends SDKSubsystem {
    public static final Drive INSTANCE = new Drive();
    private Drive() {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    private final Cell<CachingDcMotor> leftFront = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotor.class, Constants.Drive.leftFront)));
    private final Cell<CachingDcMotor> leftBack = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotor.class, Constants.Drive.leftBack)));
    private final Cell<CachingDcMotor> rightFront = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotor.class, Constants.Drive.rightFront)));
    private final Cell<CachingDcMotor> rightBack = subsystemCell(() -> new CachingDcMotor(getHardwareMap().get(DcMotor.class, Constants.Drive.rightBack)));

    public ElapsedTime driverTimer = new ElapsedTime();
    private double workingOffset = Constants.Drive.imuOffsetDegrees;

    private double botHeading = 0;

    // Retrieve the IMU from the hardware map
    final Cell<IMU> imu = subsystemCell(() -> getHardwareMap().get(IMU.class, "imu"));
    // Adjust the orientation parameters to match your robot
    IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
            RevHubOrientationOnRobot.UsbFacingDirection.UP));

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        leftFront.get().setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.get().setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        imu.get().initialize(parameters);
    }

    public Lambda driveCommand(boolean isFieldCentric, boolean normalizeVelocities) {
        BoundGamepad gamepad1 = Mercurial.gamepad1();
        return new Lambda("mecanum-drive-robot-centric")
                .setInit(() -> {})
                .setExecute(() -> {
                    double y = gamepad1.leftStickY().state();
                    double x = gamepad1.leftStickX().state();
                    double rx = gamepad1.rightStickX().state();

                    double[] normalizedVelocities = normalizeVelocities(y,x,rx);

                    botHeading = imu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + Math.toRadians(workingOffset);

                    double cosHeading = Math.cos(-botHeading);
                    double sinheading = Math.sin(-botHeading);

                    double rotX = x * cosHeading - y * sinheading;
                    double rotY = x * cosHeading + y * sinheading;

                    double nrotX = x * cosHeading - y * sinheading;
                    double nrotY = x * cosHeading + y * sinheading;
                    rotX *= 1.1;

                    if (normalizeVelocities) {
                        if (isFieldCentric) {
                            double denominator3 = Math.max(Math.abs(nrotY) + Math.abs(nrotX) + Math.abs(normalizedVelocities[2]), 1);
                            double frontLeftPowerFieldNormalized = (nrotY + nrotX + normalizedVelocities[2]) / denominator3;
                            double backLeftPowerFieldNormalized = (nrotY - nrotX + normalizedVelocities[2]) / denominator3;
                            double frontRightPowerFieldNormalized = (nrotY - nrotX - normalizedVelocities[2]) / denominator3;
                            double backRightPowerFieldNormalized = (nrotY + nrotX - normalizedVelocities[2]) / denominator3;
                            leftFront.get().setPower(frontLeftPowerFieldNormalized);
                            leftBack.get().setPower(backLeftPowerFieldNormalized);
                            rightFront.get().setPower(frontRightPowerFieldNormalized);
                            rightBack.get().setPower(backRightPowerFieldNormalized);
                        } else {
                            double denominator4 = Math.max(Math.abs(normalizedVelocities[0]) + Math.abs(normalizedVelocities[1]) + Math.abs(normalizedVelocities[2]), 1);
                            double frontLeftPowerNonFieldNormalized = (normalizedVelocities[0] + normalizedVelocities[1] + normalizedVelocities[2]) / denominator4;
                            double backLeftPowerNonFieldNormalized = (normalizedVelocities[0] - normalizedVelocities[1] + normalizedVelocities[2]) / denominator4;
                            double frontRightPowerNonFieldNormalized = (normalizedVelocities[0] - normalizedVelocities[1] - normalizedVelocities[2]) / denominator4;
                            double backRightPowerNonFieldNormalized = (normalizedVelocities[0] + normalizedVelocities[1] - normalizedVelocities[2]) / denominator4;
                            leftFront.get().setPower(frontLeftPowerNonFieldNormalized);
                            leftBack.get().setPower(backLeftPowerNonFieldNormalized);
                            rightFront.get().setPower(frontRightPowerNonFieldNormalized);
                            rightBack.get().setPower(backRightPowerNonFieldNormalized);
                        }
                    } else {
                        if (isFieldCentric) {
                            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
                            double frontLeftPowerFieldOriented = (rotY + rotX + rx) / denominator;
                            double backLeftPowerFieldOriented = (rotY - rotX + rx) / denominator;
                            double frontRightPowerFieldOriented = (rotY - rotX - rx) / denominator;
                            double backRightPowerFieldOriented = (rotY + rotX - rx) / denominator;
                            leftFront.get().setPower(frontLeftPowerFieldOriented / 3);
                            leftBack.get().setPower(backLeftPowerFieldOriented / 3);
                            rightFront.get().setPower(frontRightPowerFieldOriented / 3);
                            rightBack.get().setPower(backRightPowerFieldOriented / 3);
                        } else {
                            double denominator2 = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                            double frontLeftPowerNonField = (y + x + rx) / denominator2;
                            double backLeftPowerNonField = (y - x + rx) / denominator2;
                            double frontRightPowerNonField = (y - x - rx) / denominator2;
                            double backRightPowerNonField = (y + x - rx) / denominator2;
                            leftFront.get().setPower(frontLeftPowerNonField / 3);
                            leftBack.get().setPower(backLeftPowerNonField / 3);
                            rightFront.get().setPower(frontRightPowerNonField / 3);
                            rightBack.get().setPower(backRightPowerNonField / 3);
                        }
                    }
                })
                .setFinish(() -> false);
    }

    public Lambda slowDriveCommand(boolean isFieldCentric, boolean normalizeVelocities) {
        BoundGamepad gamepad1 = Mercurial.gamepad1();
        return new Lambda("mecanum-drive-robot-centric")
                .setInit(() -> {})
                .setExecute(() -> {
                    double y = gamepad1.leftStickY().state();
                    double x = gamepad1.leftStickX().state();
                    double rx = gamepad1.rightStickX().state();

                    double[] normalizedVelocities = normalizeVelocities(y,x,rx);



                    botHeading = imu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + Math.toRadians(Constants.Drive.imuOffsetDegrees);

                    // Rotate the movement direction counter to the bot's rotation

                    double cosHeading = Math.cos(-botHeading);
                    double sinheading = Math.sin(-botHeading);

                    double rotX = x * cosHeading - y * sinheading;
                    double rotY = x * cosHeading + y * sinheading;

                    double nrotX = x * cosHeading - y * sinheading;
                    double nrotY = x * cosHeading + y * sinheading;

                    rotX *= 1.1;

                    if (normalizeVelocities) {
                        if (isFieldCentric) {
                            double denominator3 = Math.max(Math.abs(nrotY) + Math.abs(nrotX) + Math.abs(normalizedVelocities[2]), 1);
                            double frontLeftPowerFieldNormalized = (nrotY + nrotX + normalizedVelocities[2]) / denominator3;
                            double backLeftPowerFieldNormalized = (nrotY - nrotX + normalizedVelocities[2]) / denominator3;
                            double frontRightPowerFieldNormalized = (nrotY - nrotX - normalizedVelocities[2]) / denominator3;
                            double backRightPowerFieldNormalized = (nrotY + nrotX - normalizedVelocities[2]) / denominator3;
                            leftFront.get().setPower(frontLeftPowerFieldNormalized/3);
                            leftBack.get().setPower(backLeftPowerFieldNormalized/3);
                            rightFront.get().setPower(frontRightPowerFieldNormalized/3);
                            rightBack.get().setPower(backRightPowerFieldNormalized/3);
                        } else {
                            double denominator4 = Math.max(Math.abs(normalizedVelocities[0]) + Math.abs(normalizedVelocities[1]) + Math.abs(normalizedVelocities[2]), 1);
                            double frontLeftPowerNonFieldNormalized = (normalizedVelocities[0] + normalizedVelocities[1] + normalizedVelocities[2]) / denominator4;
                            double backLeftPowerNonFieldNormalized = (normalizedVelocities[0] - normalizedVelocities[1] + normalizedVelocities[2]) / denominator4;
                            double frontRightPowerNonFieldNormalized = (normalizedVelocities[0] - normalizedVelocities[1] - normalizedVelocities[2]) / denominator4;
                            double backRightPowerNonFieldNormalized = (normalizedVelocities[0] + normalizedVelocities[1] - normalizedVelocities[2]) / denominator4;
                            leftFront.get().setPower(frontLeftPowerNonFieldNormalized/3);
                            leftBack.get().setPower(backLeftPowerNonFieldNormalized/3);
                            rightFront.get().setPower(frontRightPowerNonFieldNormalized/3);
                            rightBack.get().setPower(backRightPowerNonFieldNormalized/3);
                        }
                    } else {
                        if (isFieldCentric) {
                            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
                            double frontLeftPowerFieldOriented = (rotY + rotX + rx) / denominator;
                            double backLeftPowerFieldOriented = (rotY - rotX + rx) / denominator;
                            double frontRightPowerFieldOriented = (rotY - rotX - rx) / denominator;
                            double backRightPowerFieldOriented = (rotY + rotX - rx) / denominator;
                            leftFront.get().setPower(frontLeftPowerFieldOriented / 3);
                            leftBack.get().setPower(backLeftPowerFieldOriented / 3);
                            rightFront.get().setPower(frontRightPowerFieldOriented / 3);
                            rightBack.get().setPower(backRightPowerFieldOriented / 3);
                        } else {
                            double denominator2 = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                            double frontLeftPowerNonField = (y + x + rx) / denominator2;
                            double backLeftPowerNonField = (y - x + rx) / denominator2;
                            double frontRightPowerNonField = (y - x - rx) / denominator2;
                            double backRightPowerNonField = (y + x - rx) / denominator2;
                            leftFront.get().setPower(frontLeftPowerNonField / 3);
                            leftBack.get().setPower(backLeftPowerNonField / 3);
                            rightFront.get().setPower(frontRightPowerNonField / 3);
                            rightBack.get().setPower(backRightPowerNonField / 3);
                        }
                    }

                })
                .setFinish(() -> false);
    }


    public Lambda timedDriveCommand(long time, double x, double y, double rx, boolean isFieldCentric, boolean normalizeVelocities) {
        BoundGamepad gamepad1 = Mercurial.gamepad1();
        return new Lambda("timed-mecanum-drive-robot-centric")
                .setInit(() -> {
                    this.driverTimer.reset();
                })
                .setExecute(() -> {


                    double[] normalizedVelocities = normalizeVelocities(y,x,rx);

                    botHeading = imu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + Math.toRadians(workingOffset);

                    double cosHeading = Math.cos(-botHeading);
                    double sinheading = Math.sin(-botHeading);

                    double rotX = x * cosHeading - y * sinheading;
                    double rotY = x * cosHeading + y * sinheading;

                    double nrotX = x * cosHeading - y * sinheading;
                    double nrotY = x * cosHeading + y * sinheading;
                    rotX *= 1.1;

                    if (normalizeVelocities) {
                        if (isFieldCentric) {
                            double denominator3 = Math.max(Math.abs(nrotY) + Math.abs(nrotX) + Math.abs(normalizedVelocities[2]), 1);
                            double frontLeftPowerFieldNormalized = (nrotY + nrotX + normalizedVelocities[2]) / denominator3;
                            double backLeftPowerFieldNormalized = (nrotY - nrotX + normalizedVelocities[2]) / denominator3;
                            double frontRightPowerFieldNormalized = (nrotY - nrotX - normalizedVelocities[2]) / denominator3;
                            double backRightPowerFieldNormalized = (nrotY + nrotX - normalizedVelocities[2]) / denominator3;
                            leftFront.get().setPower(frontLeftPowerFieldNormalized);
                            leftBack.get().setPower(backLeftPowerFieldNormalized);
                            rightFront.get().setPower(frontRightPowerFieldNormalized);
                            rightBack.get().setPower(backRightPowerFieldNormalized);
                        } else {
                            double denominator4 = Math.max(Math.abs(normalizedVelocities[0]) + Math.abs(normalizedVelocities[1]) + Math.abs(normalizedVelocities[2]), 1);
                            double frontLeftPowerNonFieldNormalized = (normalizedVelocities[0] + normalizedVelocities[1] + normalizedVelocities[2]) / denominator4;
                            double backLeftPowerNonFieldNormalized = (normalizedVelocities[0] - normalizedVelocities[1] + normalizedVelocities[2]) / denominator4;
                            double frontRightPowerNonFieldNormalized = (normalizedVelocities[0] - normalizedVelocities[1] - normalizedVelocities[2]) / denominator4;
                            double backRightPowerNonFieldNormalized = (normalizedVelocities[0] + normalizedVelocities[1] - normalizedVelocities[2]) / denominator4;
                            leftFront.get().setPower(frontLeftPowerNonFieldNormalized);
                            leftBack.get().setPower(backLeftPowerNonFieldNormalized);
                            rightFront.get().setPower(frontRightPowerNonFieldNormalized);
                            rightBack.get().setPower(backRightPowerNonFieldNormalized);
                        }
                    } else {
                        if (isFieldCentric) {
                            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
                            double frontLeftPowerFieldOriented = (rotY + rotX + rx) / denominator;
                            double backLeftPowerFieldOriented = (rotY - rotX + rx) / denominator;
                            double frontRightPowerFieldOriented = (rotY - rotX - rx) / denominator;
                            double backRightPowerFieldOriented = (rotY + rotX - rx) / denominator;
                            leftFront.get().setPower(frontLeftPowerFieldOriented / 3);
                            leftBack.get().setPower(backLeftPowerFieldOriented / 3);
                            rightFront.get().setPower(frontRightPowerFieldOriented / 3);
                            rightBack.get().setPower(backRightPowerFieldOriented / 3);
                        } else {
                            double denominator2 = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                            double frontLeftPowerNonField = (y + x + rx) / denominator2;
                            double backLeftPowerNonField = (y - x + rx) / denominator2;
                            double frontRightPowerNonField = (y - x - rx) / denominator2;
                            double backRightPowerNonField = (y + x - rx) / denominator2;
                            leftFront.get().setPower(frontLeftPowerNonField / 3);
                            leftBack.get().setPower(backLeftPowerNonField / 3);
                            rightFront.get().setPower(frontRightPowerNonField / 3);
                            rightBack.get().setPower(backRightPowerNonField / 3);
                        }
                    }
                })
                .setEnd((interrupted) -> {
                    leftFront.get().setPower(0);
                    leftBack.get().setPower(0);
                    rightFront.get().setPower(0);
                    rightBack.get().setPower(0);
                })
                .setFinish(() -> driverTimer.time(TimeUnit.MILLISECONDS) >= time );

    }


    public static double[] normalizeVelocities(double yVelocity, double xVelocity, double turnVelocity) {
        // Calculate the sum of the absolute values of the velocities
        double sum = Math.abs(yVelocity) + Math.abs(xVelocity) + Math.abs(turnVelocity);

        // If the sum is greater than 1, normalize the velocities
        if (sum > 1) {
            double scale = 1.0 / sum; // Calculate the scaling factor
            yVelocity *= scale;
            xVelocity *= scale;
            turnVelocity *= scale;
        }

        return new double[]{yVelocity, xVelocity, turnVelocity};
    }


    public double getIMU() {
        return imu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + Math.toRadians(workingOffset);
    }

    private void resetIMUOffset(double userInputedOffsetDegrees) {
        workingOffset = userInputedOffsetDegrees;
    }

    private void resetIMUOffset() {
        workingOffset = imu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) + workingOffset;
    }

    public Lambda resetIMU() {
        return new Lambda("resetIMU")
                .setInit(this::resetIMUOffset);
    }

    public Lambda overrideIMUOffset(double offset) {
        return new Lambda("overrideIMUOffset")
                .setInit(() -> resetIMUOffset(offset));
    }

}
