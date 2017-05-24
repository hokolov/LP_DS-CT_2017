package coursework03_rmi_client;

public class CommandsParser {

    public String[] parseCommand(String line) {
        String[] parseArray = line.split(" ", 2);
        return (parseArray[0].equals("echo")) ? parseArray : line.split(" ");
    }
}
