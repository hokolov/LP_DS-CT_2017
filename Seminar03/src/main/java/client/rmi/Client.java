package client.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import lpi.server.rmi.IServer;

public class Client {

    public static boolean flag = true;

    public void startClient(int port) {

        try (Scanner sc = new Scanner(System.in)) {

            Registry reg = LocateRegistry.getRegistry(port);
            IServer proxy = (IServer)reg.lookup(IServer.RMI_SERVER_NAME);
            
            System.out.print("Connecting OK.\nWelcome on server.\nEnter command: ");
            CommandsInterpretator interpret = new CommandsInterpretator(proxy);
            
            while (flag) {
                String inpLine = sc.nextLine().trim();
                
                if (!inpLine.equals("")) {
                    interpret.interpretator(inpLine);
                }
            }

        } catch (RemoteException | NotBoundException exept) {
            System.out.println("Problem with connection.");
        }
    }
}