package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OutakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.VerticalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.LimelightHelper;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.commands.OuttakeDumpCommand;


import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

@org.firstinspires.ftc.teamcode.Util.BulkReads.Attach

@Mercurial.Attach
@HorizontalSlides.Attach
@VerticalSlides.Attach
@LimelightHelper.Attach
@IntakeSubsystem.Attach
@OutakeSubsystem.Attach

@TeleOp(name = "PleaseJustCyclingWork", group = "Praying")
public class PleaseJustCyclingWork extends RobotHardware {
    LimelightHelper limelightHelper;
    IntakeSubsystem intakeSubsystem;
    VerticalSlides verticalSlides;
    HorizontalSlides horizontalSlides;
    OutakeSubsystem outakeSubsystem;
    @Override
    public void init() {
        intakeSubsystem = new IntakeSubsystem();
        limelightHelper = new LimelightHelper();
        verticalSlides = new VerticalSlides();
        horizontalSlides = new HorizontalSlides();
        outakeSubsystem = new OutakeSubsystem();

        intakeSubsystem.returnIntake();
        outakeSubsystem.reset();
        horizontalSlides.retractionScript();
    }


    public void loop() {
        new Sequential(
          new IntakeCommand(),
                intakeSubsystem.outtake(1),
                new OuttakeDumpCommand()
        );


    }
}
