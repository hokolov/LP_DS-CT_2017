package client.rmi;

public class CommandsParser {

	public String[] parseCommand(String line) {
        String[] outArr;
        String[] parseArr = line.split(" ", 2);

        switch (parseArr[0]) {
            case "echo":
            	outArr = line.split(" ", 2); // command _ anyText                  
                break;

            case "msg":
            	outArr = line.split(" ", 3); // command _ destination _ messegeText 
                break;

            default:
            	outArr = line.split(" ");
                break;
        }
        return outArr;
    }
}

