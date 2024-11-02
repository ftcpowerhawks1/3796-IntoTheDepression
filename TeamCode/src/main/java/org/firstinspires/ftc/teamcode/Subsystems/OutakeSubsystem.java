package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Subsystems.Slides.HorizontalSlides;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
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
    private final Cell<CachingServo> outTakePivotLeft = subsystemCell(() -> getHardwareMap().get(CachingServo.class, "LeftOuttakeServo"));
    private final Cell<CachingServo> outTakePivotRight = subsystemCell(() -> getHardwareMap().get(CachingServo.class, "RightOuttakeServo"));

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

    public Lambda dump() {
        return new Lambda("dump")
                .setInit(() -> setPosition(0.6));
    }
    public Lambda reset() {
        return new Lambda("reset")
                .setInit(() -> setPosition(0));
    }


}