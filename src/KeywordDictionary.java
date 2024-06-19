import java.io.*;
import java.util.*;

public class KeywordDictionary {
    static final String[] defaultRubrics = new String[] {
            "science",
            "sport",
            "culture",
            "economics",
            "media",
            "politics",
            "society",
    };

    HashSet<String>[] sets;

    public KeywordDictionary(
            int rubricCount,
            int wordsPerRubric)
            throws Exception {
        if (rubricCount <= 0 || wordsPerRubric <= 0)
            throw new IllegalArgumentException();

        sets = new HashSet[rubricCount];
        for (int r = 0; r < rubricCount; r++) {
            sets[r] = new HashSet<>();
            String rubric = defaultRubrics[r];
            BufferedReader br = new BufferedReader(
                    new FileReader("data/keywords/" + rubric + ".txt"));
            for (int i = 0; i < wordsPerRubric; i++)
                sets[r].add(br.readLine());
            br.close();
        }
    }

    public HashSet<String> getWords(Rubric rubric) {
        return sets[rubric.value];
    }

    public HashSet<String> getWords(int value) {
        return sets[value];
    }

    public int getRubricCount() {
        return sets.length;
    }
}
