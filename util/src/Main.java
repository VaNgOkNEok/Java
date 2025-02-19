import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        DataFilter dataFilter = new DataFilter(args);

        try {
            dataFilter.filter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}