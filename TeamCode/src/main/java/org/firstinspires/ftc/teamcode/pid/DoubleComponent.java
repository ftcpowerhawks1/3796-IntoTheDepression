package org.firstinspires.ftc.teamcode.pid;

import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

import dev.frozenmilk.dairy.core.util.controller.calculation.ControllerCalculation;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponentSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents;

public abstract class DoubleComponent {

    private DoubleComponent() {
        // Prevent instantiation of the abstract base class
    }

    public static class P implements ControllerCalculation<Double> {
        private final MotionComponents motionComponent;
        private DoubleSupplier kPSupplier;

        public P(MotionComponents motionComponent, DoubleSupplier kP) {
            this.motionComponent = motionComponent;
            this.kPSupplier = kP;
        }

        @Override
        public void update(
            @NotNull Double accumulation,
            @NotNull MotionComponentSupplier<? extends Double> state,
            @NotNull MotionComponentSupplier<? extends Double> target,
            @NotNull MotionComponentSupplier<? extends Double> error,
            double deltaTime) {}

        @Override
        @NotNull
        public Double evaluate(
                @NotNull Double accumulation,
                @NotNull MotionComponentSupplier<? extends Double> state,
                @NotNull MotionComponentSupplier<? extends Double> target,
                @NotNull MotionComponentSupplier<? extends Double> error,
                double deltaTime) {
            double res = error.get(motionComponent) * kPSupplier.getAsDouble();
            if (Double.isNaN(res)) return accumulation;
            else return accumulation + res;
        }

        @Override
        public void reset() {}
    }

    public static class I implements ControllerCalculation<Double> {
        private final MotionComponents motionComponent;
        private final double lowerLimit;
        private final double upperLimit;
        private DoubleSupplier kISupplier;
        private double i = 0.0;

        public I(MotionComponents motionComponent, DoubleSupplier kISupplier) {
            this(motionComponent, kISupplier, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        public I(MotionComponents motionComponent, DoubleSupplier kISupplier, double lowerLimit, double upperLimit) {
            this.motionComponent = motionComponent;
            this.kISupplier = kISupplier;
            this.lowerLimit = lowerLimit;
            this.upperLimit = upperLimit;
        }

        @Override
        public void update(
                @NotNull Double accumulation,
                @NotNull MotionComponentSupplier<? extends Double> state,
                @NotNull MotionComponentSupplier<? extends Double> target,
                @NotNull MotionComponentSupplier<? extends Double> error,
                double deltaTime) {
            i += (error.get(motionComponent) / deltaTime) * kISupplier.getAsDouble();
            i = Math.max(lowerLimit, Math.min(i, upperLimit));
        }

        @Override
        public Double evaluate(
                @NotNull Double accumulation,
                @NotNull MotionComponentSupplier<? extends Double> state,
                @NotNull MotionComponentSupplier<? extends Double> target,
                @NotNull MotionComponentSupplier<? extends Double> error,
                double deltaTime) {
            update(accumulation, state, target, error, deltaTime);
            if (Double.isNaN(i)) return accumulation;
            else return accumulation + i;
        }

        @Override
        public void reset() {
            i = 0.0;
        }
    }

    public static class D implements ControllerCalculation<Double> {
        private final MotionComponents motionComponent;
        private DoubleSupplier kDSupplier;
        private double previousError = 0.0;

        public D(MotionComponents motionComponent, DoubleSupplier kDSupplier) {
            this.motionComponent = motionComponent;
            this.kDSupplier = kDSupplier;
        }

        @Override
        public void update(
                @NotNull Double accumulation,
                @NotNull MotionComponentSupplier<? extends Double> state,
                @NotNull MotionComponentSupplier<? extends Double> target,
                @NotNull MotionComponentSupplier<? extends Double> error,
                double deltaTime) {
            previousError = error.get(motionComponent);
        }

        @Override
        public Double evaluate(
                @NotNull Double accumulation,
                @NotNull MotionComponentSupplier<? extends Double> state,
                @NotNull MotionComponentSupplier<? extends Double> target,
                @NotNull MotionComponentSupplier<? extends Double> error,
                double deltaTime
        ) {
            double err = error.get(motionComponent);
            double res = ((err - previousError) / deltaTime) * kDSupplier.getAsDouble();
            previousError = err;
            if (Double.isNaN(res)) return accumulation;
            else return accumulation + res;
        }

        @Override
        public void reset() {
            previousError = 0.0;
        }
    }
}
