package org.example;

public class QuestRewardGenerator {
    private static final char TAG_BRONZE = 'b';
    public static final char TAG_SILVER = 's';

    public static String generateQuestRewards(int N) {
        StringBuilder rewards = new StringBuilder(N);

        for (int i = 0; i < N; i++) {
            double probability = sigmoid(normalize(i, N), 3);
            if (probability + sinusoidal(i, N) < 0.5) {
                rewards.append(TAG_BRONZE);
            } else {
                rewards.append(TAG_SILVER);
            }
        }

        return ensureControlledVariability(rewards.toString(), N);
    }

    private static double sigmoid(double x, int steepness) {
        // Sigmoid function for smoother transition
        // 0.5 splits the result roughly equally between bronze and silver. can adjust to change ratio
        return 1 / (1 + Math.exp(-steepness * (x - 0.5)));
    }

    private static double sinusoidal(int i, int N) {
        // Sinusoidal function to add periodic variability
        return 0.3 * Math.sin(10 * Math.PI * normalize(i, N));
    }

    private static String ensureControlledVariability(String input, int N) {
        StringBuilder result = new StringBuilder(N);
        int runLength = 0;
        char lastChar = '\0';
        int initialMaxRunLengthB = 10; // Initial maximum allowed consecutive 'b'
        int initialMaxRunLengthS = 1;  // Initial maximum allowed consecutive 's'

        for (int i = 0; i < N; i++) {
            char currentChar = input.charAt(i);

            if (currentChar == lastChar) {
                runLength++;
            } else {
                runLength = 1;
                lastChar = currentChar;
            }

            // Adjust the maximum run lengths dynamically using the sigmoid function
            double positionFactor = normalize(i, N);
            double positionSigmoid = sigmoid(positionFactor, 10);
            int maxRunLengthB = (int) Math.max(1, initialMaxRunLengthB * (1 - positionSigmoid)); // Decrease 'b' run length
            int maxRunLengthS = (int) Math.max(initialMaxRunLengthS, 2 + 8 * positionSigmoid); // Increase 's' run length

            // Break runs longer than maxRunLengthB or maxRunLengthS by inserting the opposite character
            if ((currentChar == TAG_BRONZE && runLength > maxRunLengthB) || (currentChar == TAG_SILVER && runLength > maxRunLengthS)) {
                currentChar = (currentChar == TAG_BRONZE) ? TAG_SILVER : TAG_BRONZE;
                runLength = 1;
                lastChar = currentChar;
            }

            result.append(currentChar);
        }

        return result.toString();
    }

    private static double normalize(int value, int itemCount) {
        return (double) value / itemCount;
    }
}
