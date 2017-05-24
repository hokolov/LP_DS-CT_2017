package coursework03_rmi_server;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Instant;

public interface Compute extends Remote {

    public static final int SERVER_PORT = 15155;
    public static final String SERVER_NAME = "coursework03_rmi_server";

    public <T> T executeTask(Task<T> t) throws IOException, RemoteException;
    public String echo(String text) throws RemoteException;
    public String ping() throws RemoteException;

    public static class FileInfo implements Serializable {

        private static final long serialVersionUID = 229L;
        private byte[] fileContent;
        private String filename;

        public byte[] getFileContent() {
            return fileContent;
        }

        public void setFileContent(byte[] fileContent) {
            this.fileContent = fileContent;
        }        
        
        public String getFileName() {
            return filename;
        }

        public void getFileName(String filename) {
            this.filename = filename;
        }

        public FileInfo() {
        }

        public FileInfo(File file) throws IOException {
            fileContent = Files.readAllBytes(file.toPath());
            filename = file.getName();
        }
    }

    public class SortElemens implements Task<Compute.FileInfo>, Serializable {

        private static final long serialVersionUID = 227L;

        private final Compute.FileInfo fileInfo;
        private String[] stringArray;
        private double[] doubleArray;
        private File file;

        public SortElemens(FileInfo fileInfo) throws IOException {
            this.fileInfo = fileInfo;
        }

        @Override
        public Compute.FileInfo execute() throws IOException {
            convertArr();
            shakerSortArr();

            file = new File("sorted_" + fileInfo.getFileName());
            writeToFile();
            return new Compute.FileInfo(file);
        }
        
        private void convertArr() {
            String content = new String(fileInfo.getFileContent(), StandardCharsets.UTF_8).trim(); //tostr
            stringArray = (content.equals("")) ? new String[]{} : content.split(" ");
            
            doubleArray = (stringArray.length == 0) ? new double[]{} : toDouble(); //todouble
        }

        private double[] toDouble() {
            doubleArray = new double[stringArray.length];
            for (int i = 0; i < stringArray.length; i++) {
                doubleArray[i] = Double.parseDouble(stringArray[i]);
            }
            return doubleArray;
        }
        
        private void writeToFile() {

            if (doubleArray.length == 0) {
                return;
            }

            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                for (double element : doubleArray) {
                    dos.writeBytes(String.format( Locale.US, "%.1f", element) + " ");
                }
            } catch (Exception ex) {
                System.out.println("Problem with write file");
            }
        }
        
        private void shakerSortArr() {
            int startTime = (int)(Instant.now().getEpochSecond());

            //Start sorting
            boolean swapped = true;
            int start = 0;
            int end = doubleArray.length;
 
            while (swapped==true) {
                // reset the swapped flag on entering the 
                // loop, because it might be true from a 
                // previous iteration.
                swapped = false;
 
                // loop from bottom to top same as
                // the bubble sort
                for (int i = start; i < end-1; ++i) {
                    if (doubleArray[i] > doubleArray[i + 1]) {
                        double temp = doubleArray[i];
                        doubleArray[i] = doubleArray[i+1];
                        doubleArray[i+1] = temp;
                        swapped = true;
                    }
                }
 
                // if nothing moved, then array is sorted.
                if (swapped==false) break;
 
                // otherwise, reset the swapped flag so that it
                // can be used in the next stage
                swapped = false;
 
                // move the end point back by one, because
                // item at the end is in its rightful spot
                end = end-1;
 
                // from top to bottom, doing the
                // same comparison as in the previous stage
                for (int i = end-1; i >=start; i--) {
                    if (doubleArray[i] > doubleArray[i+1]) {
                        double temp = doubleArray[i];
                        doubleArray[i] = doubleArray[i+1];
                        doubleArray[i+1] = temp;
                        swapped = true;
                    }
                }
 
                // increase the starting point, because
                // the last stage would have moved the next
                // smallest number to its rightful spot.
                start = start+1;
            }

            int finishTime = (int) (Instant.now().getEpochSecond() - startTime);
            System.out.printf("Duration of sorting " + finishTime + " sec.");
        }
    }
}
