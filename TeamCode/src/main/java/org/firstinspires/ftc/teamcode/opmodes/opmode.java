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
                .onTrue(HorizontalSlides.INSTANCE.runToPosition(-1500));

        Mercurial.gamepad2().b().onTrue(HorizontalSlides.INSTANCE.runToPosition(0));

        Mercurial.gamepad2().x().onTrue(VerticalSlides.INSTANCE.runToPosition(-5000));
        Mercurial.gamepad2().dpadUp().onTrue(VerticalSlides.INSTANCE.runToPosition(-2500));

        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.runToPosition(0));


    }
    @Override
    public void loop() {
        if (Mercurial.gamepad1().rightBumper().onTrue()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.slowDriveCommand(true));
        }
        if (Mercurial.gamepad1().rightBumper().onFalse()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true));
        }
    }
}