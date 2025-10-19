import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class SentimentAnalysis {

    private static final String SENTIMENTS_FILE = "sentiments.txt";

    public static void main(String[] args) {
        MyHashMap<String, Integer> sentimentMap = new MyHashMap<>();

        try {
            loadSentiments(sentimentMap);
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + SENTIMENTS_FILE + " not found. Please ensure it's in the same directory.");
            return;
        }

        StringBuilder inputText = new StringBuilder();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter text:");

            String line;
            while (!(line = scanner.nextLine()).equalsIgnoreCase("END")) {
                inputText.append(line).append(" ");
            }
        }

        String[] words = inputText.toString().replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

        int totalSentiment = 0;
        int wordCount = 0;

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.isEmpty()) {
                continue;
            }
            wordCount++;

            if (i + 1 < words.length) {
                String twoWordPhrase = word + " " + words[i + 1];
                Integer phraseSentiment = sentimentMap.get(twoWordPhrase);
                if (phraseSentiment != null) {
                    totalSentiment += phraseSentiment;
                    i++; 
                    continue; 
                }
            }
            totalSentiment += sentimentMap.getOrDefault(word, 0);
        }

        double averageSentiment = (wordCount > 0) ? (double) totalSentiment / wordCount : 0.0;

        System.out.println("\nWords: " + wordCount);
        System.out.println("Sentiment: " + totalSentiment);
        System.out.printf("Overall: %.2f\n", averageSentiment);
    }

    private static void loadSentiments(MyHashMap<String, Integer> map) throws FileNotFoundException {
        try (Scanner fileScanner = new Scanner(new File(SENTIMENTS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String phrase = parts[0].trim();
                    try {
                        int sentiment = Integer.parseInt(parts[1].trim());
                        map.put(phrase, sentiment);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }
}