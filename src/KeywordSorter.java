import java.io.*;
import java.util.*;

public class KeywordSorter {
    public static void main(String[] args) throws Exception {
        HashSet<String> stopwords = new HashSet<>();
        String[] filters = new String[] {"names", "stopwords"};
        for (var f : filters) {
            BufferedReader br = new BufferedReader(
                    new FileReader("data/filters/" + f + ".filter"));
            String line;
            while ((line = br.readLine()) != null) {
                stopwords.add(line);
            }
            br.close();
        }

        String[] rubrics = new String[] {
                "economics",
                "sport",
                "culture",
                "science",
                "media",
                "politics",
                "society",
        };

        for (var rubric : rubrics) {
            HashMap<String, Integer> wordBag = new HashMap<>();
            Process p = Runtime.getRuntime().exec(new String[] {
                    "util/mystem.exe",
                    "-nwl",
                    "data/csv/news-" + rubric + ".txt"
            });
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String word;
            while ((word = br.readLine()) != null) {
                if (word.contains("??"))
                    continue;
                if (word.contains("|"))
                    word = word.split("|", 2)[0];
                if (stopwords.contains(word)
                        || word.length() <= 3
                        || stopwords.contains(word))
                    continue;
                Integer count = wordBag.getOrDefault(word, 0);
                wordBag.put(word, count + 1);
            }
            br.close();

            var list = new ArrayList<>(wordBag.entrySet());
            list.sort(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(
                        Map.Entry<String, Integer> o1,
                        Map.Entry<String, Integer> o2) {
                    return Integer.compare(o2.getValue(), o1.getValue());
                }
            });

            FileWriter fw = new FileWriter("data/csv/keywords-" + rubric + ".txt");
            for (var entry : list) {
                fw.write(entry.getKey());
                fw.write('\n');
            }
            fw.close();

            System.out.println(rubric);
        }
    }
}
