package client.tcp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CommandsInterpretator {
	
    public static String sendCommand = null;

    CommandsToServer commandToSrv = new CommandsToServer();
    
    public byte[] interpretator(String inLine) throws IOException {

        String[] commandMas = parseCommand(inLine);

        switch (commandMas[0]) {
            case "ping":
                return commandToSrv.pingToServer(commandMas[0]);

            case "echo":
                return commandToSrv.echoToServer(commandMas);

            case "login":
                return commandToSrv.loginToServer(commandMas);

            case "list":
                return commandToSrv.listToServer(commandMas);

            case "msg":
                return commandToSrv.msgToServer(commandMas);

            case "receivemsg":
                return commandToSrv.receiveMsgToServer();

            default:
                System.out.println("This command does not exist.");
                return null;
        }
    }
    
    CommandsFromServer commandFromSrv = new CommandsFromServer();

    public void interpretator(byte[] serverResp) {
        if (serverResp != null) {
            try {
                switch (sendCommand) {
                    case "ping":
                        commandFromSrv.pingFromServer(serverResp);
                        break;

                    case "echo":
                        commandFromSrv.echoFromServer(serverResp);
                        break;

                    case "login":
                        commandFromSrv.loginFromServer(serverResp);                        
                        break;

                    case "list":
                        commandFromSrv.listFromServer(serverResp);
                        break;

                    case "msg":
                        commandFromSrv.msgFromServer(serverResp);                        
                        break;

                    case "receivemsg":
                        commandFromSrv.receiveMsg(serverResp);
                        break;
                        
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Problem with response interpretation.");
            }
        }
    }

    public static byte[] serialize(Object object) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(object);
            return byteStream.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] data, int offset, Class<T> clazz) throws ClassNotFoundException, IOException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(data, offset, data.length - offset);
                ObjectInputStream objectStream = new ObjectInputStream(stream)) {
            return (T) objectStream.readObject();
        }
    }

    private String[] parseCommand(String line) {
        String[] outMas = null;
        String[] parsMas = line.split(" ", 2);

        switch (parsMas[0]) {
            case "echo":
                outMas = line.split(" ", 2);                
                break;

            case "msg":
                outMas = line.split(" ", 3); 
                break;

            default:
                outMas = line.split(" ");
                break;
        }
        return outMas;
    }

}
