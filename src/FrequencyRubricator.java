import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FrequencyRubricator {
    private KeywordDictionary kd;

    public FrequencyRubricator(KeywordDictionary kd) {
        this.kd = kd;
    }

    public int rubricate(String text) throws IOException {
        Process p = new ProcessBuilder(
                "cmd.exe",
                "/c",
                "echo",
                text,
                "|",
                "C:\\Users\\ilya\\Documents\\program\\research_work\\diploma\\util\\mystem.exe",
                "-nwl").start();
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

        int[] counters = new int[kd.getRubricCount()];
        String token;
        while ((token = br.readLine()) != null) {
            for (int i = 0; i < kd.getRubricCount(); i++) {
                if (kd.getWords(i).contains(token)) {
                    counters[i] += 1;
                }
            }
        }

        int max = 0, maxi = 0;
        for (int i = 0; i < counters.length; i++) {
            if (counters[i] > max) {
                max = counters[i];
                maxi = i;
            }
        }

        return maxi;
    }
}
