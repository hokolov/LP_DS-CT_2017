package client.rmi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import lpi.server.rmi.IServer;

public class CommandsProcessing {

    private final IServer iServ;

    public CommandsProcessing(IServer iServ) {
        this.iServ = iServ;
    }
    
//    public void enterCommand(){        
//    	System.out.print("Enter command: ");
//    }

    public void ping(){        
        try {
            iServ.ping();
            System.out.println("Ping OK.");
        } catch (Exception exept) {
            System.out.println("Problem with connecting.");
        }
    }

    public void echo(String[] commandArr){
    	try {
    		if (commandArr.length == 2) {
                System.out.println(iServ.echo(commandArr[1]));
            } else {
                System.out.println("Bad argument.");
            }
        } catch (Exception exept) {
            System.out.println("Problem with connecting.");
        }
    }

    private final Timer timer = new Timer();
    private static String userLogin = null;
    private static String sessionId = null;

    public void login(String[] commandArr) throws RemoteException {
        if (commandArr.length == 3) {

            if (!commandArr[1].equals(userLogin)) {

                if (sessionId != null) {
                    iServ.exit(sessionId);
                }

                sessionId = iServ.login(commandArr[1], commandArr[2]);

                if (userLogin == null) {
                    timer.schedule(receive, 0, 1500);
                }
                userLogin = commandArr[1];
            }
        } else {
            System.out.println("Bad argument.");
        }
    }

    public void list() throws RemoteException {

        if (sessionId != null) {
            String[] list = iServ.listUsers(sessionId);
            if (list != null) {
                for (String f : list) {
                    System.out.println(f);
                }
            }
        } else {
            System.out.println("Sign up.");
        }
    }

    public void msg(String[] commandArr) throws RemoteException {
        if (sessionId == null) {
            System.out.println("Sign up.\n");
        } else if (commandArr.length == 3) {

            if (isItUser(commandArr[1])) {
                iServ.sendMessage(sessionId, new IServer.Message(commandArr[1], userLogin, commandArr[2]));
                System.out.println("Message sent.");
            }

        } else {
            System.out.println("Bad argument.");
        }

    }

    public void file(String[] commandArr) throws IOException {

        if (sessionId == null) {
            System.out.println("Sign up.");
        } else if (commandArr.length == 3) {
            File fil = new File(commandArr[2]);

            if (isItUser(commandArr[1])) {
                if (fil.exists()) {
                    iServ.sendFile(sessionId, new IServer.FileInfo(commandArr[1], fil));
                    System.out.println("File sent.");
                } else {
                    System.out.println("No this file.");
                }
            }
        } else {
            System.out.println("Bad argument.");
        }
    }

    public void receiveMsg() throws RemoteException {
        if (sessionId != null) {
            IServer.Message message = null;
            message = iServ.receiveMessage(sessionId);
            if (message != null) {
                System.out.println("Message from: " + message.getSender() + " : " + message.getMessage());
            } else if (flag) {
                System.out.println("No message.");
            }
        } else {
            System.out.println("Sign up.");
        }
    }

    public void receiveFile() throws RemoteException {
        IServer.FileInfo file = null;
        
        if (sessionId != null) {
            file = iServ.receiveFile(sessionId);
            
            if (file != null) {
                System.out.println(file.toString());

                try (FileOutputStream fos = new FileOutputStream(
                        new File(file.getSender() + "_" + file.getFilename()))
                        ) {
                    fos.write(file.getFileContent());
                } catch (Exception ex) {
                    System.out.println("Problem with write file.");
                }
            } else if (flag) {
                System.out.println("No file.");
            }
        } else {
            System.out.println("Sign up.");
        }
    }

    public void exit() throws RemoteException {
        timer.cancel();
        iServ.exit(sessionId);
        System.out.println("Exit from server.");
        Client.flag = false;
    }

    private boolean isItUser(String user) {
        boolean isItUsr;
        if (this.user.contains(user)) {
            isItUsr = true;
        } else {
            System.out.println("This user does not exist.");
            isItUsr = false;
        }
        return isItUsr;
    }

    private boolean flag;

    TimerTask receive = new TimerTask() {

        @Override
        public void run() {
            try {

                flag = false;
                receiveMsg();
                receiveFile();
                activeUser();
                flag = true;

            } catch (RemoteException ex) {
                System.out.println("Problem with timer task.");
            }
        }
    };

    private final List<String> user = new LinkedList<>();

    private void activeUser() throws RemoteException {
        if (sessionId != null) {
            List<String> activeUser = new LinkedList<>();
            List<String> loggedOut = new LinkedList<>();

            String[] list = iServ.listUsers(sessionId);

            if (list != null) {

                for (String f : list) {
                    activeUser.add(f);
                    if (!user.contains(f)) {
                        user.add(f);
                        System.out.println(f + " logged in.");
                    }
                }

                for (String us : user) {
                    if (!activeUser.contains(us)) {
                        System.out.println(us + " logged out.");
                        loggedOut.add(us);
                    }
                }
                for (String out : loggedOut) {
                    user.remove(out);
                }
            }
        }
    }

}

