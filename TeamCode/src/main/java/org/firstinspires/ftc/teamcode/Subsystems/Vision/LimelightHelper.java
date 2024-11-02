package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class LimelightHelper extends SDKSubsystem {
    public static final LimelightHelper INSTANCE = new LimelightHelper();
    public LimelightHelper() { }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(HorizontalSlides.Attach.class));
    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }
    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    //Hardware
    private final Cell<Limelight3A> Limelight = subsystemCell(() -> getHardwareMap().get(Limelight3A.class, "limelight"));

    // init hook, to handle init config
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {}

    // init hook, to handle init config
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {}

    public void apriltagPipeline(){
        Limelight.get().pipelineSwitch(0);
    }
    public void yellowSamplePipeline(){
        Limelight.get().pipelineSwitch(0);
    }
    public void blueSamplePipeline(){
        Limelight.get().pipelineSwitch(0);
    }
    public void redSamplePipeline(){
        Limelight.get().pipelineSwitch(0);
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
        telemetry.setMsTransmissionInterval(11);

        if (opMode.getOpModeType()== OpModeMeta.Flavor.AUTONOMOUS) {
            yellowSamplePipeline();
        } else {
            yellowSamplePipeline();
        }
        Limelight.get().start();
    }
    //TODO FINISH LIMELIGHT LOGIC

    /**3796 Is the return value of no object**/
    public Optional<Double> getTX() {
        LLResult result = Limelight.get().getLatestResult();
        if (result != null) {
            return(Optional.of(result.getTx()));
        } else {
            return(Optional.empty());
        }
    }

    public Pose3D getBotpose() {
        LLResult result = Limelight.get().getLatestResult();
        if(result != null) {
            if (result.isValid()){
                return(result.getBotpose());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}