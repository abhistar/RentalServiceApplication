import java.util.Scanner;

public class RentalServiceApplication {
    public static void main(String[] args) {
        System.out.println("Welcome to Rental Service Application");

        Scanner input = new Scanner(System.in);
        RentalServiceExecutor serviceExecutor = new RentalServiceExecutor();

        while(true) {
            System.out.println("Enter next command: ");
            String command = input.nextLine();

            if(command.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using the application, Good Bye!");
                break;
            }
            System.out.println(serviceExecutor.execute(command));
        }
    }
}
