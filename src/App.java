import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class App {
    static AppConfiguration appCfg = null;

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Текстовый рубрикатор");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea textArea = new JTextArea(30, 80);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton loadModelButton = new JButton(
                "<html><center>Загрузить<br>модель</center></html>");
        loadModelButton.setHorizontalAlignment(SwingConstants.CENTER);
        loadModelButton.setMinimumSize(new Dimension(130, 50));
        loadModelButton.setMaximumSize(new Dimension(130, 50));
        loadModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("./configs");
                fileChooser.setFileFilter(new FileNameExtensionFilter(
                        "Rubricator configuration file", "appcfg"));
                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(file))) {
                        appCfg = (AppConfiguration)ois.readObject();
                        JOptionPane.showMessageDialog(
                                frame,
                                "Можно приступать к рубрикации",
                                "Модель загружена",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(
                                frame,
                                "Указанный файл не был найден",
                                "Файл не найден!",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (ClassNotFoundException | IOException ex) {
                        JOptionPane.showMessageDialog(
                                frame,
                                "Файл конфигурации недействителен или поврежден"
                                        + " и не может быть загружен",
                                "Ошибка загрузки!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JButton rubricateButton = new JButton(
                "<html><center>Рубрицировать<br>текст</center></html>");
        rubricateButton.setHorizontalAlignment(SwingConstants.CENTER);
        rubricateButton.setMinimumSize(new Dimension(130, 50));
        rubricateButton.setMaximumSize(new Dimension(130, 50));
        rubricateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (appCfg == null) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Для рубрикации текста необходимо загрузить конфигурацию",
                            "Загрузите конфигурацию!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String text = textArea.getText().trim().replace('\n', ' ');
                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Введите рубрицируемый текст в текстовое поле",
                            "Введите текст!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String rubricName = rubricateText(text, appCfg);
                if (rubricName == null) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Не удалось рубрицировать текст",
                            "Ошибка рубрикации!",
                            JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Предполагаемая рубрика: " + rubricName,
                            "Текст рубрицирован",
                            JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        eastPanel.add(loadModelButton);
        eastPanel.add(rubricateButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(eastPanel, BorderLayout.EAST);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    static String rubricateText(String text, AppConfiguration appCfg) {
        // Stemmify and encode text
        double[] wordsCounted = new double[appCfg.wordVectorSorted.length];
        try {
            Stemmificator s = new Stemmificator(text);
            String token;
            while ((token = s.nextToken()) != null) {
                for (int w = 0; w < appCfg.wordVectorSorted.length; w++)
                    if (token.equals(appCfg.wordVectorSorted[w]))
                        wordsCounted[w] += 1;
            }
            s.close();
        } catch (IOException e) {
        }

        // Fit resulting vector into the model
        double[] rubricProbabilities = appCfg.model.predict(wordsCounted);

        // Find index of max value
        int maxIdx = 0;
        for (int i = 0; i < rubricProbabilities.length; i++) {
            maxIdx = rubricProbabilities[i] > rubricProbabilities[maxIdx] ? i : maxIdx;
        }

        // Return rubric by found index
        return appCfg.rubricNames[maxIdx];
    }
}