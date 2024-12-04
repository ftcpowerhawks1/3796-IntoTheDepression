package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.commands.*;
import org.firstinspires.ftc.teamcode.subsystems.*;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@Mercurial.Attach
@HorizontalSlides.Attach
@VerticalSlides.Attach
@Drive.Attach
@TeleOp(name = "Main TeleOp")
public class opmode extends OpMode {
    ColorSensor color;

    @Override
    public void init() {
        color = hardwareMap.get(ColorSensor.class, "color");

        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        Mercurial.gamepad2().a()
                .onTrue(HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.FULL_EXTEND));

        Mercurial.gamepad2().b().onTrue(HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.HOME));

        Mercurial.gamepad2().x().onTrue(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HIGH_SCORING));
        Mercurial.gamepad2().dpadUp().onTrue(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.MID_SCORING));

        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HOME));

        Mercurial.gamepad1().x().onTrue(Intake.INSTANCE.intake(1.0));
        Mercurial.gamepad1().b().onTrue(Intake.INSTANCE.intake(-1.0));


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

        if (VerticalSlides.INSTANCE.getCurrent()>= Constants.Slides.VERTICALCURRENTLIMIT) {
            VerticalSlides.INSTANCE.setSlides(VerticalSlides.SlideState.HOME);
            gamepad1.rumble(1000);
            gamepad2.rumble(1000);
        }

        if (HorizontalSlides.INSTANCE.getCurrent()>= Constants.Slides.HORIZONTALCURRENTLIMIT) {
            HorizontalSlides.INSTANCE.setSlides(HorizontalSlides.SlideState.HOME);
            gamepad1.rumble(1000);
            gamepad2.rumble(1000);
        }

    }
}