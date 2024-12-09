package org.firstinspires.ftc.teamcode;


public class Constants {
    public static final class Drive {
        public static final String leftFront = "lf";
        public static final String leftBack = "lb";
        public static final String rightFront = "rf";
        public static final String rightBack = "rb";
    }





    public static final class Slides {
        public static final String HORIZONTALLEFT = "Horizontalleft";
        public static final String HORIZONTALRIGHT = "Horizontalright";

        public static final String VERTICALLEFT = "Verticalleft";
        public static final String VERTICALRIGHT = "Verticalright";

        public static final double VERTICALCURRENTLIMIT = 8.2;
        public static final double VERTICALVELOCITYMIN = 1;
        public static final double HORIZONTALVELOCITYMIN = 1;

        public static final double HORIZONTALCURRENTLIMIT = 8.2;

        public static final double fullIntakeExtend = -1500;
        public static final double halfIntakeExtend = -750;

        public static final double highScoringPos = -5000;
        public static final double midScoringPos = -2500;
        public static final double specimenScoringPos = 0.0;
        public static final double homePos = 0.0;
    }



    public static final class Intake {
        public static final String intakePivot = "intakePivot";
        public static final String intake = "intake";
        public static final double intakePos = 0;
        public static final double depositPos = 0.5;
        public static final double intakePower = 1;
        public static final double outtakePower = -1;



    }
    public static final class Outtake {
        public static final String outtakePivotLeft = "LeftOuttakeServo";
        public static final String outtakePivotRight = "RightOuttakeServo";
        public static final double inPos = 0.0;
        public static final double outPos = 0.4;
    }




}
