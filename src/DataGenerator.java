import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DataGenerator {
    static final int TEXTS_PER_RUBRIC = 15000;
    static final int WORDS_PER_RUBRIC = 50;

    public static void main(String[] args) throws Exception {
        for (int rc = 7; rc <= KeywordDictionary.defaultRubrics.length; rc++) {
            // String[] wordVector = new String[rc * WORDS_PER_RUBRIC];
            // KeywordDictionary kd = new KeywordDictionary(rc, WORDS_PER_RUBRIC);
            // int i = 0;
            // for (var set : kd.sets) {
            //     for (var word : set) {
            //         wordVector[i] = word;
            //         i += 1;
            //     }
            // }
            // Arrays.sort(wordVector);

            // Save word vector.
            // try (FileOutputStream fos = new FileOutputStream(
            //         "data/serialize/words_sorted_" + rc + "rub.vec");
            //         ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            //     oos.writeObject(wordVector);
            // } catch (Exception e) {
            // }

            // Load word vector.
            String[] wordVector = null;
            try (FileInputStream fos = new FileInputStream(
                    "data/serialize/words_sorted_" + rc + "rub.vec");
                    ObjectInputStream ois = new ObjectInputStream(fos);) {
                wordVector = (String[])ois.readObject();
            } catch (Exception e) {
            }

            FileWriter out = new FileWriter("data/vectors/" + rc + "rub.csv");
            for (int r = 0; r < rc; r++) {
                BufferedReader in = new BufferedReader(
                        new FileReader("data/news/" + Rubric.getNameByValue(r) + ".txt"));
                for (int t = 0; t < TEXTS_PER_RUBRIC; t++) {
                    Stemmificator s = new Stemmificator(in.readLine());
                    int[] vector = new int[wordVector.length + rc];
                    String token;
                    while ((token = s.nextToken()) != null) {
                        for (int w = 0; w < wordVector.length; w++)
                            if (token.equals(wordVector[w]))
                                vector[w] += 1;
                    }
                    s.close();

                    vector[vector.length - rc + r] = 1;

                    out.write(Arrays.stream(vector)
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining(",")));
                    out.write('\n');
                    out.flush();

                    if (t % 1000 == 0)
                        System.out.println(rc + ":\t" + t);
                }
                in.close();
            }
            out.close();

            break;
        }
    }
}
