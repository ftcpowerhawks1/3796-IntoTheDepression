package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.LimelightHelper;
import org.firstinspires.ftc.teamcode.Subsystems.OutakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.VerticalSlides;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;

import dev.frozenmilk.mercurial.Mercurial;

@org.firstinspires.ftc.teamcode.Util.BulkReads.Attach

@HorizontalSlides.Attach
@VerticalSlides.Attach
@LimelightHelper.Attach
@IntakeSubsystem.Attach
@OutakeSubsystem.Attach

@TeleOp(name = "BlueTeleop", group = "Teleop")
public class BLUETeleop extends RobotHardware {
    LimelightHelper limelightHelper;
    IntakeSubsystem intakeSubsystem;
    @Override
    public void init() {
        intakeSubsystem = new IntakeSubsystem();
        limelightHelper = new LimelightHelper();
    }

    @Override
    public void loop() {
        Mercurial.gamepad1().a().toggleTrue(new IntakeCommand());

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
