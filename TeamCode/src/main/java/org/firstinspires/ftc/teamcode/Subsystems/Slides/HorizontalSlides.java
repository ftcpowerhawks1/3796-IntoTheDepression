package org.firstinspires.ftc.teamcode.Subsystems.Slides;

import static org.firstinspires.ftc.teamcode.Util.RobotConstants.horizontalSlideD;
import static org.firstinspires.ftc.teamcode.Util.RobotConstants.horizontalSlideI;
import static org.firstinspires.ftc.teamcode.Util.RobotConstants.horizontalSlideP;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.util.controller.calculation.pid.DoubleComponent;
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
	public HorizontalSlides() { }
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
	private final Cell<DcMotorEx> left = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, "Horizontalleft"));
	private final Cell<DcMotorEx> right = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, "Horizontalright"));
	
	// encoder
	private final Cell<EnhancedDoubleSupplier> encoder = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) left.get().getCurrentPosition()));

	//Current Monitor
	private final Cell<EnhancedDoubleSupplier> current = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) left.get().getCurrent(CurrentUnit.AMPS)));

	// controller
	private double target = 0.0;
	private final CachedMotionComponentSupplier<Double> targetSupplier = new CachedMotionComponentSupplier<>(motionComponent -> {
		if (motionComponent == MotionComponents.STATE) {
			return target;
		}
		return 0.0;
	});
	private final CachedMotionComponentSupplier<Double> toleranceSupplier = new CachedMotionComponentSupplier<>(motionComponent -> {
		if (motionComponent == MotionComponents.STATE) {
			return target;
		}
		return Double.NaN;
	});
	private final Cell<DoubleController> controller = subsystemCell(() -> 
			new DoubleController(
					targetSupplier,
					encoder.get(),
					toleranceSupplier,
					(Double power) -> {
						left.get().setPower(power);
						right.get().setPower(power);
					},
					new DoubleComponent.P(MotionComponents.STATE, horizontalSlideP)
							.plus(new DoubleComponent.I(MotionComponents.STATE, horizontalSlideI, -0.1, 0.1)
									.plus(new DoubleComponent.D(MotionComponents.STATE, horizontalSlideD)) // then D
							)
			)
	);
	
	//Commands
	private void setTarget(double target) {
		controller.get().setEnabled(true);
		this.target = target;
		targetSupplier.reset();
	}

	private void retract() {
		controller.get().setEnabled(false);
		left.get().setPower(-1);
		right.get().setPower(-1);
	}

	public double getHorizontalVelocity(){
		return(this.encoder.get().rawVelocity());
	}

	public double getHorizontalCurrent(){
		return(current.get().state());
	}

	public double getHorizontalEncoder() {return(encoder.get().state());}
	public double getCurrentChange(){
		return(current.get().rawVelocity());

	}
	public void resetHorizontalEncoder() {
		left.get().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		targetSupplier.reset();

	}

	public boolean currentSpiked(){
		return(getCurrentChange()>= (double) 5);
	}
	public boolean getHorizontalControllerFinished(){return(controller.get().finished());}


	// init hook, to handle init config
	@Override
	public void preUserInitHook(@NonNull Wrapper opMode) {
		controller.get().setEnabled(false);
	}
	
	// init hook, to handle init config
	@Override
	public void preUserStartHook(@NonNull Wrapper opMode) {
		controller.get().setEnabled(true);
	}

	public Lambda runToPosition(double target) {
		return new Lambda("run-to-position-horizontal")
				.setInit(() -> setTarget(target))
				.setFinish(() -> controller.get().finished());

	}

	public Lambda retractionScript() {
		return new Lambda("retract-horizontal")
				.setInit(this::retract)
				.setFinish(this::currentSpiked);
	}

}