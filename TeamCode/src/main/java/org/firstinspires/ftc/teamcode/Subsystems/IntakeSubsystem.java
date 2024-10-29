package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class IntakeSubsystem extends SDKSubsystem {
    public static final IntakeSubsystem INSTANCE = new IntakeSubsystem();
    public IntakeSubsystem() { }
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
    private final Cell<CachingServo> intakePivot = subsystemCell(() -> getHardwareMap().get(CachingServo.class, "intakePivotServo"));
    private final Cell<CachingServo> right = subsystemCell(() -> getHardwareMap().get(CachingServo.class, "intake"));


    // set target method
    public void setPosition(double Position) {
         intakePivot.get().setPosition(Position);
    }

    // init hook, to handle init config
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {

    }

    // init hook, to handle init config
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {

    }

}