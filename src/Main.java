import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static ArrayBlockingQueue<String> qA = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> qB = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> qC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        Random random = new Random();

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String str = generateText("abc", 100_000);

                if (str.startsWith("a")) {
                    try {
                        qA.put(str);
                    } catch (InterruptedException e) {
                        return;
                    }
                } else {
                    if (str.startsWith("b")) {
                        try {
                            qB.put(str);
                        } catch (InterruptedException e) {
                            return;
                        }
                    } else {
                        if (str.startsWith("c")) {
                            try {
                                qC.put(str);
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                    }
                }
            }
        }).start();//Поток заполняет очереди строками в зависимости от первого символа

        new Thread(() -> {
            String resultA = null;
            int size = 0;
            for (int i = 0; i < 100; i++) {
                try {
                    String s = qA.take();
                    if (isBigger(s) > size) {
                        size = isBigger(s);
                        resultA = s;
                    }
                } catch (InterruptedException e) {
                    return;
                }

            }
            System.out.println("Самая длинная строка на 'а' - "  + size + " символов: ");
        }).start(); // поток обрабатывает очередь на А

        new Thread(() -> {
            String resultB = null;
            int size = 0;
            for (int i = 0; i < 100; i++) {
                try {
                    String s = qB.take();
                    if (isBigger(s) > size) {
                        size = isBigger(s);
                        resultB = s;
                    }
                } catch (InterruptedException e) {
                    return;
                }

            }
            System.out.println("Самая длинная строка на 'b' - "  + size + " символов: " );
        }).start(); // поток обрабатывает очередь на B

        new Thread(() -> {
            String resultC = null;
            int size = 0;
            for (int i = 0; i < 100; i++) {
                try {
                    String s = qC.take();
                    if (isBigger(s) > size) {
                        size = isBigger(s);
                        resultC = s;
                    }
                } catch (InterruptedException e) {
                    return;
                }

            }
            System.out.println("Самая длинная строка на 'c' - "  + size + " символов: " );
        }).start(); // поток обрабатывает очередь на C


    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean iSPolindrom(String text) {
        boolean isP = true;
        char[] chars = text.toCharArray();
        for (int i = 0; i < (chars.length / 2); i++) {
            if (chars[i] != chars[chars.length - 1 - i]) {
                isP = false;
                break;
            }
        }
        return isP;
    }

    public static boolean isRepeat(String text) {
        boolean isR = true;
        char[] chars = text.toCharArray();
        for (int i = 1; i < (chars.length - 1); i++) {
            if (chars[0] != chars[i]) {
                isR = false;
                break;
            }
        }
        return isR;
    }

    public static boolean isAscending(String text) {
        boolean isA = true;
        char[] chars = text.toCharArray();
        Arrays.sort(chars);
        String sort = new String(chars);
        if (!(text.equals(sort))) {
            isA = false;
        }
        ;
        return isA;
    }

    public static int isBigger(String string) {
        int count = 1;
        char[] chars = string.toCharArray();
        for (int i = 1; i < (chars.length - 1); i++) {
            if (chars[0] != chars[i]) {
                break;
            } else {
                count++;
            }
        }
        return count;
    }
}