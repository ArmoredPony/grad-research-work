import java.io.*;

public class Stemmificator {
    private BufferedReader br;

    public Stemmificator(String text) throws IOException {
        // Process p = new ProcessBuilder(
        //         "cmd.exe",
        //         "/c",
        //         "echo",
        //         text,
        //         "|",
        //         System.getProperty("user.dir") + "\\util\\mystem.exe",
        //         "-nwl").start();
        Process p = new ProcessBuilder(
                System.getProperty("user.dir") + "\\util\\mystem.exe",
                "-nwl").start();
        var bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream(), "UTF-8"));
        bw.write(text);
        bw.flush();
        bw.close();
        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    public String nextToken() throws IOException {
        var line = br.readLine();
        return line;
    }

    public void close() throws IOException {
        br.close();
    }
}
