import java.io.*;
import java.util.*;

import mlp.MLP;
import mlp.datasets.Dataset;

public class Main {
    static final int TEXTS_PER_RUBRIC = 15000;
    static final int WORDS_PER_RUBRIC = 50;
    static final double SPLIT_RATIO = 0.05;

    public static void main(String[] args) throws Exception {
        int rubric = 7;
        int epoches = 200;
        double learningRate = 0.0001;
        int firstLayerNeurons = 16;

        FileReader fr = new FileReader("data/vectors/" + rubric + "rub.csv");
        Dataset test = new Dataset(fr,
                rubric * TEXTS_PER_RUBRIC,
                rubric * WORDS_PER_RUBRIC,
                rubric);
        Dataset train = test.split(SPLIT_RATIO);
        fr.close();

        System.err.println("Rubrics: " + rubric
                + "\tTrain: " + train.getSize()
                + "\tTest: " + test.getSize());

        int outputLength = train.getTargetLength();

        Random rnd = new Random();
        long seed = rnd.nextLong();
        rnd.setSeed(seed);

        // ReLU relu = new ReLU();
        // Softmax softmax = new Softmax();
        // MLP mlp = new MLP(new MSE(),
        //         new Layer(new HeInitializer(rnd, train.getInputLength()),
        //                 relu,
        //                 train.getInputLength(), firstLayerNeurons),
        //         new Layer(new HeInitializer(rnd, firstLayerNeurons),
        //                 softmax,
        //                 firstLayerNeurons, outputLength));

        // mlp.train(train, epoches, 1, learningRate);

        // // Save trained network.
        // try (FileOutputStream fos = new FileOutputStream(
        //         "data/serialize/rubricator_7rub.mlp");
        //         ObjectOutputStream oos = new ObjectOutputStream(fos);) {
        //     oos.writeObject(mlp);
        // } catch (Exception e) {
        //     throw e;
        // }

        // Load trained network.
        MLP mlp = null;
        try (FileInputStream fis = new FileInputStream(
                "data/serialize/rubricator_7rub.mlp");
                ObjectInputStream ois = new ObjectInputStream(fis);) {
            mlp = (MLP)ois.readObject();
        } catch (Exception e) {
            throw e;
        }

        Hashtable<String, Integer> ht = new Hashtable<>();
        for (int r = 0; r < rubric; r++)
            ht.put(Rubric.getNameByValue(r), 0);

        for (int i = 0; i < test.getSize(); i++) {
            var input = test.inputs[i];
            var target = test.targets[i];
            double[] output = mlp.predict(input);

            // for (int v = 0; v < target.length; v++)
            //     System.Neurons.print(String.format("%2.2f ", target[v]));
            // System.Neurons.print("| ");
            // for (int v = 0; v < output.length; v++)
            //     System.Neurons.print(String.format("%2.2f ", output[v]));
            // System.Neurons.println();

            int targeti = maxi(target);
            if (maxi(output) != targeti) {
                String key = Rubric.getNameByValue(targeti);
                ht.put(key, ht.get(key) + 1);
            }
        }

        System.out.println("SEED:\t" + seed);
        int errorSum = 0;
        System.out.println("ERRORS:");
        for (var entry : ht.entrySet()) {
            errorSum += entry.getValue();
            System.out.println(String.format("  - %-15s%-8s%.2f%%",
                    entry.getKey(), entry.getValue(),
                    (float)entry.getValue() / test.getSize() * rubric * 100));
        }
        System.err.println("Total error: " + errorSum
                + " or " + (float)errorSum / (float)test.getSize() * 100 + "%");
    }

    static int maxi(double[] array) {
        int i = 0;
        for (int j = 1; j < array.length; j++) {
            if (array[j] > array[i])
                i = j;
        }
        return i;
    }
}
