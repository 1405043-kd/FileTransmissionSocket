import java.io.*;
import java.net.Socket;

/**
 * Created by USER on 9/29/2017.
 */
public class FileSender {
    byte[]mybytearray = null;
    clientThread threadRec = null;
    int current;
    FileOutputStream fos=null;
    BufferedOutputStream bos=null;
    ObjectOutputStream oOs = null;
    int bytesRead;
    Socket clientSocket;

    public FileSender(Socket socket, ObjectOutputStream oOs) {
        this.clientSocket = socket;
        this.oOs = oOs;
    }

    public void readFile() {
        try {
            ObjectInputStream oIs=new ObjectInputStream(clientSocket.getInputStream());
            System.out.println(oIs.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //current=0;
        //String filePath = new File("").getAbsolutePath()+"\\a.JPG";
      //  fos = new FileOutputStream(filePath);
      //  bos = new BufferedOutputStream(fos);
     //   bytesRead = is.read(mybytearray,0,mybytearray.length);
     //   current = bytesRead;

       // do {
        //    bytesRead =
            //        is.read(mybytearray, current, (mybytearray.length-current));
         //   if(bytesRead >= 0) current += bytesRead;
      //  } while(bytesRead > -1);
    }

}

