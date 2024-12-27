package org.firstinspires.ftc.teamcode.opmodes.testingOpmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Outtake.Attach
@TeleOp(name = "VarietyServoTest")
public class VarietyServoTest extends OpMode {

    @Override
    public void init() {

        // Smashing buttons until it works
        Mercurial.gamepad1().x().onTrue(Outtake.INSTANCE.setOuttakePivotDirect(0.0)); // HOME
        Mercurial.gamepad1().a().onTrue(Outtake.INSTANCE.setOuttakePivotDirect(0.1)); // PARALLEL WITH FLOOR
        Mercurial.gamepad1().b().onTrue(Outtake.INSTANCE.setOuttakePivotDirect(0.2));
        Mercurial.gamepad1().y().onTrue(Outtake.INSTANCE.setOuttakePivotDirect(0.3));
        Mercurial.gamepad2().x().onTrue(Outtake.INSTANCE.setOuttakePivotDirect(0.4)); // FULL EXTEND
        Mercurial.gamepad2().a().onTrue(Outtake.INSTANCE.setOuttakePivotDirect(0.5)); // ABOUT TO FLIP
        Mercurial.gamepad2().b().onTrue(Outtake.INSTANCE.setOuttakePivotDirect(0.6)); // OH SHIT THAT'S BROKEN
        Mercurial.gamepad2().y().onTrue(Outtake.INSTANCE.setOuttakePivotDirect(0.7)); // I'M NOT GOING TO EVEN TRY


    }
    @Override
    public void loop() {

    }

}