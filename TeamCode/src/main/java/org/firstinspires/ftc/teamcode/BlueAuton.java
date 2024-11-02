package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OutakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.VerticalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.LimelightHelper;

@org.firstinspires.ftc.teamcode.Util.BulkReads.Attach

@HorizontalSlides.Attach
@VerticalSlides.Attach
@LimelightHelper.Attach
@IntakeSubsystem.Attach
@OutakeSubsystem.Attach

@Autonomous(name = "BlueAuto", group = "Auto", preselectTeleOp = "BlueTeleop")
public class BlueAuton extends OpMode {
    HorizontalSlides horizontalSlides;
    VerticalSlides verticalSlides;
    LimelightHelper limelightHelper;
    IntakeSubsystem intakeSubsystem;
    OutakeSubsystem outakeSubsystem;

    @Override
    public void init() {
        //Init Subsystems
        HorizontalSlides horizontalSlides = new HorizontalSlides();
        VerticalSlides verticalSlides = new VerticalSlides();
        LimelightHelper limelightHelper = new LimelightHelper();
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
        OutakeSubsystem outakeSubsystem = new OutakeSubsystem();

    }

    @Override
    public void start() {
        super.start();
        intakeSubsystem.returnIntake();
        outakeSubsystem.reset();

    }

    @Override
    public void loop() {

    }
}
