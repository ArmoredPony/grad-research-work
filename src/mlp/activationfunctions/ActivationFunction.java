package mlp.activationfunctions;

import java.io.Serializable;
import java.util.function.UnaryOperator;

interface SerializableUnaryOperator<T> extends UnaryOperator<T>, Serializable {}

public class ActivationFunction implements Serializable {
    private SerializableUnaryOperator<double[]> act;
    private SerializableUnaryOperator<double[]> dAct;

    public ActivationFunction(
            SerializableUnaryOperator<double[]> act,
            SerializableUnaryOperator<double[]> dAct) {
        this.act = act;
        this.dAct = dAct;
    }

    public double[] f(double[] input) {
        return act.apply(input);
    }

    public double[] df(double[] input) {
        return dAct.apply(input);
    }
}
