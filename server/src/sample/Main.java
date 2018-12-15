package sample;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class    Main {
    private void startServer(DBServerInterface stub) {

        try {
            Scanner scanner=new Scanner(System.in);
            System.out.println("Geef poortnummer: ");
            int poortnummer=scanner.nextInt();
// create on port 1099

            Registry registry = LocateRegistry.createRegistry(poortnummer);

// create a new service named CounterService

            registry.rebind("Login", new CounterImpl(stub));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("system is ready");
    }
    public static void main(String[] args) {
        Main main = new Main();
        DBServerInterface stub = main.connectToDatabase();
        main.startServer(stub);
    }

    private DBServerInterface connectToDatabase() {
        DBServerInterface stub = null;
        try {
            // fire to localhost port 1099
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 1098);

            // search for CounterService
            stub = (DBServerInterface) myRegistry.lookup("DBService");

            // call server's method

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("connected with database server");
        return stub;
    }
}