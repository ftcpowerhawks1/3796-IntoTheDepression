//package org.firstinspires.ftc.teamcode.util.localizers;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.PoseVelocity2d;
//import com.acmerobotics.roadrunner.Vector2d;
//import com.qualcomm.hardware.limelightvision.LLResult;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//
//import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
//import org.firstinspires.ftc.teamcode.roadrunner.Localizer;
//
//@Config
//public final class BAKELocalizerJava implements Localizer {
//    private final Localizer actualLocalizer;
//    private final Limelight3A trackerLimelight;
//    private Pose2d pose;
//
//    public BAKELocalizerJava(Localizer localizer, Pose2d initialPose, Limelight3A limelight) {
//        actualLocalizer = localizer;
//        this.pose = initialPose;
//        trackerLimelight = limelight;
//        trackerLimelight.start();
//
//    }
//
//    @Override
//    public void setPose(Pose2d pose) {
//        this.pose = pose;
//        actualLocalizer.setPose(pose);
//
//    }
//
//    @Override
//    public Pose2d getPose() {
//        pose =  actualLocalizer.getPose();
//        return pose;
//    }
//
//    @Override
//    public PoseVelocity2d update() {
//        PoseVelocity2d returnval = actualLocalizer.update();
//        trackerLimelight.updateRobotOrientation(pose.heading.toDouble());
//        LLResult result = trackerLimelight.getLatestResult();
//        Pose2d tempPose;
//        if (result != null) {
//            if (result.isValid()) {
//                Pose3D botpose = result.getBotpose_MT2();
//                tempPose = new Pose2d(new Vector2d(botpose.getPosition().x, botpose.getPosition().y) , botpose.getOrientation().getYaw());
//                actualLocalizer.setPose(tempPose);
//            } else{
//                pose = actualLocalizer.getPose();
//            }
//        } else {
//            pose = actualLocalizer.getPose();
//        }
//        return(returnval);
//
//    }
//}
