package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.util.Constants.Intake.depositPos;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.intakePos;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.intakePower;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.outtakePower;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Constants;

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
    private final Cell<CachingCRServo> intake = subsystemCell(() -> new CachingCRServo(getHardwareMap().get(CRServo.class, Constants.Intake.intake)));
    private final Cell<CachingServo> intakePivot = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Intake.intakePivot)));

    public void setIntake(IntakeState Intakestate) {
        Intake.intakeState = Intakestate;
        switch (intakeState) {
            case INTAKING:
                setIntakePivot(intakePos);
                setIntakePower(intakePower);
                break;
            case OUTTAKING:
                setIntakePivot(depositPos);
                setIntakePower(outtakePower);

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
