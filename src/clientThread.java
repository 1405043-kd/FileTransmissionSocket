import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by USER on 9/28/2017.
 */

class clientThread extends Thread {

    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private Map<String,clientThread> studentMap;
    private String name = null;
    private String line = null;

    public clientThread(Socket clientSocket, clientThread[] threads, Map<String,clientThread> studentMap) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
        this.studentMap = studentMap;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;
    //    Map<Integer, clientThread> studentMap=this.studentMap;

        try {
      /*
       * Create input and output streams for this client.
       */

            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            int cou=0;
          while(cou<100) {
              int i=0;
              os.println("Enter Student ID: ");
              name = is.readLine().trim();
              //eikhane map korbo student ID'r accord e
              for (i = 0; i < maxClientsCount; i++) {
                  if (threads[i] == this && studentMap.get(name) == null) {
                      studentMap.put(name, threads[i]);
                      threads[i].os.println("Student ID " + name + " online now");
                      os.println("Student ID " + name + " login done. Write \" logout \" to logout");
                      //eita diye sob thread e khobor pathabo je ekjon online hoise.. yeyeyeye
                      for (int j = 0; j < maxClientsCount; j++) {
                          if (threads[j] != null && threads[j] != this) {
                              threads[j].os.println("Student ID " + name + " online now");
                          }
                      }
                      System.out.println("hi hi");
                      break;
                  } else if (threads[i] == this && studentMap.get(name) != null) {
                      threads[i].os.println("Student ID already logged in");
                  }
              }
              System.out.println("ki ki ki");
              //mapping done
              if(i<maxClientsCount) cou=10000;
              cou++;
          }

            while (true) {
                line = is.readLine().trim();
                if (line.startsWith("logout")) {
                    break;
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].os.println("<" + name + "> " + line);
                    }
                }
            }
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** The user " + name
                            + " is leaving the chat room !!! ***");
                }
            }
            studentMap.remove(name);
            os.println("*** Bye " + name + " ***");

      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }

      /*
       * Close the output stream, close the input stream, close the socket.
       */
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}