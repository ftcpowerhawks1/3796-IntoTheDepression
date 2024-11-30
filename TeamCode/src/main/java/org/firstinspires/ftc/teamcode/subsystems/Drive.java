package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotor;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.dairy.pasteurized.SDKGamepad;
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

    public Lambda driveCommand(boolean isFieldCentric) {
        BoundGamepad gamepad1 = Mercurial.gamepad1();
        return new Lambda("mecanum-drive-robot-centric")
                .setInit(() -> {})
                .setExecute(() -> {
                    double y = gamepad1.leftStickY().state();
                    double x = gamepad1.leftStickX().state();
                    double rx = gamepad1.rightStickX().state();

                    if (gamepad1.a().onTrue()) {
                        imu.get().resetYaw();
                    }

                    double botHeading = imu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

                    // Rotate the movement direction counter to the bot's rotation
                    double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
                    double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

                    rotX *= 1.1;

                    double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
                    double frontLeftPower = (rotY + rotX + rx) / denominator;
                    double backLeftPower = (rotY - rotX + rx) / denominator;
                    double frontRightPower = (rotY - rotX - rx) / denominator;
                    double backRightPower = (rotY + rotX - rx) / denominator;

                    double denominator2 = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                    double frontLeftPower2 = (y + x + rx) / denominator;
                    double backLeftPower2 = (y - x + rx) / denominator;
                    double frontRightPower2 = (y - x - rx) / denominator;
                    double backRightPower2 = (y + x - rx) / denominator;

                    if (isFieldCentric) {
                        leftFront.get().setPower(frontLeftPower);
                        leftBack.get().setPower(backLeftPower);
                        rightFront.get().setPower(frontRightPower);
                        rightBack.get().setPower(backRightPower);
                    }
                    else {
                        leftFront.get().setPower(frontLeftPower2);
                        leftBack.get().setPower(backLeftPower2);
                        rightFront.get().setPower(frontRightPower2);
                        rightBack.get().setPower(backRightPower2);
                    }
                    /*leftFront.get().setPower(y);
                    leftBack.get().setPower(y);
                    rightFront.get().setPower(-gamepad1.rightStickY().state());
                    rightBack.get().setPower(-gamepad1.rightStickY().state());*/
                })
                .setFinish(() -> false);
    }

    public Lambda slowDriveCommand(boolean isFieldCentric) {
        BoundGamepad gamepad1 = Mercurial.gamepad1();
        return new Lambda("mecanum-drive-robot-centric")
                .setInit(() -> {})
                .setExecute(() -> {
                    double y = gamepad1.leftStickY().state();
                    double x = gamepad1.leftStickX().state();
                    double rx = gamepad1.rightStickX().state();

                    if (gamepad1.a().onTrue()) {
                        imu.get().resetYaw();
                    }

                    double botHeading = imu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

                    // Rotate the movement direction counter to the bot's rotation
                    double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
                    double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

                    rotX *= 1.1;

                    double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
                    double frontLeftPower = (rotY + rotX + rx) / denominator;
                    double backLeftPower = (rotY - rotX + rx) / denominator;
                    double frontRightPower = (rotY - rotX - rx) / denominator;
                    double backRightPower = (rotY + rotX - rx) / denominator;

                    double denominator2 = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                    double frontLeftPower2 = (y + x + rx) / denominator;
                    double backLeftPower2 = (y - x + rx) / denominator;
                    double frontRightPower2 = (y - x - rx) / denominator;
                    double backRightPower2 = (y + x - rx) / denominator;

                    if (isFieldCentric) {
                        leftFront.get().setPower(frontLeftPower / 3);
                        leftBack.get().setPower(backLeftPower / 3);
                        rightFront.get().setPower(frontRightPower / 3);
                        rightBack.get().setPower(backRightPower / 3);
                    }
                    else {
                        leftFront.get().setPower(frontLeftPower2 / 3);
                        leftBack.get().setPower(backLeftPower2 / 3);
                        rightFront.get().setPower(frontRightPower2 / 3);
                        rightBack.get().setPower(backRightPower2 / 3);
                    }
                    /*leftFront.get().setPower(y);
                    leftBack.get().setPower(y);
                    rightFront.get().setPower(-gamepad1.rightStickY().state());
                    rightBack.get().setPower(-gamepad1.rightStickY().state());*/
                })
                .setFinish(() -> false);
    }
}
