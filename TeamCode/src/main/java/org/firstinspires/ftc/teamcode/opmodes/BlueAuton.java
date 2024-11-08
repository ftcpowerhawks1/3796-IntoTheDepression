package org.firstinspires.ftc.teamcode.opmodes;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.OutakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.VerticalSlides;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.LimelightHelper;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

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
    LeftAutonState leftAutonState;

    @Override
    public void init() {

        side = Side.LEFT;
        alliance = Alliance.BLUE;
        leftAutonState = LeftAutonState.PRELOAD;
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
        new Sequential (
                outakeSubsystem.reset(),
                intakeSubsystem.returnIntake()
        );
    }

    @Override
    public void loop() {
        telemetry.addData("Limelight TX: ", limelightHelper.getTX());
        telemetry.addData("Vertical Encoder: ", verticalSlides.getVerticalEncoder());

        telemetry.update();


    }


    public boolean drivebusy() {
        return(true);
    }

    public void testingLogic() {
        switch (side) {
            case LEFT:
                switch (leftAutonState) {
                    case PRELOAD:
                        if (!drivebusy()) {
                            leftAutonState = LeftAutonState.RIGHTSAMPLE;
                        }
                    case RIGHTSAMPLE:
                        if (!drivebusy()) {
                            leftAutonState = LeftAutonState.SCORERIGHTSAMPLE;
                        }
                    case SCORERIGHTSAMPLE:
                        if (!drivebusy()) {
                            leftAutonState = LeftAutonState.MIDDLESAMPLE;
                        }
                    case MIDDLESAMPLE:
                        if (!drivebusy()) {
                            leftAutonState = LeftAutonState.SCOREMIDDLESAMPLE;
                        }
                    case SCOREMIDDLESAMPLE:
                        if (!drivebusy()) {
                            leftAutonState = LeftAutonState.LEFTSAMPLE;
                        }
                    case LEFTSAMPLE:
                        if (!drivebusy()) {
                            leftAutonState = LeftAutonState.SCORELEFTSAMPLE;
                        }
                    case SCORELEFTSAMPLE:
                        if (!drivebusy()) {
                            leftAutonState = LeftAutonState.PICKUPCYCLE;
                        }
                    case PICKUPCYCLE:
                        if (!drivebusy()) {
                            leftAutonState = LeftAutonState.SCORECYCLE;
                        }
                    case SCORECYCLE:
                        if (!drivebusy()) {
                            if (getRuntime() >= 20) {
                                leftAutonState = LeftAutonState.PARK;
                            } else {
                                leftAutonState = LeftAutonState.PICKUPCYCLE;
                            }
                        }
                    case PARK:
                        if (!drivebusy()) {
                            leftAutonState = LeftAutonState.IDLE;
                        }
                    case IDLE:

                }
            case RIGHT:
        }
    }
}
