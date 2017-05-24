package coursework03_rmi_client;

public class Main {
    
    public static void main(String[] args) {        
        try {            
            Client client = new Client();
            client.start();             
        } catch (Exception ex) {
            ex.printStackTrace();
        }      
    }
}
