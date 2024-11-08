package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(80, 60, Math.toRadians(180), Math.toRadians(180), 16)
                .build();

        //Red Left
        /*myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-35, -58, Math.toRadians(270)))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d( -7, -35),90)
                        .setReversed(false)
                        .splineToLinearHeading(new Pose2d(-43,-30,Math.toRadians(135)), 90)
                        .setReversed(true)
                .splineToLinearHeading(new Pose2d(-52,-54,Math.toRadians(45)), 90)
                .splineToLinearHeading( new Pose2d(-57,-40,Math.toRadians(90)),90)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-52,-54,Math.toRadians(45)), 0)
                .setReversed(false)

                .splineToLinearHeading( new Pose2d(-55,-40,Math.toRadians(130)),90)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-52,-54,Math.toRadians(45)), 0)
                .setReversed(false)
                //Cycles
                .splineToLinearHeading(new Pose2d(-32,-10,Math.toRadians(350)),270)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-52,-54,Math.toRadians(45)), 275)
                .setReversed(false)

                .splineToLinearHeading(new Pose2d(-30,-8,Math.toRadians(5)),270)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-52,-54,Math.toRadians(45)), 275)
                .setReversed(false)

                .splineToLinearHeading(new Pose2d(-28,-6,Math.toRadians(20)),270)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-52,-54,Math.toRadians(45)), 275)
                .setReversed(false)

                .splineToLinearHeading(new Pose2d(-26,-4,Math.toRadians(5)),270)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-52,-54,Math.toRadians(45)), 275)
                .setReversed(false)
                .strafeToLinearHeading(new Vector2d(33,-58), Math.toRadians(0))

                .build());
*/
        int converstionfactor = 180;

        //Left Side Blue
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(35, 58, Math.toRadians(90)))
                .setReversed(true)
                .splineToConstantHeading(new Vector2d( 7, 35),-90)
                .setReversed(false)
                .splineToLinearHeading(new Pose2d(43,30,Math.toRadians(135+converstionfactor)), -90)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(52,54,Math.toRadians(45+converstionfactor)), 360)
                .splineToLinearHeading( new Pose2d(57,40,Math.toRadians(90+converstionfactor)),-90)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(52,54,Math.toRadians(45+converstionfactor)), 0)
                .setReversed(false)

                .splineToLinearHeading( new Pose2d(55,40,Math.toRadians(130+converstionfactor)),-90)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(52,54,Math.toRadians(45+converstionfactor)), 0)
                .setReversed(false)
                //Cycles
                .splineToLinearHeading(new Pose2d(32,10,Math.toRadians(350+converstionfactor)),-270)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(52,54,Math.toRadians(45+converstionfactor)), -275)
                .setReversed(false)

                .splineToLinearHeading(new Pose2d(30,8,Math.toRadians(5+converstionfactor)),-270)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(52,54,Math.toRadians(45+converstionfactor)), -275)
                .setReversed(false)

                .splineToLinearHeading(new Pose2d(28,6,Math.toRadians(20+converstionfactor)),-270)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(52,54,Math.toRadians(45+converstionfactor)), -275)
                .setReversed(false)

                .splineToLinearHeading(new Pose2d(26,4,Math.toRadians(5+converstionfactor)),-270)
                .setReversed(true)

                .splineToLinearHeading(new Pose2d(52,54,Math.toRadians(45+converstionfactor)), -275)
                .setReversed(false)
                .strafeToLinearHeading(new Vector2d(-33,58), Math.toRadians(0))

                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}