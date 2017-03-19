package seminar04.client.soap;
//
import java.io.IOException;
import lpi.server.soap.ArgumentFault;
import lpi.server.soap.IChatServer;
import lpi.server.soap.LoginFault;
import lpi.server.soap.ServerFault;

public class CommandsInterpretator {
    private final CommandsProcessing commandsProcessing;
    private final CommandsParser commandsParser = new CommandsParser(); 
    
    public CommandsInterpretator(IChatServer ob) {
        commandsProcessing = new CommandsProcessing(ob);        
    }
    
    public void Interpretator(String inputLine){
        String[] commandArr = commandsParser.parseCommand(inputLine);

        try {
            switch (commandArr[0]) {
                case "ping":
                    commandsProcessing.ping();
                    break;
                case "echo":
                    commandsProcessing.echo(commandArr);
                    break;
                case "login":
                    commandsProcessing.login(commandArr);
                    break;
                case "list":
                    commandsProcessing.list();
                    break;
                case "msg":
                    commandsProcessing.msg(commandArr);
                    break;
                case "file":
                    commandsProcessing.file(commandArr);
                    break;
                case "receivemsg":
                    commandsProcessing.receiveMsg();
                    break;
                case "receivefile":
                    commandsProcessing.receiveFile();
                    break;
                case "exit":
                    commandsProcessing.exit();
                    break;
                default:
                    System.out.println("This command does not exist.");
                    break;
            }
        } catch (ArgumentFault | ServerFault | LoginFault | IOException ex) {
            System.out.println("Problem with interpretator.");
        }
    }
}//CommandsInterpretator