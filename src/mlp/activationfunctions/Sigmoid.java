package mlp.activationfunctions;

public final class Sigmoid extends ActivationFunction {
    public Sigmoid() {
        super(
                (double[] x) -> {
                    for (int i = 0; i < x.length; i++)
                        x[i] = 1 / (1 + Math.exp(-x[i]));
                    return x;
                },
                (double[] x) -> {
                    for (int i = 0; i < x.length; i++)
                        x[i] = x[i] * (1 - x[i]);
                    return x;
                });
    }
}
