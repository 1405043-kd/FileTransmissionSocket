/**
 * Created by USER on 9/27/2017.
 */
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static int MaxBuf=6400000;
    private static int chunkSize=100;
    private static int chunks;
    private static boolean fileREAD=false;
    // client socket
    private static Socket clientSocket = null;
    // The output stream
    private static ObjectOutputStream os = null;
    // The input stream
    private static ObjectInputStream is = null;
    private static String toReceive = null;
    private static BufferedReader inputLine = null;
    private static boolean closed = false;
    private static boolean isLogeed = false;
    private static boolean isReading = false;
    private static byte [] mybytearray = null;
    private static byte[][] arrayO= null;
    private static int curr=0;
    public static void main(String[] args) {

        // The default port.
        int portNumber = 2222;
        // The default host.
        String host = "localhost";

        /*
        if (args.length < 2) {
            System.out
                    .println("Usage: java MultiThreadChatClient <host> <portNumber>\n"
                            + "Now using host=" + host + ", portNumber=" + portNumber);
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        } */

    // new socket
        try {
            clientSocket = new Socket(host, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new ObjectOutputStream(clientSocket.getOutputStream());
            is = new ObjectInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + host);
        } catch (IOException e) {
            System.err.println("I/O Exception at " + host);
        }

    /*
     * If everything has been initialized then write some data to the
     * socket we have opened a connection to on the port portNumber.
     */
        if (clientSocket != null && os != null && is != null) {
            try {

        /* Create a thread to read from the server. */

                while (!closed) {
                     if(isLogeed==true && isReading==false) {
                         System.out.println("asche");
                        /* if(fileREAD==true) {
                            System.out.println("asche");
                            String s=(String)is.readObject();
                            if(s.contains("no file"))
                                fileREAD=false;

                        }*/
                        String responseLine="";
                        synchronized (is) {
                            responseLine = (String) is.readObject();
                        }
                        if(responseLine.contains("login")){
                            System.out.println("brrooo");
                        }
                        // input e je file name dibe oita buffer e jabe
                        String[] lineArray = inputLine.readLine().trim().split(" ");
                        String filePath = new File("").getAbsolutePath();
                        filePath = filePath + "\\" + lineArray[1];
                        toReceive = lineArray[0];
                        System.out.println(toReceive);
                        File file = new File(filePath);
                        FileInputStream fis = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        mybytearray=new byte[(int)file.length()];
                        bis.read(mybytearray,0,(int)file.length());
                        System.out.println(filePath);
                        os.writeObject(filePath);
                        responseLine="";
                        responseLine = (String) is.readObject();

                        if(responseLine.contains("overflowed")){
                            System.out.println("NOT POSSIBLE: TOO BIG TO HANDLE");
                        }
                        else if(responseLine.contains("ready")){
                            System.out.println(responseLine);
                            isReading=true;
                            String numberOnly= responseLine.replaceAll("[^0-9]", "");
                            System.out.println(numberOnly);
                            chunks=Integer.parseInt(numberOnly);
                            arrayO=new byte[chunks][chunkSize];
                            for(int i=0;i<chunks;i++){
                                for(int j=0;j<chunkSize;j++){
                                    arrayO[i][j]=mybytearray[i*chunkSize+j];
                                }
                            }
                            curr=0;
                           /* synchronized (os) {
                                os.writeObject(mybytearray);  //eta ki chilo?????
                            } */
                         //   System.out.println(mybytearray);
                            continue;

                        }

                     }
                     else if(isLogeed==false && isReading==false){
                         String responseLine="";
                         responseLine=(String)is.readObject();
                         if(responseLine.contains("login done")) {
                             isLogeed = true;
                             System.out.println(responseLine);
                             synchronized (os) {
                                 os.writeObject(responseLine);
                             }
                             continue;
                         }

                         System.out.println(responseLine);
                         os.writeObject(inputLine.readLine().trim());
                        // continue;
                     }
                     if(isReading==true){
                      //   int chunks = mybytearray.length / chunkSize;
                      //   int bytesRead=0;
                         String responseLine="succeed";
                       //  curr=0;
                         // System.out.println(mybytearray);
                         synchronized (os) {
                             os.writeObject(arrayO[curr]);
                         }
                         curr+=1;
                         responseLine=(String)is.readObject();
                         System.out.println(responseLine);
                         if(!responseLine.contains("some")){
                             synchronized (os) {
                                 os.writeObject("hehe");
                             }
                             curr=0;chunks=0;isReading=false;fileREAD=true;
                             continue;
                         }

                     }
                    //os.println("JOJO");
                }
        /*
         * Close the output stream, close the input stream, close the socket.
         */
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Create a thread to read from the server. (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
  /*  public void run() {

        String responseLine;
        try {
          //  while ((responseLine =(String) is.readObject()) != null) {
              while (true) {
                  try {
                      responseLine=(String)is.readObject();
                      if(responseLine.contains("login done"))
                          isLogeed=true;
                      System.out.println(responseLine);
                     // System.out.println(isLogeed);
                      if (responseLine.indexOf("LeftOut") != -1)
                          break;
                  } catch (ClassNotFoundException e) {
                      e.printStackTrace();
                  }

            }
            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    } */
}