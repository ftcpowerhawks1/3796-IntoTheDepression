package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.util.Constants.Intake.IntakeCurrentMax;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.depositPos;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.intakePos;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.intakePower;
import static org.firstinspires.ftc.teamcode.util.Constants.Intake.outtakePower;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
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
import dev.frozenmilk.dairy.core.util.supplier.numeric.EnhancedDoubleSupplier;
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

    //Intake State Setup
    public enum IntakeState {
        INTAKING {
            @Override
            public void apply() {
                Intake.INSTANCE.setIntakePivot(intakePos);
                Intake.INSTANCE.setIntakePower(intakePower);
            }
        },
        OUTTAKING {
            @Override
            public void apply() {
                Intake.INSTANCE.setIntakePivot(depositPos);
                Intake.INSTANCE.setIntakePower(outtakePower);
            }
        },
        EXTENDED {
            @Override
            public void apply() {
                Intake.INSTANCE.setIntakePivot(intakePos);
            }
        },
        MIDPOSITION {
            @Override
            public void apply() {
                Intake.INSTANCE.setIntakePivot(0.2);
            }
        },
        RETRACTED {
            @Override
            public void apply() {
                Intake.INSTANCE.setIntakePivot(depositPos);
            }
        };

        public abstract void apply();
    }

    public static IntakeState intakeState;

    //Cells
    public Cell<EnhancedDoubleSupplier> getCurrentCell() {
        return current;
    }

    //Hardware Initialization
    private final Cell<DcMotorEx> intake = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, Constants.Intake.intake));

    private final Cell<CachingServo> intakePivot = subsystemCell(() -> new CachingServo(getHardwareMap().get(Servo.class, Constants.Intake.intakePivot)));

    private final Cell<EnhancedDoubleSupplier> current = subsystemCell(() -> new EnhancedDoubleSupplier(() -> intake.get().getCurrent(CurrentUnit.MILLIAMPS)));





//Dairy Hooks

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        setIntake(IntakeState.RETRACTED);
        setIntakePower(0);
    }

    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
    }

    private void setIntakePivot(double target) {
        intakePivot.get().setPosition(target);
    }

    private void setIntakePower(double power) {
        intake.get().setPower(power);
    }
    public void setIntake(IntakeState intakeState) {
        Intake.intakeState = intakeState;
        intakeState.apply();
    }
    //Lambda Commands

    public Lambda intake(double power) {
        return new Lambda("intake")
                .setInit(() -> setIntakePower(power));
    }
    public Lambda setPivot(double target) {
        return new Lambda("setPivot")
                .setInit(() -> setIntakePivot(target));
    }
    public Lambda setIntakePosition(IntakeState intakeState) {
        return new Lambda("setIntakePosition")
                .setInit(() -> setIntake(intakeState));
    }

    public Lambda smartIntakeCommand(double power) {
        return new Lambda("intake")
                .setInit(() -> setIntakePower(power))
                .setFinish(() -> current.get().velocity()>=IntakeCurrentMax);
    }
}
