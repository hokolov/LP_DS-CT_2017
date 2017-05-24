package coursework03_rmi_client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.Locale;
import coursework03_rmi_server.Compute;
import coursework03_rmi_server.Compute.SortElemens;
import coursework03_rmi_server.Compute.FileInfo;

public class CommandsProcessing {
    
    private static final int NUMBER_OF_NEEDED_ELEMENTS = 0b11110100001001000000; //its 1000000 in binary
    private static final int RANGE_END_OF_ELEMENTS = 0b111011100110101100101000000000; //its 1000000000 (1E+9) in binary
    private static final int LENGTH_OF_ECHO_PARAMETERS = 2;
    private static final int LENGTH_OF_TASK_PARAMETERS = 2;

    private final Compute remoteCompute;

    public CommandsProcessing(Compute remoteCompute) { this.remoteCompute = remoteCompute; }

    public void help() {
        System.out.println("Available commands:\n"
                + "ping -this command send \"Ping\" to server\n"
                + "echo (anyText) -this command send \"Echo\" request to server\n"
                + "sort (fileName) --this command generate numbers of range 0 to 1E+9, saves them in a file and sends."
                + "\n     (fileName) this means that you have to enter the name of the file to be created.\n"
                + "exit -this command Exit from server and close connection\n");
    }
    
    public void exit() throws RemoteException {
        System.out.println("Exit from server.");
        Client.flag = false;
    }

    public void ping() throws RemoteException {
        System.out.println(remoteCompute.ping());
    }

    public void echo(String[] parameters) throws RemoteException {
        if (isValidNumberOfParameter(parameters.length, LENGTH_OF_ECHO_PARAMETERS)) {
            System.out.println(remoteCompute.echo(parameters[1]));
        }
    }

    public void sort(String[] parameters) throws RemoteException, IOException {
        if (isValidNumberOfParameter(parameters.length, LENGTH_OF_TASK_PARAMETERS)) {
            FileInfo sendedFileInfo = generatingElemens(parameters[1]);

            respondsProcessing(remoteCompute.executeTask(new SortElemens(sendedFileInfo)));
            System.out.println("Sort passed successfully.");
        }
    }

    private boolean isValidNumberOfParameter(int CommandArrayLength, int validLength) {
        return (CommandArrayLength == validLength) ? true : badLength();
    }
    
    private boolean badLength(){
        System.out.println("Bad argument.");
        return false;
    }

    private FileInfo generatingElemens(String fileName) throws IOException {
        String[] stringArr = new String[NUMBER_OF_NEEDED_ELEMENTS];
        log("Start generating elemens.");
        for (int i = 0; i < stringArr.length; i++) {
            double value = Math.random() * RANGE_END_OF_ELEMENTS;
            stringArr[i] = String.format(Locale.US, "%.0f", value);
        }
        log("Done. All elemens has been generated.");
        return new FileInfo(writeInFile(fileName, stringArr));
    }

    private void respondsProcessing(FileInfo fileInfo) {
        String content = new String(fileInfo.getFileContent(), StandardCharsets.UTF_8);
        writeInFile(fileInfo.getFileName(), content.split(" "));
    }

    private File writeInFile(String fileName, String[] writeArr) {
        File file = new File(fileName);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            for (String element : writeArr) {
                dos.writeBytes(element + " ");
            }
        } catch (Exception ex) {
            System.out.println("Problem with write file.");
        }
        return file;
    }
    
    private static void log(String logMsg){
        System.out.println(logMsg);
    }
}