package mlp.activationfunctions;

public class LeakyReLU extends ActivationFunction {
    public LeakyReLU(double a) {
        super(
                (double[] x) -> {
                    for (int i = 0; i < x.length; i++)
                        x[i] = x[i] > 0 ? x[i] : a * x[i];
                    return x;
                },
                (double[] x) -> {
                    for (int i = 0; i < x.length; i++)
                        x[i] = x[i] > 0 ? 1 : -a;
                    return x;
                });
    }
}
