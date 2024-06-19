import java.io.*;

import com.opencsv.CSVReader;

public class NewsSeparator {
    public static void main(String[] args) throws Exception {
        if (args.length < 1)
            throw new FileNotFoundException("Provide .csv file name as first argument.");

        String csvName = args[0];
        FileReader fr = new FileReader(csvName);
        CSVReader csv = new CSVReader(fr);

        String[] rubrics = new String[] {
                "Экономика",
                "Спорт",
                "Культура",
                "Наука и техника",
                "Интернет и СМИ",
                "Политика",
                "Общество",
        };

        String[] translations = new String[] {
                "economics",
                "sport",
                "culture",
                "science",
                "media",
                "politics",
                "society",
        };

        FileWriter[] files = new FileWriter[rubrics.length];
        for (int i = 0; i < rubrics.length; i++)
            files[i] = new FileWriter("data/csv/news-" + translations[i] + ".txt");

        String[] rec;
        while ((rec = csv.readNext()) != null) {
            for (int i = 0; i < rubrics.length; i++) {
                if (rec[1].equals(rubrics[i]) || rec[2].equals(rubrics[i])) {
                    files[i].write(rec[0]);
                    files[i].write('\n');
                }
            }
        }

        fr.close();
        csv.close();
        for (int i = 0; i < rubrics.length; i++)
            files[i].close();
    }
}
