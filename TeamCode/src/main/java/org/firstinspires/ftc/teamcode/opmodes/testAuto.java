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
import org.firstinspires.ftc.teamcode.util.SilkRoad;
import org.firstinspires.ftc.teamcode.util.talonsOpMode;
import org.firstinspires.ftc.teamcode.util.utilActions.MercurialAction;

import dev.frozenmilk.mercurial.commands.groups.Parallel;

@SilkRoad.Attach
@Autonomous(name = "Test Auto", preselectTeleOp = "Simple OpMode")
public class testAuto extends talonsOpMode {
    Pose2d initialPose = new Pose2d(-35, -61,Math.toRadians(270));
    MecanumDrive drive = new MecanumDrive(hardwareMap,initialPose);
    Action mainAction = drive.actionBuilder(initialPose)
            .stopAndAdd(new MercurialAction(HorizontalSlides.INSTANCE.setSlidePosition(HorizontalSlides.SlideState.FULL_EXTEND)))
            .build();

    @Override
    public void init() {

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
