package client.tcp;

import java.io.IOException;

public class CommandsToServer {
	private static final byte[] PING_ID = new byte[]{1};
    private static final byte[] ECHO_ID = new byte[]{3};
    private static final byte[] LOGIN_ID = new byte[]{5};
    private static final byte[] LIST_ID = new byte[]{10};
    private static final byte[] MSG_ID = new byte[]{15};
    private static final byte[] RECEIVE_MSG_ID = new byte[]{25};

    public byte[] pingToServer(String comand) {
    	CommandsInterpretator.sendCommand = comand;
        return PING_ID;
    }

    public byte[] echoToServer(String[] comand) {
    	CommandsInterpretator.sendCommand = comand[0];
        if (comand.length == 1) {
            return ECHO_ID;
        } else {
            byte[] respByte = new byte[1 + comand[1].getBytes().length];
            respByte[0] = ECHO_ID[0];

            System.arraycopy(comand[1].getBytes(), 0, respByte, 1, comand[1].getBytes().length);
            return respByte;
        }
    }

    private String[] user = null;
    private byte[] serialize = null;

    public byte[] loginToServer(String[] comand) throws IOException {
    	CommandsInterpretator.sendCommand = comand[0];

        if (comand.length == 3) {
            user = new String[2];
            user[0] = comand[1];
            user[1] = comand[2];
        }
        serialize = CommandsInterpretator.serialize(user);
        byte[] respByte = new byte[1 + serialize.length];
        respByte[0] = LOGIN_ID[0];

        System.arraycopy(serialize, 0, respByte, 1, serialize.length);
        return respByte;
    }

    public byte[] listToServer(String[] comand) {
        CommandsInterpretator.sendCommand = comand[0];
        return LIST_ID;
    }

    public byte[] msgToServer(String[] comand) throws IOException {
        CommandsInterpretator.sendCommand = comand[0];
        if (comand.length == 3) {
            user = new String[2];
            user[0] = comand[1];
            user[1] = comand[2];

            serialize = CommandsInterpretator.serialize(user);
            byte[] respByte = new byte[1 + serialize.length];
            respByte[0] = MSG_ID[0];

            System.arraycopy(serialize, 0, respByte, 1, serialize.length);
            return respByte;
        } else {
            return null;
        }
    }
    
    
    public byte[] receiveMsgToServer() {
        CommandsInterpretator.sendCommand = "receivemsg";
        return RECEIVE_MSG_ID;
    }

}
