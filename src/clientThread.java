import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by USER on 9/28/2017.
 */

class clientThread extends Thread implements Serializable {


    private static int MaxBuf=10000;
    private static int chunkSize=100;
    private int chunks=0;
    private String toReceive="";
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private Map<String,clientThread> studentMap=new ConcurrentHashMap<>();
    private String name = null;
    private String line = null;
    private static boolean flag=true;
    private boolean isReading = false;
    private boolean isWriting = false;
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    FileInputStream fis = null;
    ObjectOutputStream osF = null;
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    private DataOutputStream dos = null;
    private static int fileID=0;
    private byte[][][] arrayO=new byte[100][][];
    private byte[][] getMybytearray=null;
    private byte[][] extraByteArr=null;
    private byte[] mybytearray = new byte[100];

    public clientThread(Socket clientSocket, clientThread[] threads, Map<String,clientThread> studentMap,byte[][][]mybyte) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
        this.studentMap = studentMap;
        this.arrayO=mybyte;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;
    //    Map<Integer, clientThread> studentMap=this.studentMap;

        try {
      /*
       * Create input and output streams for this client.
       */

            is = new ObjectInputStream(clientSocket.getInputStream());
            os = new ObjectOutputStream(clientSocket.getOutputStream());
            String filePath = new File("").getAbsolutePath()+"\\a.JPG";
            fos = new FileOutputStream(filePath);
            bos = new BufferedOutputStream(fos);
          //  dos = new DataOutputStream(clientSocket.getOutputStream());
          //  osF = new ObjectOutputStream(clientSocket.getOutputStream());
         //   ObjectInputStream isF = new ObjectInputStream(clientSocket.getInputStream());
            int cou=0;
          while(cou<100) {
              int i=0;
              os.writeObject("EnterStudentID:");
             // name = is.readLine().trim();
              try {
                  name=(String)is.readObject();
                  //eikhane map korbo student ID'r accord e
                  for (i = 0; i < maxClientsCount; i++) {
                      if(name.contains("NOTLO")){
                          os.writeObject("Failure as not logged in");
                          continue;
                      }
                      if (threads[i] == this && studentMap.get(name) == null) {
                          studentMap.put(name, threads[i]);
                        //  threads[i].os.writeObject("Student ID " + name + " online now");
                          os.writeObject("Student ID " + name + " login done. Write \" logout \" to logout");
                          //eita diye sob thread e khobor pathabo je ekjon online hoise.. yeyeyeye
                      /*    for (int j = 0; j < maxClientsCount; j++) {
                              if (threads[j] != null && threads[j] != this) {
                                  threads[j].os.writeObject("Student ID " + name + " online now");
                              }
                          }  */
                          System.out.println("hi hi");
                          break;
                      } else if (threads[i] == this && studentMap.get(name) != null) {
                          threads[i].os.writeObject("Student ID already logged in");
                      }
                  }
              } catch (ClassNotFoundException e) {
                  e.printStackTrace();
              }

          //    System.out.println("ki ki ki");
              //mapping done
              if(i<maxClientsCount) cou=10000;
              cou++;
          }

/* **file to be sent here now on..and using the isReading things :p */
        while (true) {
            if(isReading==false && isWriting==false) {
                try {
                    line="";
                    line = (String) is.readObject();
                    System.out.println(line);
                    if(line.contains("NO")){
                        String[] lineArray = line.split(" ");
                        arrayO[Integer.parseInt(lineArray[2])]=null;
                        studentMap.get(lineArray[2]).os.writeObject("deniedByUserFAILURE");
                    }
                    if(line.contains("YES")){
                        String[] lineArray = line.split(" ");
                        extraByteArr=new byte[arrayO[Integer.parseInt(lineArray[0])].length][];
                        extraByteArr=arrayO[Integer.parseInt(lineArray[0])];
                        os.writeObject(fileID+",RECEIVESTART,"+Integer.toString(arrayO[Integer.parseInt(lineArray[0])].length));
                        chunks=arrayO[Integer.parseInt(lineArray[0])].length;
                        isWriting=true;
                        flag=false;
                        continue;
                    }
                    if (line.contains("logout")) {
                     //   os.writeObject("log out");
                        break;
                    }
                    else if(line.contains("\\")){
                        String [] tempStr=line.split(" ");
                        File file = new File(tempStr[0]);
                        toReceive=tempStr[1];
                        System.out.println((int) file.length());
                        System.out.println(toReceive);
                        mybytearray = new byte[(int)file.length()];
                        chunks=(int)(file.length()+99)/100;
                        System.out.println(chunks);
                        if ((((int) file.length()+99) / 100) > MaxBuf) {
                            os.writeObject("overflowed");
                        }
                        else {
                            os.writeObject("ready" + Integer.toString(chunks));
                            //eikhane array ta dite hobe
                            System.out.println("ready" + chunks);
                            getMybytearray=new byte[chunks][];
                            isReading = true;
                            current=0;
                        }
                    }
                    else os.writeObject("nofilegiven");
                     //   System.out.println(line);
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else if(isReading==true && isWriting==false) {
                String ss = "";
                    //read korte thakbe :3
                    long testTime = System.currentTimeMillis();
                    synchronized (is) {
                        mybytearray = (byte[]) is.readObject();
                    }
                    if(testTime >= (System.currentTimeMillis()+ 29*1000)) { //multiply by 1000 to get milliseconds
                        current=chunks+1;
                        System.out.println("TIME OUT");
                    }
                    else {
                        System.out.println(mybytearray);
                        getMybytearray[current] = mybytearray;
                    }
                    current += 1;
                    if (current >= chunks) {
                        isReading = false;
                        current = 0;
                        if (studentMap.get(toReceive) == null) {
                            os.writeObject("USER NOT AVAILABLE");
                            continue;
                        }
                        System.out.println(Integer.toString(fileID) + " " + name + " " + Integer.toString(getMybytearray.length * getMybytearray[0].length));
                        studentMap.get(toReceive).os.writeObject(Integer.toString(fileID) + " " + name + " " + Integer.toString(getMybytearray.length * getMybytearray[0].length));

                        os.writeObject(Integer.toString(getMybytearray.length * getMybytearray[0].length));
                        arrayO[fileID] = getMybytearray;
                        fileID++;
                        System.out.println("y");
                        continue;
                    } else {
                        os.writeObject("ServerResponseCHUNK_RECEIVED");
                    }
                }







            else if(isReading==false && isWriting==true) {
                System.out.println("came_here!!" + chunks);
                line = "";
                line = (String) is.readObject();
                if(line.contains("startSending")) {
                    System.out.println(line + current);
                    os.writeObject(extraByteArr[current]);
                    current += 1;
                }
                else continue;
                if (current>=chunks) {
                    current = 0;
                    chunks = 0;
                    isWriting = false;
                    flag = false;
                    System.out.println("sesh");
                 }



            }

        }
        for (int i = 0; i < maxClientsCount; i++) {
            if (threads[i] == this) {
                threads[i] = null;
            }
            studentMap.remove(name);
        }
        is.close();
        os.close();
        clientSocket.close();
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}