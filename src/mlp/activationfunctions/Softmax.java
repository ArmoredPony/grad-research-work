package mlp.activationfunctions;

public final class Softmax extends ActivationFunction {

    public Softmax() {
        super(
                (double[] x) -> {
                    double sum = 0;
                    for (int i = 0; i < x.length; i++)
                        sum += Math.exp(x[i]);
                    for (int i = 0; i < x.length; i++)
                        x[i] = Math.exp(x[i]) / sum;
                    return x;
                },
                (double[] x) -> {
                    for (int i = 0; i < x.length; i++)
                        x[i] = x[i] * (1 - x[i]);
                    return x;
                });
    }

}