package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.LimelightHelper;
import org.firstinspires.ftc.teamcode.Subsystems.OutakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.VerticalSlides;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

@org.firstinspires.ftc.teamcode.Util.BulkReads.Attach
@HorizontalSlides.Attach
@OutakeSubsystem.Attach
@VerticalSlides.Attach
@IntakeSubsystem.Attach
@LimelightHelper.Attach
@TeleOp(name = "BlueTeleop", group = "Teleop")
public class BLUETeleop extends RobotHardware {
    LimelightHelper limelightHelper;
    IntakeSubsystem intakeSubsystem;
    @Override
    public void init() {
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
        LimelightHelper limelightHelper = new LimelightHelper();
    }

    @Override
    public void loop() {
        Mercurial.gamepad1().a().whileTrue(new Sequential(intakeSubsystem.intake(1).then(intakeSubsystem.returnIntake())));
        Mercurial.gamepad1().a().onFalse(intakeSubsystem.returnIntake());
        if (limelightHelper.getTX().isPresent()) {
            telemetry.addData("Tx: ", limelightHelper.getTX().isPresent());
        }
        telemetry.addData("Red: ", intakeSubsystem.getColor().red);
        telemetry.addData("Green: ", intakeSubsystem.getColor().green);
        telemetry.addData("Blue: ", intakeSubsystem.getColor().blue);

        if (intakeSubsystem.getColor().red >= 0.8 && intakeSubsystem.getColor().green >= 0.8 && intakeSubsystem.getColor().blue <= 0.2) {
            telemetry.addData(">","Yellow Detected");
        } else {
            telemetry.addData(">","No Yellow Detected");
        }


        telemetry.update();
    }
}
