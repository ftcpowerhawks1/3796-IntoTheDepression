package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;
@Autonomous(name = "Left Side Auto", preselectTeleOp = "Teleop" )

public class auto extends talonsOpMode {
    @Override
    public void init() {
        Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.DOWN);
        Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED);
        Pose2d initialPose = new Pose2d(11.8, 61.7, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        Action sampleOne = drive.getDrive().actionBuilder(new Pose2d(-35, -61, Math.toRadians(270)
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-9,-33),Math.toRadians(90))
                .setReversed(false)
                //First Sample
                .splineToSplineHeading(new Pose2d(-52,-48,Math.toRadians(135)),Math.toRadians(180))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))

                //Second Sample
                .splineToLinearHeading(new Pose2d(-58,-43,Math.toRadians(90)),Math.toRadians(90))
                .waitSeconds(1)

                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))

                //Third Sample
                .splineToLinearHeading(new Pose2d(-58,-43,Math.toRadians(60)),Math.toRadians(90))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))


                //Fourth Sample
                .splineToLinearHeading(new Pose2d(-25,-10,Math.toRadians(0)),Math.toRadians(0))

                .waitSeconds(1)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(180))
                .setReversed(false)


                //Fith Sample
                .splineToLinearHeading(new Pose2d(-25,-10,Math.toRadians(0)),Math.toRadians(0))

                .waitSeconds(1)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(180))
                .setReversed(false)

                //Sixth Sample
                .splineToLinearHeading(new Pose2d(-25,-10,Math.toRadians(0)),Math.toRadians(0))
                .waitSeconds(1)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(180))


    }

    @Override
    public void loop() {

    }
}
