package coursework03_rmi_server;

import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Compute, Closeable {

    private Registry registry;
    private Compute stub;

    private int serverPort;

    public void start(int port) {
        
        this.serverPort = port;
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            setRegistry();
        } catch (Exception ex) {
            log("Problem with start server.");
        }
    }

    private void setRegistry() throws Exception{
        registry = LocateRegistry.createRegistry(serverPort);
        stub = (Compute) UnicastRemoteObject.exportObject(this, serverPort);
        registry.rebind(Compute.SERVER_NAME, stub);
    }
    
    @Override
    public void close() throws IOException {
        closeRegistry();
        closeStub();
    }

    private void closeRegistry() throws IOException{
        if (registry != null) {
            try {
                registry.unbind(SERVER_NAME);
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
            registry = null;
        }
    }

    private void closeStub() throws IOException{
        if (stub != null) {
            UnicastRemoteObject.unexportObject(this, true);
            stub = null;
        }
    }

    @Override
    public String ping() throws RemoteException {
        log("Sending response on ping.");
        return "pong";
    }

    @Override
    public String echo(String echo) throws RemoteException {
        log("Sending response on echo.");
        return "ECHO: " + echo;
    }

    @Override
    public <T> T executeTask(Task<T> t) throws IOException, RemoteException {
        log("Starting sorting.");
        return t.execute();
    }
    
    private static void log(String logMsg){
        System.out.println(logMsg);
    }
}

