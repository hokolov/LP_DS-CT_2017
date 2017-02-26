package client.tcp;

public class TwoMain {

	public static void main(String[] args) {
		
		Client client = new Client();        
        client.startClient("localhost",4321); //localhost:4321, it's default,  when server started on the same machine
        
	}
}
