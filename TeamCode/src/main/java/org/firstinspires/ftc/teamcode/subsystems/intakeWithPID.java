package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesD;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesI;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesP;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.depositPos;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.intakePos;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.pid.DoubleComponent;
import org.firstinspires.ftc.teamcode.util.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingServo;
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

public class intakeWithPID extends SDKSubsystem {
    public static final intakeWithPID INSTANCE = new intakeWithPID();
    private intakeWithPID() {}

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

    public enum IntakeState {
        INTAKING,
        OUTTAKING,
        EXTENDED,
        RETRACTED
    }
    public static IntakeState intakeState;

    //motors
    private final Cell<DcMotorEx> intake = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, Constants.Intake.intake));
    private final Cell<CachingServo> intakePivot = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Intake.intakePivot)));


    private final Cell<EnhancedDoubleSupplier> encoder = subsystemCell(() -> new EnhancedDoubleSupplier(() -> intake.get().getVelocity()));
    private final Cell<EnhancedDoubleSupplier> velocity = subsystemCell(() -> new EnhancedDoubleSupplier(() -> encoder.get().velocity()));

    //controller
    private double targetVel = 0.0;
    private double velTolerance = 1.0;
    private final CachedMotionComponentSupplier<Double> targetSupplier = new CachedMotionComponentSupplier<>(motionComponent -> {
        if (motionComponent == MotionComponents.VELOCITY) {
            return targetVel;
        }
        return Double.NaN;
    });
    private final CachedMotionComponentSupplier<Double> toleranceSupplier = new CachedMotionComponentSupplier<>(motionComponent -> {

        if (motionComponent == MotionComponents.VELOCITY) {
            return velTolerance;
        }
        return Double.NaN;
    });
    private final Cell<DoubleController> controller = subsystemCell(() ->
            new DoubleController(
                    targetSupplier,
                    velocity.get(),
                    toleranceSupplier,
                    (Double power) -> {
                        intake.get().setPower(power);
                    },
                    new DoubleComponent.P(MotionComponents.STATE, () -> SlidesP)
                            .plus(new DoubleComponent.I(MotionComponents.STATE, () -> SlidesI))
                            .plus(new DoubleComponent.D(MotionComponents.STATE, () -> SlidesD))
            )
    );

    public void setTarget(double target) {
        controller.get().setEnabled(true);
        this.targetVel = target;
        targetSupplier.reset();
    }



    public void setIntake(IntakeState Intakestate) {
        intakeWithPID.intakeState = Intakestate;
        switch (intakeState) {
            case INTAKING:
                setIntakePivot(intakePos);
                break;
            case OUTTAKING:
                setIntakePivot(depositPos);

                break;
            case EXTENDED:
                setIntakePivot(intakePos);
                break;
            case RETRACTED:
                setIntakePivot(depositPos);
                break;
        }
    }



    //init hook
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        setIntake(IntakeState.RETRACTED);
        controller.get().setEnabled(false);

    }

    //start hook
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
        controller.get().setEnabled(true);

    }


    private void setIntakePivot(double target) {
        intakePivot.get().setPosition(target);
    }

    private void setIntakePower(double power) {
        setTarget(power);
    }
    public Lambda setPivot(double target) {
        return new Lambda("setPivot")
                .setInit(() -> setIntakePivot(target));
    }
    public Lambda setIntakePosition(IntakeState intakeState) {
        return new Lambda("setIntakePosition")
                .setInit(() -> setIntake(intakeState));
    }
    public Lambda intake(double power) {
        return new Lambda("intake")
                .setInit(() -> setIntakePower(power));
    }
}
