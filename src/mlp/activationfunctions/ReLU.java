package mlp.activationfunctions;

public class ReLU extends ActivationFunction {
    public ReLU() {
        super(
                (double[] x) -> {
                    for (int i = 0; i < x.length; i++)
                        x[i] = x[i] > 0 ? x[i] : 0;
                    return x;
                },
                (double[] x) -> {
                    for (int i = 0; i < x.length; i++)
                        x[i] = x[i] > 0 ? 1 : 0;
                    return x;
                });
    }
}
