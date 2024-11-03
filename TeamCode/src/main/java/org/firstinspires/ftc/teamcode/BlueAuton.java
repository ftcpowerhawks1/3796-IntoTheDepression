package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OutakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.VerticalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.LimelightHelper;

import dev.frozenmilk.mercurial.Mercurial;

@org.firstinspires.ftc.teamcode.Util.BulkReads.Attach

@HorizontalSlides.Attach
@VerticalSlides.Attach
@LimelightHelper.Attach
@IntakeSubsystem.Attach
@OutakeSubsystem.Attach

@Autonomous(name = "BlueAuto", group = "Auto", preselectTeleOp = "BlueTeleop")
public class BlueAuton extends RobotHardware {
    HorizontalSlides horizontalSlides;
    VerticalSlides verticalSlides;
    LimelightHelper limelightHelper;
    IntakeSubsystem intakeSubsystem;
    OutakeSubsystem outakeSubsystem;

    Alliance alliance;
    Side side;

    @Override
    public void init() {

        side = Side.LEFT;
        alliance = Alliance.BLUE;
        //Init Subsystems
        HorizontalSlides horizontalSlides = new HorizontalSlides();
        VerticalSlides verticalSlides = new VerticalSlides();
        LimelightHelper limelightHelper = new LimelightHelper();
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
        OutakeSubsystem outakeSubsystem = new OutakeSubsystem();

    }

    @Override
    public void init_loop() {

        if (Mercurial.gamepad1().dpadLeft().onTrue()) {side = Side.LEFT;}
        if (Mercurial.gamepad1().dpadRight().onTrue()) {side = Side.RIGHT;}

        telemetry.addData("Alliance: ", "Blue");

        if (side == Side.LEFT) {
            telemetry.addData("Current Side: ", "Left");
        } else {
            telemetry.addData("Current Side: ", "Right");
        }

        telemetry.addData("This is the Auto For Blue Alliance ", "Select Other Autonomous For Red");
        telemetry.addData("Press Dpad Left for Left Side", "Press Dpad Right for Right Side");

        telemetry.update();
    }

    @Override
    public void start() {
        super.start();
        intakeSubsystem.returnIntake();
        outakeSubsystem.reset();

    }

    @Override
    public void loop() {
        telemetry.addData("Limelight TX: ", limelightHelper.getTX());
        telemetry.addData("Vertical Encoder: ", verticalSlides.getVerticalEncoder());


        telemetry.update();
    }
}
