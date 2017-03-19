package seminar04.client.soap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import lpi.server.soap.ArgumentFault;
import lpi.server.soap.FileInfo;
import lpi.server.soap.IChatServer;
import lpi.server.soap.LoginFault;
import lpi.server.soap.Message;
import lpi.server.soap.ServerFault;

public class CommandsProcessing {
    private final IChatServer iServ;

    public CommandsProcessing(IChatServer iServ) {
        this.iServ = iServ;
    }

//    public void enterCommand(){        
//    	System.out.print("Enter command: ");
//    }

    public void ping() {
        try {
            iServ.ping();
            System.out.println("Ping OK.");
        } catch (Exception exept) {
            System.out.println("Problem with connecting.");
        }
    }
    
    public void echo(String[] comamndArr) throws RemoteException {
        try {
            if (comamndArr.length == 2) {
                System.out.println(iServ.echo(comamndArr[1]));
            } else {
                System.out.println("Bad argument.");
            }
        } catch (Exception exept) {
            System.out.println("Problem with connecting.");
        }
    }

    private final Timer timer = new Timer();
    private static String userLogin = null;
    private static String sessionID = null;

    public void login(String[] comamndArr) throws RemoteException, ArgumentFault, ServerFault, LoginFault {
        if (comamndArr.length == 3) {
            if (!comamndArr[1].equals(userLogin)) {
                if (sessionID != null) {
                    iServ.exit(sessionID);
                }
                
                sessionID = iServ.login(comamndArr[1], comamndArr[2]);

                if (userLogin == null) {
                    timer.schedule(receive, 0, 1500);
                }
                userLogin = comamndArr[1];
            }
        } else {
            System.out.println("Bad argument.");
        }
    }

    public void list() throws RemoteException, ArgumentFault, ServerFault {
        if (sessionID != null) {
            List<String> list = iServ.listUsers(sessionID);
            if (list != null) {
                for (String f : list) {
                    System.out.println(f);
                }
            }
        } else {
            System.out.println("Sign up.");
        }
    }

    public void msg(String[] comamndArr) throws RemoteException, ArgumentFault, ServerFault {
        if (sessionID == null) {
            System.out.println("Sign up.");
        } else if (comamndArr.length == 3) {
            Message messag = new Message();
            messag.setSender(userLogin);
            messag.setReceiver(comamndArr[1]);
            messag.setMessage(comamndArr[2]);

            if (isItUser(comamndArr[1])) {
                iServ.sendMessage(sessionID, messag);
                System.out.println("Message sent.");
            }
        } else {
            System.out.println("Bad argument.");
        }
    }

    public void receiveMsg() throws RemoteException, ArgumentFault, ServerFault {
        if (sessionID != null) {
            Message message = null;
            message = iServ.receiveMessage(sessionID);
            if (message != null) {
                System.out.println("Message from: " + message.getSender() + " - " + message.getMessage());
            } else if (flag) {
                System.out.println("No message.");
            }
        } else {
            System.out.println("Sign up.");
        }
    }    
    
    public void file(String[] comamndArr) throws IOException, ArgumentFault, ServerFault {
        if (sessionID == null) {
            System.out.println("Sign up.");
        } else if (comamndArr.length == 3) {
            File file = new File(comamndArr[2]);

            if (isItUser(comamndArr[1])) {
                if (file.exists()) {
                    FileInfo fileInf = new FileInfo();
                    fileInf.setSender(userLogin);
                    fileInf.setReceiver(comamndArr[1]);
                    fileInf.setFilename(comamndArr[2]);
                    fileInf.setFileContent(Files.readAllBytes(file.toPath()));
                    iServ.sendFile(sessionID, fileInf);
                    System.out.println("File sent.");
                } else {
                    System.out.println("No this file.");
                }
            }
        } else {
            System.out.println("Bad argument.");
        }
    }

    public void receiveFile() throws RemoteException, ArgumentFault, ServerFault {
        FileInfo fileInfo = null;
        if (sessionID != null) {
            fileInfo = iServ.receiveFile(sessionID);
            if (fileInfo != null) {
                System.out.println("File from: " + fileInfo.getSender() + ", file name: " + fileInfo.getFilename());
                try (FileOutputStream fos = new FileOutputStream(
                        new File(fileInfo.getSender() + "_" + fileInfo.getFilename()))) {
                    fos.write(fileInfo.getFileContent());
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

    public void exit() throws RemoteException, ArgumentFault, ServerFault {
        Client.flag = false;
        timer.cancel();
        iServ.exit(sessionID);
        System.out.println("Exit from server.");        
    }

    private boolean isItUser(String user) {
        boolean isIt;
        if (this.user.contains(user)) {
            isIt = true;
        } else {
            System.out.println("This user does not exist.");
            isIt = false;
        }
        return isIt;
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
            } catch (ArgumentFault | ServerFault ex) {
                Logger.getLogger(CommandsProcessing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    private final List<String> user = new LinkedList<>();

    private void activeUser() throws RemoteException, ArgumentFault, ServerFault {
        if (sessionID != null) {
            List<String> activeUser = new LinkedList<>();
            List<String> logedOut = new LinkedList<>();
            List<String> list = iServ.listUsers(sessionID);

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
                        logedOut.add(us);
                    }
                }
                for (String out : logedOut) {
                    user.remove(out);
                }
            }
        }
    }//activeUser
}//CommandsProcessing
