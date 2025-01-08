package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.util.MercurialAction;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;
@Autonomous(name = "Auto", preselectTeleOp = "Simple OpMode")
public class auto extends talonsOpMode {
    Pose2d initialPose = new Pose2d(-35, -61,Math.toRadians(270));
    MecanumDrive drive = new MecanumDrive(hardwareMap,initialPose);
    Action mainAction = drive.actionBuilder(initialPose)
            //Specimen
            .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.MID_SCORING)))

            .setReversed(true)
                    .splineToConstantHeading(new Vector2d(-9,-33),Math.toRadians(90))
            .setReversed(false)
            //First Sample
                .splineToSplineHeading(new Pose2d(-52,-48,Math.toRadians(135)),Math.toRadians(180))

            .stopAndAdd(new MercurialAction(Intake.INSTANCE.setIntakePosition(Intake.IntakeState.INTAKING)))
            .stopAndAdd(new MercurialAction(HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.FULL_EXTEND)))

            .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))
            //TODO Everything after this
//            //Second Sample
//            .splineToLinearHeading(new Pose2d(-58,-43,Math.toRadians(90)),Math.toRadians(90))
//            .waitSeconds(1)
//
//                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))
//
//            //Third Sample
//            .splineToLinearHeading(new Pose2d(-58,-43,Math.toRadians(60)),Math.toRadians(90))
//            .waitSeconds(1)
//                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))
//
//
//            //Fourth Sample
//            .splineToLinearHeading(new Pose2d(-25,-10,Math.toRadians(0)),Math.toRadians(0))
//
//            .waitSeconds(1)
//                .setReversed(true)
//                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(180))
//            .setReversed(false)
//
//
//    //Fith Sample
//                .splineToLinearHeading(new Pose2d(-25,-10,Math.toRadians(0)),Math.toRadians(0))
//
//            .waitSeconds(1)
//                .setReversed(true)
//                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(180))
//            .setReversed(false)
//
//    //Sixth Sample
//                .splineToLinearHeading(new Pose2d(-25,-10,Math.toRadians(0)),Math.toRadians(0))
//            .waitSeconds(1)
//                .setReversed(true)
//                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(180))

            .build();

    @Override
    public void init() {

    }

    @Override
    public void start() {
        Actions.runBlocking(
                mainAction
        );
    }
    @Override
    public void loop() {

    }
}
