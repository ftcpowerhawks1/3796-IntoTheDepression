package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.util.Constants.Slides.*;
import org.firstinspires.ftc.teamcode.pid.DoubleComponent;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesD;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesI;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesP;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.util.controller.implementation.DoubleController;
import dev.frozenmilk.dairy.core.util.supplier.numeric.CachedMotionComponentSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.EnhancedDoubleSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class HorizontalSlides extends SDKSubsystem {
    public static final HorizontalSlides INSTANCE = new HorizontalSlides();
    private HorizontalSlides() {}

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

    public enum SlideState {
        FULL_EXTEND,
        HALF_EXTEND,
        HOME
    }

    public static SlideState slideState;

    //motors
    private final Cell<DcMotorEx> rightslides = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, Constants.Slides.HORIZONTALRIGHT));

    //encoder
    private final Cell<EnhancedDoubleSupplier> encoder = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) rightslides.get().getCurrentPosition()));
    //current of motor
    private final Cell<EnhancedDoubleSupplier> current = subsystemCell(() -> new EnhancedDoubleSupplier(() -> rightslides.get().getCurrent(CurrentUnit.AMPS)));

    //controller
    private double targetPos = 0.0;
    private double targetVel = 0.0;
    private double posTolerance = 50.0;
    private double velTolerance = 1.0;
    private final CachedMotionComponentSupplier<Double> targetSupplier = new CachedMotionComponentSupplier<>(motionComponent -> {
        if (motionComponent == MotionComponents.STATE) {
            return targetPos;
        }/*
        else if (motionComponent == MotionComponents.VELOCITY) {
            return targetVel;
        }*/
        return Double.NaN;
    });
    private final CachedMotionComponentSupplier<Double> toleranceSupplier = new CachedMotionComponentSupplier<>(motionComponent -> {
        if (motionComponent == MotionComponents.STATE) {
            return posTolerance;
        }
        /*else if (motionComponent == MotionComponents.VELOCITY) {
            return velTolerance;
        }*/
        return Double.NaN;
    });
    private final Cell<DoubleController> controller = subsystemCell(() ->
            new DoubleController(
                    targetSupplier,
                    encoder.get(),
                    toleranceSupplier,
                    (Double power) -> {
                        rightslides.get().setPower(power);
                    },
                    new DoubleComponent.P(MotionComponents.STATE, () -> SlidesP)
                            .plus(new DoubleComponent.I(MotionComponents.STATE, () -> SlidesI))
                            .plus(new DoubleComponent.D(MotionComponents.STATE, () -> SlidesD))
            )
    );


    //set Target method
    public void setTarget(double target) {
        controller.get().setEnabled(true);
        this.targetPos = target;
        targetSupplier.reset();
    }

    public void retract() {
        controller.get().setEnabled(false);
        rightslides.get().setPower(-1);
    }

    public void stopSlides() {
        rightslides.get().setPower(0);
        setTarget(0);
    }

    public void setSlides(SlideState slideState) {
        switch (slideState) {
            case FULL_EXTEND:
                setTarget(fullIntakeExtend);
                break;
            case HALF_EXTEND:
                setTarget(halfIntakeExtend);
                break;
            case HOME:
                setTarget(homePos);
                break;
        }
        HorizontalSlides.slideState = slideState;
    }

    public double getVelocity() {
        return (this.encoder.get().rawVelocity());
    }

    public double getCurrent() {
        return (current.get().state());
    }

    public double getCurrentChange() {
        return (current.get().rawVelocity());
    }

    public double getEncoder() {
        return (encoder.get().state());
    }

    public boolean getControllerFinished() {
        return (controller.get().finished());
    }

    public void resetEncoder() {
        rightslides.get().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        targetSupplier.reset();
    }

    //init hook
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        rightslides.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        controller.get().setEnabled(false);
    }

    //start hook
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
        controller.get().setEnabled(true);
    }

    public Lambda runToPosition(double target) {
        return new Lambda("run_to_position-horizontal")
                .setInit(() -> setTarget(target))
                .setFinish(() -> controller.get().finished());
    }
    public Lambda setSlidePosition(SlideState slideState) {
        return new Lambda("setSlidePosition")
                .setInit(() -> setSlides(slideState));
    }
}
