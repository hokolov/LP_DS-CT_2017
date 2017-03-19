package seminar04.client.soap;

public class CommandsParser {
    
    public String[] parseCommand(String line) {
        String[] outArr;
        String[] parseArr = line.split(" ", 2);

        switch (parseArr[0]) {
            case "echo":
                outArr = line.split(" ", 2);             
                break;
            case "msg":
                outArr = line.split(" ", 3);
                break;
            default:
                outArr = line.split(" ");
                break;
        }
        return outArr;
    }
}//CommandsParser
