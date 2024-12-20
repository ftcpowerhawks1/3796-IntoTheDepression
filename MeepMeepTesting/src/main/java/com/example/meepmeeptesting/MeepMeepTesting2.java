package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting2 {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        double aliiance = 1;
        double offset = 0;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(70, 60, Math.toRadians(180), Math.toRadians(180), 18)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-35*aliiance, -61*aliiance, Math.toRadians(270+offset)))
                //Specimen
                .setReversed(true)
                    .splineToConstantHeading(new Vector2d(-9*aliiance,-33*aliiance),Math.toRadians(90+offset))
                .setReversed(false)
                //First Sample
                .splineToSplineHeading(new Pose2d(-52*aliiance,-48*aliiance,Math.toRadians(135+offset)),Math.toRadians(180+offset))
                        .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(-55*aliiance,-55*aliiance,Math.toRadians(45+offset)),Math.toRadians(90+offset))

                //Second Sample
                .splineToLinearHeading(new Pose2d(-58*aliiance,-43*aliiance,Math.toRadians(90+offset)),Math.toRadians(90+offset))
                .waitSeconds(1)

                .splineToLinearHeading(new Pose2d(-55*aliiance,-55*aliiance,Math.toRadians(45+offset)),Math.toRadians(90+offset))

                //Third Sample
                .splineToLinearHeading(new Pose2d(-58*aliiance,-43*aliiance,Math.toRadians(60+offset)),Math.toRadians(90+offset))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(-55*aliiance,-55*aliiance,Math.toRadians(45+offset)),Math.toRadians(90+offset))


                //Fourth Sample
                .splineToLinearHeading(new Pose2d(-25*aliiance,-10*aliiance,Math.toRadians(0+offset)),Math.toRadians(0+offset))

                .waitSeconds(1)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-55*aliiance,-55*aliiance,Math.toRadians(45+offset)),Math.toRadians(180+offset))
                .setReversed(false)


                //Fith Sample
                .splineToLinearHeading(new Pose2d(-25*aliiance,-10*aliiance,Math.toRadians(0+offset)),Math.toRadians(0+offset))

                .waitSeconds(1)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-55*aliiance,-55*aliiance,Math.toRadians(45+offset)),Math.toRadians(180+offset))
                .setReversed(false)

                //Sixth Sample
                .splineToLinearHeading(new Pose2d(-25*aliiance,-10*aliiance,Math.toRadians(0+offset)),Math.toRadians(0+offset))
                .waitSeconds(1)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-55*aliiance,-55*aliiance,Math.toRadians(45+offset)),Math.toRadians(180+offset))

                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_LIGHT)
                .setDarkMode(false)
                .setBackgroundAlpha(0.1f)
                .addEntity(myBot)
                .start();
    }
}