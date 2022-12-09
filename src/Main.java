import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static int amountText = 10_000;
    public static int textLength = 100_000;
    public static BlockingQueue<String> firstChar = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> secondChar = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> threeChar = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < amountText; i++) {
                String symbols = generateText("abc", textLength);
                try {
                    firstChar.put(symbols);
                    secondChar.put(symbols);
                    threeChar.put(symbols);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
        new Thread(() -> countChar('a', firstChar)).start();
        new Thread(() -> countChar('b', secondChar)).start();
        new Thread(() -> countChar('c', threeChar)).start();
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void countChar(char z, BlockingQueue<String> blockingQueue) {
        int count = 0;
        for (int i = 0; i < amountText; i++) {
            try {
                String letter = blockingQueue.take();
                for (int j = 0; j < letter.length(); j++) {
                    if (letter.charAt(j) == z)
                        count++;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }

        }
        System.out.println("Символов '" + z + " '" + count + " шт");


    }
}
