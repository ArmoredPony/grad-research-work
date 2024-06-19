package mlp.datasets;

import java.io.*;
import java.util.*;

public class Dataset {
    public double[][] inputs;
    public double[][] targets;
    private int size;
    private int inputLength;
    private int targetLength;

    public int getSize() {
        return size;
    }

    public int getInputLength() {
        return inputLength;
    }

    public int getTargetLength() {
        return targetLength;
    }

    public Dataset(double[][] inputs, double[][] targets) {
        if (inputs.length == 0 || targets.length == 0)
            throw new IllegalArgumentException(
                    "Both inputs and targets must have at least one vector.");

        if (inputs.length != targets.length)
            throw new IllegalArgumentException(
                    "Number of inputs must match number of targets.");

        size = inputs.length;
        inputLength = inputs[0].length;
        targetLength = targets[0].length;
        for (int i = 1; i < size; i++) {
            if (inputs[i].length != inputLength)
                throw new IllegalArgumentException(
                        "All input vectors must have same length.");

            if (targets[i].length != targetLength)
                throw new IllegalArgumentException(
                        "All target vectors must have same length.");
        }

        this.inputs = inputs;
        this.targets = targets;
    }

    public Dataset(Reader in, int size, int inputLength, int targetLength) {
        if (size <= 0 || inputLength <= 0 || targetLength <= 0)
            throw new IllegalArgumentException(
                    "Size, input length and target length values must be > 0.");

        this.size = size;
        this.inputLength = inputLength;
        this.targetLength = targetLength;

        inputs = new double[size][];
        targets = new double[size][];

        int i = 0;
        String line;
        try (BufferedReader br = new BufferedReader(in)) {
            while ((line = br.readLine()) != null) {
                double[] values = Arrays.stream(line.split(","))
                        .mapToDouble(Double::parseDouble)
                        .toArray();

                if (values.length != targetLength + inputLength)
                    throw new IllegalArgumentException("On " + (i + 1)
                            + " line, vector with length of " + values.length
                            + " was parsed, but its length should be "
                            + (inputLength + targetLength) + ".");

                inputs[i] = Arrays.copyOfRange(values, 0, inputLength);
                targets[i] = Arrays.copyOfRange(values, inputLength,
                        inputLength + targetLength);

                i += 1;
            }

            in.close();
        } catch (NumberFormatException e) {
            throw new ClassCastException(
                    "On " + (i + 1) + " line, a value cannot be cast to double.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (i != size)
            throw new IllegalArgumentException("Declared size is " + size
                    + ", but there are only " + i + " vectors in the file.");

    }

    public Dataset split(double ratio) {
        if (ratio <= 0 || ratio >= 1)
            throw new IllegalArgumentException("Ratio value must be > 0 and < 1.");

        int splitSize = (int)(size * ratio);
        if (splitSize == 0)
            splitSize = 1;
        else if (splitSize == size)
            splitSize = size - 1;

        int newSize = size - splitSize;

        int step = size / splitSize;

        double[][] newInputs = new double[newSize][];
        double[][] newTargets = new double[newSize][];
        double[][] splitInputs = new double[splitSize][];
        double[][] splitTargets = new double[splitSize][];
        int p = 0, q = 0;
        for (int i = 0; i < size; i++) {
            if ((i + 1) % step == 0) {
                splitInputs[p] = inputs[i].clone();
                splitTargets[p] = targets[i].clone();
                p++;
            } else {
                newInputs[q] = inputs[i].clone();
                newTargets[q] = targets[i].clone();
                q++;
            }
        }

        this.size = newSize;
        this.inputs = newInputs;
        this.targets = newTargets;

        return new Dataset(splitInputs, splitTargets);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(String.format("%4d: ", (i + 1)));
            for (int j = 0; j < inputLength; j++)
                sb.append(String.format(Locale.US, "%8.2f ", inputs[i][j]));
            sb.append(" |  ");
            for (int j = 0; j < targetLength; j++)
                sb.append(String.format(Locale.US, "%-6.2f ", targets[i][j]));
            sb.append("\n");
        }

        return sb.toString();
    }
}
