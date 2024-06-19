package mlp;

import java.io.Serializable;
import java.util.Locale;

import mlp.activationfunctions.ActivationFunction;
import mlp.initiliazers.Initializer;

public class Layer implements Serializable {
    int rows;
    int cols;
    double[][] weights;
    ActivationFunction function;

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double[] act(double[] input) {
        return function.f(input);
    }

    public double[] dAct(double[] input) {
        return function.df(input);
    }

    public Layer(Initializer weightInitiliaser,
            ActivationFunction function,
            int rows, int cols) {
        if (rows <= 0)
            throw new IllegalArgumentException("Rows number must be > 0.");
        if (cols <= 0)
            throw new IllegalArgumentException("Columns number must be > 0.");

        this.rows = rows;
        this.cols = cols;
        this.function = function;
        weights = new double[rows + 1][cols];
        for (int i = 0; i < rows + 1; i++)
            for (int j = 0; j < cols; j++)
                weights[i][j] = weightInitiliaser.get();
    }

    public Layer(ActivationFunction function, int rows, int cols) {
        this(new Initializer(), function, rows, cols);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < rows + 1; i++) {
            sb.append((i == 0 ? "biases:\t" : "\t"));
            for (int j = 0; j < cols; j++)
                sb.append(String.format(Locale.US, "%6.2f ", weights[i][j]));

            sb.append("\n");
        }

        return sb.toString();
    }
}