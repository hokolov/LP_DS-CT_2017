package client.rmi;

public class ThreeMain {
    
    public static void main(String[] args) {
        
        Client client = new Client();        
        client.startClient(4321); //4321, it's default port
	
    }
}
