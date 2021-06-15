import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GameOfLife {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Если используете терминал Windows, введите 1\nЕсли Linux, то 2");
        int osChoice = scanner.nextInt();
        System.out.println("Введите цифру 1, если хотите посмотреть на планер.\nИли введите любую другую цифру, и плоскость заполнится хаотично.");
        int choice = scanner.nextInt();
        System.out.println("Введите высоту поля:");
        int height = scanner.nextInt();
        System.out.println("Введите ширину поля:");
        int width = scanner.nextInt();
        byte[][] currentFrame = new byte[height][width];
        byte[][] newFrame = new byte[height][width];

        if(choice == 1) {
            // Показать планер
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    currentFrame[i][j] = 0;
                }
            }
            currentFrame[0][2] = 1;
            currentFrame[2][2] = 1;
            currentFrame[2][3] = 1;
            currentFrame[1][3] = 1;
            currentFrame[1][4] = 1;
        } else {
            // Хаотичное заполнение
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        currentFrame[i][j] = (byte) ThreadLocalRandom.current().nextInt(0, 2);
                    }
                }
        }

        while (true) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    // Поправляем координаты, чтобы обеспечить плоскость тора
                    int iDown = (i-1+height)%height;
                    int jDown = (j-1+width)%width;
                    int iUp = (i+1+height)%height;
                    int jUp = (j+1+width)%width;

                    // Считаем, сколько у клетки соседей
                    int neighboursCount = currentFrame[iDown][jDown] + currentFrame[iDown][j] + currentFrame[iDown][jUp] + currentFrame[i][jDown] + currentFrame[i][jUp] + currentFrame[iUp][jDown] + currentFrame[iUp][j] + currentFrame[iUp][jUp];

                    // Заполняем ячейку массива для нового кадра
                    if (currentFrame[i][j] == 0 && neighboursCount == 3) {
                        newFrame[i][j] = 1;
                    } else if (currentFrame[i][j] == 1 && (neighboursCount == 2 || neighboursCount == 3)) {
                        newFrame[i][j] = 1;
                    } else {
                        newFrame[i][j] = 0;
                    }

                    // Формируем строку для текущего кадра
                    if (currentFrame[i][j] == 0)
                        str.append(' ');
                    else
                        str.append('O');
                }
                str.append('\n');
            }

            // Готовим массив нового кадра к следующей итерации
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    currentFrame[i][j] = newFrame[i][j];
                }
            }

            // Чистим консоль и выводим текущий кадр
            cls(osChoice);
            System.out.println(str);

            // Частота обновления кадров
            TimeUnit.MILLISECONDS.sleep(300);
        }
    }

    // Очистка консоли терминала
    public static void cls(int osChoice) throws InterruptedException, IOException {
        if(osChoice == 1) {
            // Windows
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            // Linux
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}