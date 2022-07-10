import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RentalServiceApplication {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Welcome to Rental Service Application");

        String filePath = "src/main/resources/input_file.txt";

        Scanner scanner = new Scanner(new File(filePath));
        RentalServiceExecutor serviceExecutor = new RentalServiceExecutor();

        while(scanner.hasNext()) {
            String input = scanner.nextLine();
            System.out.println("Entered input: " + input);

            System.out.println(serviceExecutor.execute(input));
        }
        scanner.close();
        System.out.println("Thank you for using the application, Good Bye!");
    }
}
