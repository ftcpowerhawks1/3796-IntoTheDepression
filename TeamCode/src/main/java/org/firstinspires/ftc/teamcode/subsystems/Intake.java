package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Intake.depositPos;
import static org.firstinspires.ftc.teamcode.Constants.Intake.intakePos;
import static org.firstinspires.ftc.teamcode.Constants.Slides.highScoringPos;
import static org.firstinspires.ftc.teamcode.Constants.Slides.homePos;
import static org.firstinspires.ftc.teamcode.Constants.Slides.midScoringPos;
import static org.firstinspires.ftc.teamcode.Constants.Slides.specimenScoringPos;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesD;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesI;
import static org.firstinspires.ftc.teamcode.config.HorizontalSlidesPIDConfig.SlidesP;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.pid.DoubleComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.cachinghardware.CachingCRServo;
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

public class Intake extends SDKSubsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() {}

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
    private final Cell<CachingCRServo> intake = subsystemCell(() -> new CachingCRServo(getHardwareMap().get(CRServo.class, Constants.Slides.VERTICALRIGHT)));
    private final Cell<CachingServo> intakePivot = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Slides.VERTICALLEFT)));

    public void setIntake(IntakeState Intakestate) {
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
        Intake.intakeState = Intakestate;
    }



    //init hook
    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
    }

    //start hook
    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
    }


    private void setIntakePivot(double target) {
        intakePivot.get().setPosition(target);
    }

    private void setIntakePower(double power) {
        intake.get().setPower(power);
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
