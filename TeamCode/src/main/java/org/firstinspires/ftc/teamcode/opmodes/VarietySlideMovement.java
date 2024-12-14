package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Outtake.Attach
@VerticalSlides.Attach
@TeleOp(name = "VerticalSlidePositions")
public class VarietySlideMovement extends OpMode {

    @Override
    public void init() {

        // Smashing buttons until it works
        Mercurial.gamepad1().x().onTrue(VerticalSlides.INSTANCE.runToPosition(0.0));
        Mercurial.gamepad1().a().onTrue(VerticalSlides.INSTANCE.runToPosition(-500));
        Mercurial.gamepad1().b().onTrue(VerticalSlides.INSTANCE.runToPosition(-1000));
        Mercurial.gamepad1().y().onTrue(VerticalSlides.INSTANCE.runToPosition(-1500));

        Mercurial.gamepad2().x().onTrue(VerticalSlides.INSTANCE.runToPosition(-2000));
        Mercurial.gamepad2().a().onTrue(VerticalSlides.INSTANCE.runToPosition(-2500));
        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.runToPosition(-3000));

        Mercurial.gamepad2().dpadLeft().onTrue(VerticalSlides.INSTANCE.runToPosition(-3500));
        Mercurial.gamepad2().dpadDown().onTrue(VerticalSlides.INSTANCE.runToPosition(-4000));
        Mercurial.gamepad2().dpadRight().onTrue(VerticalSlides.INSTANCE.runToPosition(-4500));
        Mercurial.gamepad2().dpadUp().onTrue(VerticalSlides.INSTANCE.runToPosition(-5000));

    }
    @Override
    public void loop() {
        telemetry.addData("Encoder", VerticalSlides.INSTANCE.getEncoder());
        telemetry.update();
    }

}