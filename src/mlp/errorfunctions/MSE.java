package mlp.errorfunctions;

public class MSE extends ErrorFunction {
    public MSE() {
        super(
                (double[] outputs, double[] targets) -> {
                    double[] loss = new double[outputs.length];
                    for (int i = 0; i < loss.length; i++) {
                        double e = outputs[i] - targets[i];
                        loss[i] = e * e / 2;
                    }
                    return loss;
                },
                (double[] outputs, double[] targets) -> {
                    double[] dLoss = new double[outputs.length];
                    for (int i = 0; i < dLoss.length; i++)
                        dLoss[i] = targets[i] - outputs[i];

                    return dLoss;
                });
    }
}
