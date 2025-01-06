package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;

import dev.frozenmilk.mercurial.Mercurial;

@TeleOp(name = "Simple OpMode", group = "Teleop")
public class simplifiedOpMode extends talonsOpMode {

    @Override
    public void init() {

        //driving
        Drive.INSTANCE.setDefaultCommand(Drive.INSTANCE.driveCommand(false));


        //bucket controls
        Mercurial.gamepad2().rightBumper().onTrue(Outtake.INSTANCE.toggleState());
        Mercurial.gamepad2().leftBumper().onTrue(Outtake.INSTANCE.toggleState());


        //intake controls
        Mercurial.gamepad2().dpadUp().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.EXTENDED));
        Mercurial.gamepad2().dpadDown().onTrue(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED).then(Intake.INSTANCE.intake(0)));
        //------------------
        Mercurial.gamepad2().leftTrigger().conditionalBindState().greaterThan(0.05).bind().onTrue(Intake.INSTANCE.intake(-1.0));
        Mercurial.gamepad2().rightTrigger().conditionalBindState().greaterThan(0.05).bind().onTrue(Intake.INSTANCE.intake(1.0));


        //horizontal slides
        Mercurial.gamepad2().b().onTrue(HorizontalSlides.INSTANCE.runToPosition(-2000));
        Mercurial.gamepad2().x().onTrue(HorizontalSlides.INSTANCE.runToPosition(0));


        //vertical slides
        Mercurial.gamepad2().y().onTrue(VerticalSlides.INSTANCE.setSlidePosition(-4800));
        Mercurial.gamepad2().a().onTrue(VerticalSlides.INSTANCE.setSlidePosition(0));

    }

    @Override
    public void loop() {
        if (VerticalSlides.INSTANCE.getCurrent()  >= 2){
            VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.INSTANCE.getEncoder());
        }
    }
}
