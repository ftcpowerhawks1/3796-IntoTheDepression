package org.firstinspires.ftc.teamcode.opmodes;

import android.widget.HorizontalScrollView;

import com.outoftheboxrobotics.photoncore.Photon;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;

import java.util.concurrent.TimeUnit;

import dev.frozenmilk.mercurial.Mercurial;

@TeleOp(name = "Teleop", group = "Teleop")
public class opMode extends talonsOpMode {
    ColorSensor color;
    public static double target = 0;
    ElapsedTime deltaTime = new ElapsedTime();
    @Override
    public void init() {
        color = hardwareMap.get(ColorSensor.class, "color");

        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(false));

        // NICKS CONTROLS

        //Bucket Pivots
        Mercurial.gamepad2().leftBumper().onTrue(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.DOWN));
        Mercurial.gamepad2().rightBumper().onTrue(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.UP));

        //Intake Pivots
        Mercurial.gamepad2().dpadUp().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.EXTENDED));
        Mercurial.gamepad2().dpadDown().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED).then(Intake.INSTANCE.intake(0)));

        //Horizontal Slides
        Mercurial.gamepad2().dpadLeft().onTrue(HorizontalSlides.INSTANCE.runToPosition(-700).then(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.INTAKING).then(HorizontalSlides.INSTANCE.runToPosition(-1500))));
        Mercurial.gamepad2().dpadRight().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED).then(HorizontalSlides.INSTANCE.runToPosition(0).then(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.OUTTAKING))));

        //Vertical Slides
        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-5000));
        Mercurial.gamepad1().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-2500));

        Mercurial.gamepad2().a().onTrue(VerticalSlides.INSTANCE.setSlidePosition(0));


        Mercurial.gamepad2().b().onTrue(HorizontalSlides.INSTANCE.runToPosition(-2000));
        Mercurial.gamepad2().x().onTrue(HorizontalSlides.INSTANCE.runToPosition(0));

        Mercurial.gamepad2().a().onTrue(VerticalSlides.INSTANCE.setSlidePosition(0));


        //Intake Spinner
        Mercurial.gamepad2().leftTrigger().conditionalBindState().greaterThan(0.05).bind().onTrue(Intake.INSTANCE.intake(-1.0));
        Mercurial.gamepad2().rightTrigger().conditionalBindState().lessThan(0.05).bind().onTrue(Intake.INSTANCE.intake(1.0));

        //END NICKS CONTROLS


        //JONATHAN'S CONTROLS


    }
    @Override
    public void loop() {


        if (Mercurial.gamepad1().rightBumper().onTrue()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.slowDriveCommand(false));
        }
        if (Mercurial.gamepad1().rightBumper().onFalse()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(false));
        }

        if (!(Mercurial.gamepad2().leftTrigger().conditionalBindState().greaterThan(0.05).bind().onTrue() || Mercurial.gamepad2().rightTrigger().conditionalBindState().lessThan(0.05).bind().onTrue())) {
            Intake.INSTANCE.intake(0);
        }


        telemetry.addData("Time: ", deltaTime.time(TimeUnit.MILLISECONDS));
        telemetry.update();
        deltaTime.reset();

    }
}