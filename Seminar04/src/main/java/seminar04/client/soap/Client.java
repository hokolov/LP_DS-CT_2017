package seminar04.client.soap;
//
import java.util.Scanner;
import lpi.server.soap.ChatServer;
import lpi.server.soap.IChatServer;

public class Client {
    public static boolean flag = true;

    public void startClient() {
        try (Scanner sc = new Scanner(System.in)) {
            ChatServer serverWrapper = new ChatServer();
            IChatServer serverProxy = serverWrapper.getChatServerProxy();
            
            System.out.println("Connecting OK.\nWelcome on server.\nEnter command: ");
            CommandsInterpretator cInterpretator = new CommandsInterpretator(serverProxy);
            while (flag) {
                String inputLine = sc.nextLine().trim();
                if (!inputLine.equals("")) {
                    cInterpretator.Interpretator(inputLine);
                }
            }
        } catch(Exception exept){
            System.out.println("Problem with connection.");
        }
    }//startClient
}//Client