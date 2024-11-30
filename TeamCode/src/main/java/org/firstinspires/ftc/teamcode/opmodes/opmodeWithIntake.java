package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.Constants.Intake.intakePos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@HorizontalSlides.Attach
@VerticalSlides.Attach
@Drive.Attach
@Intake.Attach
@TeleOp(name = "Testing TeleOp")
public class opmodeWithIntake extends OpMode {
    ColorSensor color;

    @Override
    public void init() {
        color = hardwareMap.get(ColorSensor.class, "color");

        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        Mercurial.gamepad2().a()
                .onTrue(HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.FULL_EXTEND).then(Intake.INSTANCE.setPivot(intakePos)));

        Mercurial.gamepad2().b().onTrue(HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.HOME));

        Mercurial.gamepad2().x().onTrue(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HIGH_SCORING));
        Mercurial.gamepad2().dpadUp().onTrue(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.MID_SCORING));

        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HOME));



    }
    @Override
    public void loop() {
        if (Mercurial.gamepad1().rightBumper().onTrue()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.slowDriveCommand(true));
        }
        if (Mercurial.gamepad1().rightBumper().onFalse()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        }

        if (color.red() >= 100 || color.blue()>=100) {
            gamepad1.rumble(200);
            gamepad2.rumble(200);
        }

    }
}