package org.firstinspires.ftc.teamcode.util.customControllers;

public class kalmanFilter {
    double x=0; //initial state
    double Q = 0.1; //model covariance
    double R = 0.4; //sensor covariance
    double p = 1; // initial covariance guess
    double k = 1; // initial kalman gain guess

    double x_previous = x;
    double p_previous = p;
    double u = 0;
    double z = 0;

    public kalmanFilter(double x, double Q, double R, double p, double k, double u, double z) {
        this.x = x;
        this.Q = Q;
        this.R = R;
        this.p = p;
        this.k = k;
        this.u = u;
        this.z = z;
        this.x_previous = x;
        this.p_previous = p;
    }

    public void update(double Firstinput, double Secondinput) {
        u = Firstinput;
        x=x_previous+u;
        p=p_previous+Q;
        k=p/(p+R);
        if (!Double.isNaN(Secondinput)) {
            z = Secondinput;
            x=x+k*(z-x);
            p=(1-k)*p;
        }
        x_previous=x;
        p_previous=p;


    }

}
