package mlp.errorfunctions;

import java.io.Serializable;
import java.util.function.BinaryOperator;

interface SerializableBinaryOperator<T> extends BinaryOperator<T>, Serializable {}

public class ErrorFunction implements Serializable {
    private SerializableBinaryOperator<double[]> loss;
    private SerializableBinaryOperator<double[]> dLoss;

    public ErrorFunction(SerializableBinaryOperator<double[]> loss,
            SerializableBinaryOperator<double[]> dLoss) {
        this.loss = loss;
        this.dLoss = dLoss;
    }

    public double[] getLoss(double[] inputs, double[] targets) {
        return loss.apply(inputs, targets);
    }

    public double getCost(double[] inputs, double[] targets) {
        double sum = 0;
        double[] loss = getLoss(inputs, targets);
        for (int i = 0; i < loss.length; i++)
            sum += loss[i];

        return sum / loss.length;
    }

    public double[] getLossDeriv(double[] inputs, double[] targets) {
        return dLoss.apply(inputs, targets);
    }

    public double getCostDeriv(double[] inputs, double[] targets) {
        double sum = 0;
        double[] dLoss = getLossDeriv(inputs, targets);
        for (int i = 0; i < dLoss.length; i++)
            sum += dLoss[i];

        return sum / dLoss.length;
    }
}
