package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.util.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class LimelightHelper extends SDKSubsystem {
    public static final LimelightHelper INSTANCE = new LimelightHelper();
    private LimelightHelper() {}



    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    private final Cell<Limelight3A> limelight = subsystemCell(() -> getHardwareMap().get(Limelight3A.class, Constants.Vision.LIMELIGHT_NAME));
    private LLResult result = null;

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        opMode.getOpMode().telemetry.setMsTransmissionInterval(11);
        limelight.get().pipelineSwitch(Constants.Vision.DEFAULT_PIPELINE);
        limelight.get().start();
    }
    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        result = limelight.get().getLatestResult();
    }

    public double limelight_aim_proportional() {
        //TODO Tune this value
        double kP = .035;

        // tx ranges from (-hfov/2) to (hfov/2) in degrees. If your target is on the rightmost edge of
        // your limelight 3 feed, tx should return roughly 31 degrees.
        double targetingAngularVelocity = getTX() * kP;

        // convert to rpm for our drive method
        // TODO Max RPM must be tuned
        targetingAngularVelocity *= Constants.Drive.MAX_WHEEL_RPM;

        //invert since tx is positive when the target is to the right of the crosshair
        targetingAngularVelocity *= -1.0;

        return targetingAngularVelocity;
    }

    public double limelight_range_proportional() {
        //TODO Tune this value
        double kP = .1;
        //TODO This can be changed to TA, the following is from limelight documentation:
        /*
        * simple proportional ranging control with Limelight's "ty" value
        * this works best if your Limelight's mount height and target mount height are different.
        * if your limelight and target are mounted at the same or similar heights, use "ta" (area) for target ranging rather than "ty"
        */

        double targetingForwardSpeed = getTY() * kP;
        targetingForwardSpeed *= Constants.Drive.MAX_WHEEL_RPM;
        targetingForwardSpeed *= -1.0;
        return targetingForwardSpeed;
    }


    public LLResult getResult() {
        if (result != null) {
            if (result.isValid()) {
                return result;
            }
        }
        return null;
    }

    public double getTX() {
        return getResult().getTx();
    }
    public double getTY() {
        return getResult().getTy();
    }

    public Pose3D getPose() {
        return getResult().getBotpose();
    }
}
