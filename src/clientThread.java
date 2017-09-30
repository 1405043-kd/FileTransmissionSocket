import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * Created by USER on 9/28/2017.
 */

class clientThread extends Thread {


    private static int MaxBuf=10000;
    private static int chunkSize=100;
    private static int chunks=0;
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private Map<String,clientThread> studentMap;
    private String name = null;
    private String line = null;
    private static boolean isReading = false;
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    FileInputStream fis = null;
    ObjectOutputStream osF = null;
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    private DataOutputStream dos = null;
    private static int fileID=0;
    private static byte[][][] arrayO=new byte[100][][];
    private byte[][] getMybytearray=null;
    private byte[] mybytearray = new byte[100];

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
              os.writeObject("Enter Student ID: ");
             // name = is.readLine().trim();
              try {
                  name=(String)is.readObject();
                  //eikhane map korbo student ID'r accord e
                  for (i = 0; i < maxClientsCount; i++) {
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
   //             if(isReading==false) {
       //             os.writeObject("Enter Student ID you want to send messege");
     //               for (int i = 0; i < maxClientsCount; i++) {
         //               if (threads[i] == this) {
           //                 threads[i].os.writeObject("\"Enter Student ID <SPACE> FILE Path\"");
             //           }
               //     }

                //   current=0;


                      // bytesRead = current;                     /
                   /*
                   try {
                       bytesRead =is.read(mybytearray, current, 100);

                   } catch (Exception e){
                       e.printStackTrace();
                   }
                   if(bytesRead>=100) {
                       current += bytesRead;
                       os.writeObject(bytesRead+" bytes succeed");
                       System.out.println("byete");
                       bytesRead=-1;
                   }
                   else {
                       System.out.println("fail");
                       os.writeObject("fail");
                   }

                       if(current>10000){
                           os.writeObject("Done");
                           break;
                       }
                                                                   */
                if(isReading==false) {

                    try {
                        line="";
                        line = (String) is.readObject();
                        System.out.println(line);
                        if (line.startsWith("logout")) {
                            break;
                        } else if(line.contains("\\")){
                            File file = new File(line);
                            System.out.println((int) file.length());
                            mybytearray = new byte[(int)file.length()];
                            chunks=(int)file.length()/100;
                            System.out.println(chunks);
                            if (((int) file.length() / 100) > MaxBuf) {
                                os.writeObject("overflowed");

                            } else {
                                os.writeObject("ready" + chunks);
                                //eikhane array ta dite hobe
                                getMybytearray=new byte[chunks][];
                                isReading = true;
                                current=0;

                            }
                        }
                        else os.writeObject("no file given");

                     //   System.out.println(line);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else if(isReading==true){
              //      System.out.println("what");
                    //read korte thakbe :3
                        synchronized (is) {
                            mybytearray = (byte[]) is.readObject();
                        }
                    System.out.println(mybytearray);
                    getMybytearray[current]=mybytearray;
                    current+=1;

                    if(current>=chunks) {
                        isReading = false;
                        arrayO[fileID]=getMybytearray;
                        os.writeObject(Integer.toString(getMybytearray.length*getMybytearray[0].length));
                        fileID++;
                        System.out.println("y");
                        continue;
                    }
                    else {

                            os.writeObject("read some");

                    }
                }






 //                   String[] lineArray = line.split(" ");
   //                 String filePath = new File("").getAbsolutePath();
                  //  System.out.println(filePath);

     //               if (studentMap.get(lineArray[0]) != null) { //successful hobe ..tai oi thread e messege ta print dibe arki
       //                 for (int i = 0; i < maxClientsCount; i++) {
         //                   if (threads[i] == studentMap.get(lineArray[0])) {
           //                     studentMap.get(lineArray[0]).os.writeObject("< " + name + " >" + line);
                                //eikhnae /*/ file read korbo arki

                               /* if(lineArray[1]!=null) {
                                    //   filePath.concat("\\");
                                    filePath=filePath+"\\"+lineArray[1];
                                    System.out.println(filePath);
                                    File file = new File (filePath);

                                    fis = new FileInputStream(file);
                                    bis = new BufferedInputStream(fis);
                                    System.out.println("shi shi");
                                    mybytearray  = new byte [(int)file.length()];
                                    bis.read(mybytearray,0,mybytearray.length);
                                    bis.close();
                                } */
                             //       studentMap.get(lineArray[0]).osF.write(mybytearray,0,mybytearray.length);
                                  //  studentMap.get(lineArray[0]).mybytearray=mybytearray;
                                    //osF=new ObjectOutputStream(clientSocket.getOutputStream());
                                  //  studentMap.get(lineArray[0]).isReading=true;

                             /*   while(true) {  //eikhane file sending er kaj gula
                                    ObjectOutputStream oOs = new ObjectOutputStream(clientSocket.getOutputStream());
                                    DataInputStream inputStream= new DataInputStream(clientSocket.getInputStream());
                                    oOs.write(mybytearray,0,mybytearray.length);
                                    FileSender fileSender=new FileSender(clientSocket,oOs);
                                    fileSender.readFile();
                                    String s= inputStream.readLine().trim();
                                    if(s.equals("YES")) continue;
                                    else if(s.equals("NO")) {
                                        oOs.close();
                                        inputStream.close();
                                        is = new DataInputStream(clientSocket.getInputStream());
                                        os = new PrintStream(clientSocket.getOutputStream());
                                        break;
                                    }
                                }  */

                              //      studentMap.get(lineArray[0]).osF.write(mybytearray,0,100);
                               //     studentMap.get(lineArray[0]).osF.flush();

          //                  }
        //                }


            //        }

//                    else { //successfull hoy nai..tai offline je oita ei thread e print korbo
  //                      for (int i = 0; i < maxClientsCount; i++) {
    //                        if (threads[i] == this) {
      //                          threads[i].os.writeObject("ERROR : : :  " + "<" + lineArray[0] + "> " + "is offline or invalid");
        //                    }
          //              }
            //        }
                /*
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].os.println("<" + name + "> " + line);
                    }
                }  ei comment er majhe ja ase ta sobaike notify korbe*/
     //           }
       //         else if (isReading==true){
                    //file read er code cholbe er moddhe

              //      current=0;
              //      String filePath = new File("").getAbsolutePath()+"\\a.pdf";
             //       fos = new FileOutputStream(filePath);
             //       bos = new BufferedOutputStream(fos);
               //     mybytearray=new byte[6535555];
              //      bytesRead = isF.read(mybytearray,0,mybytearray.length);
               //     System.out.println(bytesRead);
                   // current = bytesRead;
/*
                    do {
                        bytesRead =
                                is.read(mybytearray, current, (mybytearray.length-current));
                        if(bytesRead >= 0) current += bytesRead;
                    } while(bytesRead > -1);
*/
          //          bos.write(mybytearray, 0 , mybytearray.length);
            //        bos.flush();
             //       isReading=false;
                }
    //        }

            //loop break hoye logout hoye geche

     //       for (int i = 0; i < maxClientsCount; i++) {
       //         if (threads[i] != null && threads[i] != this) {
         //           threads[i].os.writeObject("*** The "+ name +" just logged out ");
           //     }
      //      }
        //    studentMap.remove(name);
          //  os.writeObject("*** Bye " + name + " ***");

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}