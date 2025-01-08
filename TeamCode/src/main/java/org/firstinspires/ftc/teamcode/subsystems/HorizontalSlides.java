package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.util.Constants.Intake.intakePos;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.intakePower;
import static org.firstinspires.ftc.teamcode.util.Constants.Slides.*;
import org.firstinspires.ftc.teamcode.pid.DoubleComponent;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesD;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesI;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesP;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

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

    //Dependency Setup
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

    //Slide State Setup
    public enum SlideState {

        FULL_EXTEND {
            @Override
            public void apply() {
                HorizontalSlides.INSTANCE.setSlidePosition(fullIntakeExtend);

            }
        },
        HALF_EXTEND {
            @Override
            public void apply() {
                HorizontalSlides.INSTANCE.setSlidePosition(halfIntakeExtend);

            }
        },
        HOME {
            @Override
            public void apply() {
                HorizontalSlides.INSTANCE.setSlidePosition(homePos);
            }
        };
        public abstract void apply();
    }

    //Controller Tolerance Setup
    private double targetPos = 0.0;
    private double targetVel = 0.0;
    private double posTolerance = 5.0;
    private double velTolerance = 1.0;

    public static SlideState slideState;

    //Hardware Initialization
    private final Cell<DcMotorEx> rightslides = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, Constants.Slides.HORIZONTALRIGHT));

    //Cell Initialization
    private final Cell<EnhancedDoubleSupplier> encoder = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) rightslides.get().getCurrentPosition()));
    private final Cell<EnhancedDoubleSupplier> current = subsystemCell(() -> new EnhancedDoubleSupplier(() -> rightslides.get().getCurrent(CurrentUnit.AMPS)));
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


    //Controller Definition
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

    //Wrapper Definition
    private Wrapper opmodeWrapper;

    //Hooks
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        rightslides.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        opmodeWrapper = opMode;
        controller.get().setEnabled(false);
    }
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {


        controller.get().setEnabled(true);
    }

    //Utility Functions
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
        HorizontalSlides.slideState = slideState;
        slideState.apply();
    }

    public void resetEncoder() {
        rightslides.get().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        targetSupplier.reset();
    }

    //Return Functions
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

    public double getHorizontalTargetPos() {
        return targetPos;
    }

    //Lambda Commands
    public Lambda setSlidePosition(double target) {
        return new Lambda("set-slide-position-double-horizontal")
                .setInit(() -> setTarget(target))
                .setFinish(() -> controller.get().finished()|| opmodeWrapper.getState() == Wrapper.OpModeState.STOPPED);
    }
    public Lambda setSlidePosition(SlideState slideState) {
        return new Lambda("set-slide-position-state-horizontal")
                .setInit(() -> setSlides(slideState))
                .setFinish(() -> controller.get().finished() || opmodeWrapper.getState() == Wrapper.OpModeState.STOPPED);
    }

}
