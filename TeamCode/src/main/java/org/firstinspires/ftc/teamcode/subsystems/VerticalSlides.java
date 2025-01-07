package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.util.Constants.Slides.highScoringPos;
import static org.firstinspires.ftc.teamcode.util.Constants.Slides.homePos;
import static org.firstinspires.ftc.teamcode.util.Constants.Slides.midScoringPos;
import static org.firstinspires.ftc.teamcode.util.Constants.Slides.specimenScoringPos;
import static org.firstinspires.ftc.teamcode.config.VerticalSlidesPIDConfig.SlidesD;
import static org.firstinspires.ftc.teamcode.config.VerticalSlidesPIDConfig.SlidesI;
import static org.firstinspires.ftc.teamcode.config.VerticalSlidesPIDConfig.SlidesP;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.pid.DoubleComponent;

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

public class VerticalSlides extends SDKSubsystem {
    public static final VerticalSlides INSTANCE = new VerticalSlides();
    private VerticalSlides() {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach{}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));
    private Wrapper opmodeWrapper;
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
        HIGH_SCORING,
        MID_SCORING,
        SPECIMEN_SCORING,
        HOME
    }

    public static SlideState slideState;

    //motors
    private final Cell<DcMotorEx> rightslides = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, Constants.Slides.VERTICALRIGHT));
    private final Cell<DcMotorEx> leftslides = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, Constants.Slides.VERTICALLEFT));

    //encoder
    private final Cell<EnhancedDoubleSupplier> encoder = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) leftslides.get().getCurrentPosition()));
    //current of motor
    private final Cell<EnhancedDoubleSupplier> current = subsystemCell(() -> new EnhancedDoubleSupplier(() -> leftslides.get().getCurrent(CurrentUnit.AMPS)));

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
                        leftslides.get().setPower(power);
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
        leftslides.get().setPower(-1);
    }

    public void stopSlides() {
        rightslides.get().setPower(0);
        leftslides.get().setPower(0);
        setTarget(0);
    }

    public void setSlides(SlideState slideState) {
        switch (slideState) {
            case HIGH_SCORING:
                setTarget(highScoringPos);
                break;
            case MID_SCORING:
                setTarget(midScoringPos);
                break;
            case SPECIMEN_SCORING:
                setTarget(specimenScoringPos);
                break;
            case HOME:
                setTarget(homePos);
                break;
        }
        VerticalSlides.slideState = slideState;
    }

    public double getVelocity() {
        return (this.encoder.get().velocity());
    }

    public double getCurrent() {
        return (current.get().state());
    }

    public double getCurrentChange() {
        return (current.get().velocity());
    }

    public double getEncoder() {
        return (encoder.get().state());
    }

    public boolean getControllerFinished() {
        return (controller.get().finished());
    }

    public double getVerticalTargetPos() {
        return targetPos;
    }

    public void resetEncoder() {
        rightslides.get().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        targetSupplier.reset();
    }

    //init hook
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        leftslides.get().setDirection(DcMotorSimple.Direction.REVERSE);
        leftslides.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightslides.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        opmodeWrapper = opMode;
        controller.get().setEnabled(false);
    }

    //start hook
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
        controller.get().setEnabled(true);
    }

    public Lambda setSlidePosition(double target) {
        return new Lambda("run_to_position-vertical")
                .setInit(() -> setTarget(target))
                .setFinish(() -> controller.get().finished() || opmodeWrapper.getState() == Wrapper.OpModeState.STOPPED);
    }
    public Lambda setSlidePosition(SlideState slideState) {
        return new Lambda("setSlidePosition")
                .setInit(() -> setSlides(slideState))
                .setFinish(() -> controller.get().finished() || opmodeWrapper.getState() == Wrapper.OpModeState.STOPPED);
    }
}
