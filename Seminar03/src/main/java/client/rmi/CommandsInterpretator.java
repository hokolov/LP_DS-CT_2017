package client.rmi;

import lpi.server.rmi.IServer;

public class CommandsInterpretator {

    private final CommandsProcessing commandsProcessing;
    private final CommandsParser commandsParser = new CommandsParser(); 

    public CommandsInterpretator(IServer ob) {
    	commandsProcessing = new CommandsProcessing(ob);        
    }
    
    public void interpretator(String inLine){

        String[] comandArr = commandsParser.parseCommand(inLine);
        
        try {
            switch (comandArr[0]) {

                case "ping":
                	commandsProcessing.ping();
                    break;

                case "echo":
                	commandsProcessing.echo(comandArr);
                    break;

                case "login":
                	commandsProcessing.login(comandArr);
                    break;

                case "list":
                	commandsProcessing.list();
                    break;

                case "msg":
                	commandsProcessing.msg(comandArr);
                    break;

                case "file":
                	commandsProcessing.file(comandArr);
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
        } catch (Exception exept) {
            System.out.println("Problem with interpretator.");
            exept.printStackTrace();
        }
    }
}