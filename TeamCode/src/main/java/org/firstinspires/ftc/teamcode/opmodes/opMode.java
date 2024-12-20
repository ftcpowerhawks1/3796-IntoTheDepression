package org.firstinspires.ftc.teamcode.opmodes;

import android.widget.HorizontalScrollView;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;

import dev.frozenmilk.mercurial.Mercurial;


@TeleOp(name = "Teleop", group = "Teleop")
public class opMode extends talonsOpMode {
    ColorSensor color;

    @Override
    public void init() {
        color = hardwareMap.get(ColorSensor.class, "color");

        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true,true));

        // NICKS CONTROLS

        //Bucket Pivots
        Mercurial.gamepad2().leftBumper().onTrue(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.DOWN));
        Mercurial.gamepad2().rightBumper().onTrue(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.UP));

        //Intake Pivots
        Mercurial.gamepad2().dpadUp().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.EXTENDED));
        Mercurial.gamepad2().dpadDown().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED));

        //Horizontal Slides
        Mercurial.gamepad2().b().onTrue(HorizontalSlides.INSTANCE.runToPosition(-1500));
        Mercurial.gamepad2().x().onTrue(HorizontalSlides.INSTANCE.runToPosition(0));
        Mercurial.gamepad2().dpadLeft().onTrue(HorizontalSlides.INSTANCE.runToPosition(-700).then(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.INTAKING).then(HorizontalSlides.INSTANCE.runToPosition(-1500))));
        Mercurial.gamepad2().dpadRight().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED).then(HorizontalSlides.INSTANCE.runToPosition(0).then(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.OUTTAKING))));

        //Vertical Slides
        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-5000));
        Mercurial.gamepad2().a().onTrue(VerticalSlides.INSTANCE.setSlidePosition(0));

        //Intake Spinner
        Mercurial.gamepad2().leftTrigger().conditionalBindState().greaterThan(0.05).bind().onTrue(Intake.INSTANCE.intake(-1.0));
        Mercurial.gamepad2().rightTrigger().conditionalBindState().lessThan(0.05).bind().onTrue(Intake.INSTANCE.intake(1.0));

        //END NICKS CONTROLS


        //JONATHAN'S CONTROLS
        Mercurial.gamepad1().options().onTrue(Drive.INSTANCE.resetIMU());


    }
    @Override
    public void loop() {
        if (Mercurial.gamepad1().rightBumper().onTrue()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.slowDriveCommand(true,false));
        }
        if (Mercurial.gamepad1().rightBumper().onFalse()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true,false));
        }

        if (!(Mercurial.gamepad2().leftTrigger().conditionalBindState().greaterThan(0.05).bind().state() || Mercurial.gamepad2().rightTrigger().conditionalBindState().lessThan(0.05).bind().state())) {
            Intake.INSTANCE.intake(0);
        }

        if (color.red() >= 100 || color.blue()>=100) {
            gamepad1.rumble(200);
            gamepad2.rumble(200);
        }

    }
}