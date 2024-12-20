package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 18)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-35, -61, Math.toRadians(270)))
                //Specimen
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

                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_LIGHT)
                .setDarkMode(false)
                .setBackgroundAlpha(0.1f)
                .addEntity(myBot)
                .start();
    }
}