package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;

import java.util.concurrent.TimeUnit;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundBooleanSupplier;

@TeleOp(name = "Teleop", group = "Teleop")
@Config
public class opMode extends talonsOpMode {
    ElapsedTime deltaTime = new ElapsedTime();
    private BoundBooleanSupplier cond1, cond2;

    @Override
    public void init() {
        //conditions
        cond1 = Mercurial.gamepad2().leftTrigger().conditionalBindState().greaterThan(0.05).bind();
        cond2 = Mercurial.gamepad2().rightTrigger().conditionalBindState().greaterThan(0.05).bind();


        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(false));

        // NICKS CONTROLS

        //Bucket Pivots
        Mercurial.gamepad2().leftBumper().onTrue(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.DOWN));
        Mercurial.gamepad2().rightBumper().onTrue(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.UP));

        //Intake Pivots
        Mercurial.gamepad2().dpadUp().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.EXTENDED));
        Mercurial.gamepad2().dpadDown().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED).then(Intake.INSTANCE.intake(0)));

        //Vertical Slides
        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-4800));

        Mercurial.gamepad2().a().onTrue(VerticalSlides.INSTANCE.setSlidePosition(0));


        Mercurial.gamepad2().b().onTrue(HorizontalSlides.INSTANCE.setSlidePosition(-2000));
        Mercurial.gamepad2().x().onTrue(HorizontalSlides.INSTANCE.setSlidePosition(0));



        //Intake Spinner
        Mercurial.gamepad2().leftTrigger().conditionalBindState().greaterThan(0.05).bind().onTrue(Intake.INSTANCE.intake(-1.0));
        Mercurial.gamepad2().rightTrigger().conditionalBindState().greaterThan(0.05).bind().onTrue(Intake.INSTANCE.intake(1.0));

        //END NICKS CONTROLS

        //JONATHAN'S CONTROLS
        Mercurial.gamepad1().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-3800));

    }
    @Override
    public void loop() {

        if (Mercurial.gamepad1().rightBumper().onTrue()) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.slowDriveCommand(false));
        }
        if (Mercurial.gamepad1().rightBumper().onFalse() && Drive.INSTANCE.getDefaultCommand() == Drive.INSTANCE.slowDriveCommand(false)) {
            Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(false));
        }

        if (!cond1.onTrue() && cond2.onTrue()) {
            Intake.INSTANCE.intake(0);
        }

        if (VerticalSlides.INSTANCE.getCurrent()  >= 2){
            VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.INSTANCE.getEncoder());
        }


        telemetry.addData("Time: ", deltaTime.time(TimeUnit.MILLISECONDS));
        telemetry.addData("Current: ", VerticalSlides.INSTANCE.getCurrent());

        telemetry.update();
        deltaTime.reset();

    }
}