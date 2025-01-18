package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.VerticalSlides;
import org.firstinspires.ftc.teamcode.util.utilActions.MercurialAction;
import org.firstinspires.ftc.teamcode.util.SilkRoad;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;

import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@SilkRoad.Attach
@Autonomous(name = "Auto", preselectTeleOp = "Simple OpMode")
public class auto extends talonsOpMode {
    Pose2d initialPose = new Pose2d(-35, -61,Math.toRadians(270));
    Action mainAction;
    MecanumDrive drive;


    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap,initialPose);
        mainAction = drive.actionBuilder(initialPose)
                //Specimen
                .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.SPECIMEN_SCORING)))

                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-9,-33),Math.toRadians(90))
                .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HOME)))

                .setReversed(false)
                //First Sample
                .splineToSplineHeading(new Pose2d(-52,-48,Math.toRadians(135)),Math.toRadians(180))
                .stopAndAdd(new MercurialAction(new Sequential(
                        new Parallel(
                                HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.FULL_EXTEND),
                                Intake.INSTANCE.setIntakePosition(Intake.IntakeState.INTAKING)
                        ), new Wait(1),
                        new Parallel(
                                HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.HOME),
                                Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED).then(Intake.INSTANCE.intake(0)))


                )))

                .waitSeconds(1)


                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))

                .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HIGH_SCORING)))
                .stopAndAdd(new MercurialAction(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.UP)))

                .waitSeconds(0.5)

                .stopAndAdd(new MercurialAction(new Parallel(
                        VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HOME),
                        Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.DOWN))))


                //Second Sample
                .splineToLinearHeading(new Pose2d(-58,-43,Math.toRadians(90)),Math.toRadians(90))
                .stopAndAdd(new MercurialAction(new Sequential(
                        new Parallel(
                                HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.FULL_EXTEND),
                                Intake.INSTANCE.setIntakePosition(Intake.IntakeState.INTAKING)
                        ), new Wait(1),
                        new Parallel(
                                HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.HOME),
                                Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED).then(Intake.INSTANCE.intake(0)))


                )))
                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))

                .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HIGH_SCORING)))
                .stopAndAdd(new MercurialAction(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.UP)))
                .waitSeconds(0.5)
                .stopAndAdd(new MercurialAction(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.DOWN)))
                .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HOME)))

                //Third Sample
                .splineToLinearHeading(new Pose2d(-58,-43,Math.toRadians(60)),Math.toRadians(90))
                .stopAndAdd(new MercurialAction(new Sequential(
                        new Parallel(
                                HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.FULL_EXTEND),
                                Intake.INSTANCE.setIntakePosition(Intake.IntakeState.INTAKING)
                        ), new Wait(1),
                        new Parallel(
                                HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.HOME),
                                Intake.INSTANCE.setIntakePosition(Intake.IntakeState.RETRACTED).then(Intake.INSTANCE.intake(0)))


                )))
                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(90))

                .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HIGH_SCORING)))
                .stopAndAdd(new MercurialAction(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.UP)))
                .waitSeconds(0.5)
                .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HOME)))
                .stopAndAdd(new MercurialAction(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.DOWN)))

                //TODO Everything after this

//            //Fourth Sample
//            .splineToLinearHeading(new Pose2d(-25,-10,Math.toRadians(0)),Math.toRadians(0))
//
//            .waitSeconds(1)
//                .setReversed(true)
//                .splineToLinearHeading(new Pose2d(-55,-55,Math.toRadians(45)),Math.toRadians(180))
//            .setReversed(false)
//
//            .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HIGH_SCORING)))
//            .stopAndAdd(new MercurialAction(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.UP)))
//            .waitSeconds(0.5)
//            .stopAndAdd(new MercurialAction(Outtake.INSTANCE.setOuttakePivot(Outtake.OuttakePivotState.DOWN)))
//            .stopAndAdd(new MercurialAction(VerticalSlides.INSTANCE.setSlidePosition(VerticalSlides.SlideState.HOME)))
//

                .build();
    }

    @Override
    public void start() {
        SilkRoad.RunAsync(
                mainAction
        );
    }
    @Override
    public void loop() {

    }
}
