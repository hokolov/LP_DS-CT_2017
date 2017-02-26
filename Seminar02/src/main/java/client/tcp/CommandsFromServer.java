package client.tcp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static client.tcp.CommandsInterpretator.deserialize;


public class CommandsFromServer {
	// Commands
	private static final byte PING_RESPONSE = 2;
    private static final byte LOGIN_RESPONSE_REG_OK = 6;
    private static final byte LOGIN_RESPONSE_LOGIN_OK = 7;
    private static final byte MSG_RESPONSE_SENT = 16;
    private static final byte FILE_RESPONSE_SENT = 21;
    private static final byte RECEIVE_MSG_RESPONSE_NO_MESSAGES = 26;
    private static final byte RECEIVE_FILE_RESPONSE_NO_FILE = 31;

    // Error codes (ID)
    private static final byte SERVER_ERROR = 100;
    private static final byte WRONG_SIZE = 101;
    private static final byte SERIALIZATION = 102;
    private static final byte UNKNOWN = 103;
    private static final byte COMMAND_INCORRECT_PARAMETERS = 104;
    private static final byte WRONG_PASSWORD = 110;
    private static final byte NOT_LOGGED_IN = 112;
    private static final byte SENDING_FAILED = 113;

    public void pingFromServer(byte[] srvResponse) {

        if (srvResponse[0] == PING_RESPONSE) {
            System.out.println("Ping OK.\n");
        } else {
            getError(srvResponse[0]);
        }
    }

    public void echoFromServer(byte[] srvResponse) throws UnsupportedEncodingException {
        String st = new String(srvResponse, "UTF-8");
        System.out.println(st);
    }

    private boolean isLogin = false;
    public void loginFromServer(byte[] srvResponse) {
        if (srvResponse[0] == LOGIN_RESPONSE_REG_OK) {
            System.out.println("Registration OK.");
            isLogin = true;
        } else if (srvResponse[0] == LOGIN_RESPONSE_LOGIN_OK) {
            System.out.println("Login OK.");
            isLogin = true;
        } else {
            getError(srvResponse[0]);
        }
    }

    public void listFromServer(byte[] srvResponse) throws ClassNotFoundException, IOException {
        if (srvResponse.length != 1) {
            for (String str : deserialize(srvResponse, 0, String[].class)) {
                System.out.println(str);
            }
        } else {
            getError(srvResponse[0]);
        }
    }

    public void msgFromServer(byte[] srvResponse) {
    	if (isLogin && srvResponse[0] == MSG_RESPONSE_SENT) {
            System.out.println("Message sent.");
        } else {
            getError(srvResponse[0]);
        }
    }

    public void fileFromServer(byte[] srvResponse) {
        if (srvResponse[0] == FILE_RESPONSE_SENT) {
            System.out.println("File sent.");
        } else {
            getError(srvResponse[0]);
        }

    }

    public void receiveMsg(byte[] srvResponse) throws ClassNotFoundException, IOException {
        if (srvResponse.length != 1) {
            String[] mess = deserialize(srvResponse, 0, String[].class);
            System.out.println("\nMessege from: " + mess[0] + " : " + mess[1]);
        } else if (srvResponse[0] == RECEIVE_MSG_RESPONSE_NO_MESSAGES){
            System.out.println("No messages.");
        } else {
            getError(srvResponse[0]);
        }
    }

    public void receiveFile(byte[] srvResponse) throws ClassNotFoundException, IOException {
        if (srvResponse.length != 1) {
            Object[] resFile = deserialize(srvResponse, 0, Object[].class);
            if (resFile.length != 3 || resFile[0] == null || resFile[1] == null || resFile[2] == null
                    || !resFile[0].getClass().equals(String.class) || !resFile[1].getClass().equals(String.class)
                    || !resFile[2].getClass().equals(byte[].class)) {
                System.out.println("Bad file parameters from server");
            }
            System.out.println("File from user:  " + (String) resFile[0] + "\nfile name : " + (String) resFile[1]);

            try (FileOutputStream fos = new FileOutputStream(
                    new File((String) resFile[0] + "_" + (String) resFile[1]))
                    ) {
                fos.write((byte[]) resFile[2]);
            } catch (Exception ex) {
                System.out.println("Problem with write file");
            }
        } else if (srvResponse[0] == RECEIVE_FILE_RESPONSE_NO_FILE){
            System.out.println("No file;");
        } else {
            getError(srvResponse[0]);
        }
    }

    public void getError(byte error) {
        switch (error) {
            case SERVER_ERROR:
                System.out.println("SERVER ERROR");
                break;
            case WRONG_SIZE:
                System.out.println("WRONG SIZE");
                break;
            case SERIALIZATION:
                System.out.println("SERIALIZATION");
                break;
            case UNKNOWN:
                System.out.println("UNKNOW");
                break;
            case COMMAND_INCORRECT_PARAMETERS:
                System.out.println("INCORRECT PARAMETERS");
                break;
            case WRONG_PASSWORD:
                System.out.println("WRONG PASSWORD");
                break;
            case NOT_LOGGED_IN:
                System.out.println("NOT LOGGED IN");
                break;
            case SENDING_FAILED:
                System.out.println("SENDING FAILED");
                break;
            default:                
                System.out.println("Unknow comand from server");
                System.out.println(error);
                break;
        }
    }

}