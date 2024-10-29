package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.teamcode.Util.RobotConstants.VericalSlideD;
import static org.firstinspires.ftc.teamcode.Util.RobotConstants.VericalSlideI;
import static org.firstinspires.ftc.teamcode.Util.RobotConstants.VericalSlideP;

import androidx.annotation.NonNull;

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
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponentSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;

public class VerticalSlides extends SDKSubsystem {
	public static final VerticalSlides INSTANCE = new VerticalSlides();
	private VerticalSlides() { }
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Inherited
	public @interface Attach{}
	private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(VerticalSlides.Attach.class));
	
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
	private final Cell<DcMotorEx> left = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, "Verticalleft"));
	private final Cell<DcMotorEx> right = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, "Verticalright"));
	
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
					new DoubleComponent.P(MotionComponents.STATE, VericalSlideP)
							.plus(new DoubleComponent.I(MotionComponents.STATE, VericalSlideI, -0.1, 0.1)
									.plus(new DoubleComponent.D(MotionComponents.STATE, VericalSlideD)) // then D
							)			)
	);
	
	// set target method
	private void setTarget(double target) {
		this.target = target;
		targetSupplier.reset();
	}

	public double getVerticalVelocity(){
		return(this.encoder.get().rawVelocity());
	}

	public double getVerticalCurrent(){
		return(current.get().state());
	}

	public double getCurrentChange(){
		return(current.get().rawVelocity());

	}
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
		return new Lambda("run_to_position")
				.setInit(() -> setTarget(target))
				.setFinish(() -> controller.get().finished());
	}

}