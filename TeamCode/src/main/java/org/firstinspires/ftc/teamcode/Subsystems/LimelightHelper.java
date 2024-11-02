package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingCRServo;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.OpModeWrapper;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
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
    }
    //TODO FINISH LIMELIGHT LOGIC

}