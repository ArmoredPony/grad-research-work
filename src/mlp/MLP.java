package mlp;

import java.io.Serializable;

import mlp.datasets.Dataset;
import mlp.errorfunctions.ErrorFunction;

public class MLP implements Serializable {
    Layer[] layers;
    ErrorFunction errorFunction;

    public MLP(ErrorFunction errorFunction, Layer... layers) {
        if (layers.length == 0)
            throw new IllegalArgumentException("MLP must have at least one layer.");

        if (layers.length > 1)
            for (int i = 1; i < layers.length; i++) {
                int cols = layers[i - 1].getCols();
                int rows = layers[i].getRows();
                if (cols != rows)
                    throw new IllegalArgumentException("Columns number (" + cols + ") of "
                            + (i - 1) + " layer doesn't match rows number (" + rows
                            + ") of " + i + " layer.");
            }

        this.errorFunction = errorFunction;
        this.layers = layers;
    }

    public double[] predict(double[] input) {
        if (input.length != layers[0].rows)
            throw new IllegalArgumentException(
                    "Input vector length must match first layer weights rows count.");

        double[] output = new double[0];
        for (int l = 0; l < layers.length; l++) {
            double[] lastInput = (l == 0 ? input : output);
            output = layers[l].weights[0].clone();
            for (int j = 0; j < layers[l].cols; j++)
                for (int i = 0; i < layers[l].rows; i++)
                    output[j] += lastInput[i] * layers[l].weights[i + 1][j];

            output = layers[l].act(output);
        }

        return output;
    }

    public void train(Dataset dataset, int epoches, int batchSize, double rate) {
        if (dataset.getTargetLength() != layers[layers.length - 1].cols)
            throw new IllegalArgumentException(
                    "Target vector length must match columns number.");

        if (epoches <= 0)
            throw new IllegalArgumentException("Epoches value must be > 0.");

        if (batchSize <= 0)
            throw new IllegalArgumentException("Batch size value must be > 0.");

        if (rate <= 0)
            throw new IllegalArgumentException("Rate must be > 0.");

        double[] metrics = new double[epoches];

        double[][] savedVectors = new double[layers.length + 1][];
        double[][] errors = new double[layers.length][];
        double[][] gradients = new double[layers.length][];
        for (int l = 0; l < layers.length; l++) {
            errors[l] = new double[layers[l].cols];
            gradients[l] = new double[layers[l].cols];
        }

        for (int e = 0; e < epoches; e++)
            for (int d = 0; d < dataset.getSize(); d++) {
                // Calculating and storing input, intermidiate and final outputs.
                savedVectors[0] = dataset.inputs[d];
                for (int l = 0; l < layers.length; l++) {
                    savedVectors[l + 1] = layers[l].weights[0].clone();
                    for (int j = 0; j < layers[l].cols; j++)
                        for (int i = 0; i < layers[l].rows; i++)
                            savedVectors[l + 1][j] += savedVectors[l][i]
                                    * layers[l].weights[i + 1][j];

                    savedVectors[l + 1] = layers[l].act(savedVectors[l + 1]);
                }

                // Calculating errors and updating gradients.
                errors[errors.length - 1] = errorFunction.getLossDeriv(
                        savedVectors[savedVectors.length - 1],
                        dataset.targets[d]);
                for (int l = layers.length - 1; l >= 0; l--) {
                    if (l > 0)
                        for (int i = 0; i < errors[l - 1].length; i++) {
                            errors[l - 1][i] = 0;
                            for (int j = 0; j < errors[l].length; j++)
                                errors[l - 1][i] += layers[l].weights[i + 1][j]
                                        * errors[l][j];
                        }

                    double[] dActOutput = layers[l].dAct(savedVectors[l + 1]);

                    for (int i = 0; i < gradients[l].length; i++)
                        gradients[l][i] += rate * errors[l][i] * dActOutput[i];
                }

                // When mini-batch is fed, updating weights.
                if ((d + 1) % batchSize == 0 || d == dataset.getSize() - 1)
                    for (int l = 0; l < layers.length; l++)
                        for (int j = 0; j < layers[l].cols; j++) {
                            layers[l].weights[0][j] += gradients[l][j] / batchSize;
                            for (int i = 0; i < layers[l].rows; i++)
                                layers[l].weights[i + 1][j] += gradients[l][j]
                                        * savedVectors[l][i] / batchSize;

                            gradients[l][j] = 0;
                        }

                // Calculating epoch final error.
                if (d == dataset.getSize() - 1)
                    metrics[e] = errorFunction.getCost(
                            savedVectors[savedVectors.length - 1],
                            dataset.targets[d]);
            }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("MLP with " + layers.length + " layers: ");
        for (Layer layer : layers)
            sb.append(layer.getRows() + "x" + layer.getCols() + " ");
        sb.append("\n");

        for (int i = 0; i < layers.length; i++)
            sb.append("Weights " + i + "\n" + layers[i]);

        return sb.toString();
    }
}
