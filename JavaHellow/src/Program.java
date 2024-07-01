import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Введите длину последовательности Фиббоначи: ");
        int len = in.nextInt();
        System.out.print("Введите первый начальный элемент: ");
        int n1 = in.nextInt();
        System.out.print("Введите второй начальный элемент: ");
        int n2 = in.nextInt();

        int n = 0;
        while (n < len) {
            System.out.printf("%d\t", fibonachi_number(n, n1, n2));
            n++;
        }
    }

    static int fibonachi_number(int k, int n1, int n2) {
        if (k == 0)
            return n2;

        if (k % 2 == 0) return fibonachi_number(--k, n1 + n2, n2);
        else return fibonachi_number(--k, n1, n1 + n2);
    }

}