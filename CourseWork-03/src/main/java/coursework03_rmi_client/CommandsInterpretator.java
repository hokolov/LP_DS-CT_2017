package coursework03_rmi_client;

import java.io.IOException;
import java.rmi.RemoteException;
import coursework03_rmi_server.Compute;

public class CommandsInterpretator {
    
    private final CommandsProcessing commandsProcessing;
    
    public CommandsInterpretator(Compute remoteCompute) {
        commandsProcessing = new CommandsProcessing(remoteCompute);
    }

    public void interpretator(String inLine) {
        try {            
            runCommand(inLine);            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final CommandsParser commandsParser = new CommandsParser();
    private void runCommand(String inputLine) throws RemoteException, IOException {
        
        String[] commandArr = commandsParser.parseCommand(inputLine);

        switch (commandArr[0]) {
            
            case "help":
                commandsProcessing.help();
                break;
                
            case "exit":
                commandsProcessing.exit();
                break;
                
            case "ping":
                commandsProcessing.ping();
                break;
                
            case "echo":
                commandsProcessing.echo(commandArr);
                break;
            
            case "sort":
                commandsProcessing.sort(commandArr);
                break;
                
            default:
                System.out.println("This command does not exist.");
                break;
        }
    }
}
