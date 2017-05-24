package coursework03_rmi_server;

import java.io.IOException;

import static coursework03_rmi_server.Compute.SERVER_PORT;

public class Main{
    public static void main(String[] args) throws IOException {

        try(Server server = new Server()) {
            server.start(SERVER_PORT);
            System.out.println("Server is started.");
            System.in.read();
        } finally{
            System.out.println("Server has been closed.");
        }
    }
}
