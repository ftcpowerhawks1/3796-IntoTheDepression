package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.Constants.Intake.intakePos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@HorizontalSlides.Attach
@VerticalSlides.Attach
@Drive.Attach
@Outtake.Attach
@Intake.Attach
@TeleOp(name = "Teleop", group = "Teleop")
public class opMode extends OpMode {
    ColorSensor color;

    @Override
    public void init() {
        color = hardwareMap.get(ColorSensor.class, "color");

        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(true,true));

        // NICKS CONTROLS

        //Bucket Pivots
        Mercurial.gamepad2().leftBumper().onTrue(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.IN));
        Mercurial.gamepad2().rightBumper().onTrue(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.OUT));

        //Intake Pivots
        Mercurial.gamepad2().dpadUp().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.INTAKING));
        Mercurial.gamepad2().dpadDown().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.OUTTAKING));

        //Horizontal Slides
        Mercurial.gamepad2().b().onTrue(HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.FULL_EXTEND));
        Mercurial.gamepad2().x().onTrue(HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.HOME));

        //Vertical Slides
        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HIGH_SCORING));
        Mercurial.gamepad2().a().onTrue(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HOME));

        //Intake Spinner

        Mercurial.gamepad2().leftTrigger().conditionalBindState().greaterThan(0.05).bind().onTrue(Intake.INSTANCE.intake(-1.0));
        Mercurial.gamepad2().rightTrigger().conditionalBindState().lessThan(0.05).bind().onTrue(Intake.INSTANCE.intake(1.0));
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
        telemetry.addData("Color: ", color.red() + " " + color.green() +" " + color.blue());

    }
}