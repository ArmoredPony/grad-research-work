import java.io.*;
import java.util.*;

import com.opencsv.CSVReader;

public class RubricCounter {
    public static void main(String[] args) throws Exception {
        if (args.length < 1)
            throw new FileNotFoundException("Provide .csv file name as first argument.");

        String csvName = args[0];
        FileReader fr = new FileReader(csvName);
        CSVReader csv = new CSVReader(fr);
        Map<String, Integer> rubricCount = new HashMap<>();

        String[] rec;
        while ((rec = csv.readNext()) != null) {
            for (int i = 1; i <= 2; i++) {
                String rubric = rec[i];
                if (!rubric.isEmpty()) {
                    Integer count = rubricCount.getOrDefault(rubric, 0);
                    rubricCount.put(rubric, count + 1);
                }
            }
        }

        var list = new ArrayList<>(rubricCount.entrySet());
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(
                    Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });

        for (var entry : list)
            System.out.println(
                    String.format("%-20s%s", entry.getKey(), entry.getValue()));

        csv.close();
        fr.close();
    }
}