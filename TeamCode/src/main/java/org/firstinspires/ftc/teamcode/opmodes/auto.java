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

        TrajectoryActionBuilder specimin = drive.actionBuilder(initialPose)
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-9,-33),Math.toRadians(90));

        Action sampleOne = specimin.endTrajectory().fresh()
                .setReversed(false)
                .splineToSplineHeading(new Pose2d(-52,-48,Math.toRadians(135)),Math.toRadians(180))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))
                .build();


    }

    @Override
    public void loop() {

    }
}
