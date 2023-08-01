package online_ticket;

import java.util.List;
import java.util.Scanner;

abstract class TicketingSystemAbstract {
    protected static final TicketDAO ticketDAO = new TicketDAO();
    protected static final UserDAO userDAO = new UserDAO();
    protected static User currentUser;

    
    abstract void displayMenu();
    abstract void raiseTicket(Scanner sc);
    abstract void resolveTicket(Scanner sc);
    abstract void viewAllTickets();


}


public class ConsoleApp extends TicketingSystemAbstract {
    
    public static void main(String[] ddfuis) {
    	
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.print("Enter your username: ");
        String username = sc.next();
        currentUser = userDAO.getUserByUsername(username);

        if (currentUser == null) {
            System.out.println("A User with name '"+ username +"' was not found. Do you want to create a new user? (Y/N)");
            String option = sc.next();

            if ("Y".equalsIgnoreCase(option)) { 
                createUser(username);
            } else {
                System.out.println("Exiting the application.");
                return;
            }
        }

        System.out.println("Welcome, " + currentUser.getUsername() + "!");
        ConsoleApp a= new ConsoleApp();
        while (running) {
        	
            a.displayMenu();
            int choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    a.raiseTicket(sc);
                    break;
                 case 2:
                    a.resolveTicket(sc);
                    break;
                case 3:
                    a.viewAllTickets();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.println("Application closed.");
    }
    

    void displayMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Raise a ticket");
        System.out.println("2. Resolve a ticket");
        System.out.println("3. View all tickets");
        System.out.println("4. Exit Application");
        System.out.print("Please Enter your choice: ");
    }

    void raiseTicket(Scanner sc) {
        if (currentUser.isAgent()) {
            System.out.println("Agents cannot raise tickets. Only users can raise tickets.");
            return;
        }

        System.out.print("Enter the issue description: ");
        String issueDescription = sc.nextLine();
        ticketDAO.raiseTicket(currentUser.getId(), issueDescription);
    }

    void resolveTicket(Scanner sc) {
        if (!currentUser.isAgent()) {
            System.out.println("Only agents can resolve tickets.");
            return;
        }

        viewAllTickets();
        System.out.print("Enter the ticket ID to resolve: ");
        int ticketId = sc.nextInt();
        String issue=ticketDAO.issueName(ticketId);
        System.out.println("Are you sure the issue : '"+issue+"' has been solved : (Y/N)");
        String temp=sc.next();
        if(temp.equalsIgnoreCase("Y"))
        	ticketDAO.resolveTicket(ticketId);
        else
        	System.out.println("Please resolve the ticket first ,then make the entry");

    }

    void viewAllTickets() {
        List<Ticket> tickets = ticketDAO.getAllTickets();

        if (tickets.isEmpty()) {
        	System.out.println();
        	System.out.println();
        	System.out.println("No tickets found.");
            System.out.println();
            System.out.println();
            return;
        }

        System.out.println("\n===== All Tickets =====");
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    private static void createUser(String username, String role ,String password) {
        System.out.print("Enter the role (USER/AGENT): ");

        userDAO.createUser(username, role, password);
        currentUser = userDAO.getUserByUsername(username);
        System.out.println("User created successfully!");
    }
    
    private static void createUser(String username) {
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter the role (USER/AGENT): ");
        String role = sc.next().toUpperCase();

        System.out.print("Enter the password: ");
        String password = sc.next();

        createUser(username, role ,password); 
    }
}
