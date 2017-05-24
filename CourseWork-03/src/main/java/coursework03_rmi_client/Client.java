package coursework03_rmi_client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import coursework03_rmi_server.Compute;

public class Client {    
    
    private final CommandsInterpretator interpretator;
    private final Registry registry;
    private final Compute remoteCompute;
    
    public static boolean flag = true;
    
    public Client() throws Exception{
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        registry = LocateRegistry.getRegistry(Compute.SERVER_PORT);
        remoteCompute = (Compute) registry.lookup(Compute.SERVER_NAME);
        interpretator = new CommandsInterpretator(remoteCompute);
    }

    public void start() {
        try {            
            System.out.println("Connecting OK.\n"
                + "Welcome on server.\n"
                + "You can type \"help\" to find needed commands.\n");
            try (Scanner sc = new Scanner(System.in)) {
                while (flag) interpretator.interpretator(sc.nextLine()); //wait to commands           
            }
        } catch (Exception ex) {
            System.err.println("Problem with connection.");
        }
    }
}
