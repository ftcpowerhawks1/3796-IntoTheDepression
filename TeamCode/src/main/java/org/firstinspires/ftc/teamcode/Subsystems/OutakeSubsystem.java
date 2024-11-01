package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingCRServo;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class OutakeSubsystem extends SDKSubsystem {
    public static final OutakeSubsystem INSTANCE = new OutakeSubsystem();
    public OutakeSubsystem() { }
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

    // hardware
    private final Cell<CachingServo> outTakePivotLeft = subsystemCell(() -> getHardwareMap().get(CachingServo.class, "intakePivotServo"));
    private final Cell<CachingServo> outTakePivotRight = subsystemCell(() -> getHardwareMap().get(CachingServo.class, "intake"));

    // set target method
    public void setPosition(double Position) {
        outTakePivotLeft.get().setPosition(Position);
        outTakePivotRight.get().setPosition(Position);
    }
    public double getPosition() {
        return(outTakePivotLeft.get().getPosition());
    }

    // init hook, to handle init config
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {

    }

    // init hook, to handle init config
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {

    }

    Sequential dump = new Sequential (
        //TODO FINISH OUTTAKE LOGIC
    );
}