package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Outtake.Attach
@VerticalSlides.Attach
@TeleOp(name = "OuttakeServoPositions")
public class VarietySlideMovement extends OpMode {

    @Override
    public void init() {

        // Smashing buttons until it works
        Mercurial.gamepad1().x().onTrue(VerticalSlides.INSTANCE.setSlidePosition(0.0));
        Mercurial.gamepad1().a().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-500));
        Mercurial.gamepad1().b().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-1000));
        Mercurial.gamepad1().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-1500));

        Mercurial.gamepad2().x().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-2000));
        Mercurial.gamepad2().a().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-2500));
        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-3000));

        Mercurial.gamepad2().dpadLeft().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-3500));
        Mercurial.gamepad2().dpadDown().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-4000));
        Mercurial.gamepad2().dpadRight().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-4500));
        Mercurial.gamepad2().dpadUp().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-5000));

    }
    @Override
    public void loop() {
        telemetry.addData("Encoder", VerticalSlides.INSTANCE.getEncoder());
        telemetry.update();
    }

}