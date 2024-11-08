package org.firstinspires.ftc.teamcode.opmodes;


import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.LimelightHelper;
import org.firstinspires.ftc.teamcode.Subsystems.OutakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.VerticalSlides;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;

import dev.frozenmilk.mercurial.Mercurial;

@org.firstinspires.ftc.teamcode.Util.BulkReads.Attach

@HorizontalSlides.Attach
@VerticalSlides.Attach
@LimelightHelper.Attach
@IntakeSubsystem.Attach
@OutakeSubsystem.Attach

@TeleOp(name = "BlueTeleop", group = "Teleop")
public class BLUETeleop extends RobotHardware {
    LimelightHelper limelightHelper;
    IntakeSubsystem intakeSubsystem;
    IMU imu;
    double botHeading,previousRobotHeading,X,Y,turn,previousX,previousY, previousTurn,componentX,componentY,rotX,rotY,previousComponentX,previousComponentY = 0;

    DcMotorEx frontLeft, frontRight, backLeft, backRight;

    @Override
    public void init() {
        intakeSubsystem = new IntakeSubsystem();
        limelightHelper = new LimelightHelper();

        imu = getIMU();
        frontLeft = getLeftFront();
        frontRight = getRightFront();
        backLeft = getLeftBack();
        backRight = getRightBack();
    }

    @Override
    public void loop() {
        fieldOriented();

        Mercurial.gamepad1().a().toggleTrue(new IntakeCommand());

        intakeColorDetectionTest();

        updateFieldOrientedComponents();

        telemetry.update();

    }

    void fieldOriented() {
        if (Mercurial.gamepad1().options().onTrue()) {
            imu.resetYaw();
            telemetry.addData("gyro reset", "");
        }

        X = Mercurial.gamepad1().leftStickX().state();
        Y = Mercurial.gamepad1().leftStickY().state();
        turn = Mercurial.gamepad1().rightStickX().state();
        botHeading = imu.getRobotYawPitchRollAngles().getYaw();

        if (botHeading == previousRobotHeading)  {
            if (X == previousX) {
                componentX = previousComponentX;
            } else {
                componentX = X * Math.cos(-botHeading);
            }

            if (Y == previousY) {
                componentY = previousComponentY;
            } else {
                componentY = Y * Math.sin(-botHeading);
            }
        }

        rotX = componentX - componentY;
        rotY = componentX + componentY;

        rotX = rotX * 1.1;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn), 1);
        double frontLeftPower = (rotY + rotX + turn) / denominator;
        double backLeftPower = (rotY - rotX + turn) / denominator;
        double frontRightPower = (rotY - rotX - turn) / denominator;
        double backRightPower = (rotY + rotX - turn) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }
    void updateFieldOrientedComponents() {
        previousX=X;
        previousY=Y;
        previousRobotHeading =botHeading;
        previousTurn =turn;
        previousComponentY = componentY;
        previousComponentX = componentX;
    }
    void intakeColorDetectionTest() {
        telemetry.addData("Red: ", intakeSubsystem.getColor().red);
        telemetry.addData("Green: ", intakeSubsystem.getColor().green);
        telemetry.addData("Blue: ", intakeSubsystem.getColor().blue);

        if (intakeSubsystem.getColor().red >= 0.8 && intakeSubsystem.getColor().green >= 0.8 && intakeSubsystem.getColor().blue <= 0.2) {
            telemetry.addData(">","Yellow Detected");
        } else {
            telemetry.addData(">","No Yellow Detected");
        }
    }
}
