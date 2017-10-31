/**
 * Created by USER on 9/27/2017.
 */
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.BitSet;
import static java.lang.System.out;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

public class Client implements Runnable,Serializable{

    private static int MaxBuf=6400000;
    private static int chunkSize=100;
    private static int chunks;
    private static boolean fileREAD=false;
    private static String response="";
    private static Socket clientSocket = null;
    private static ObjectOutputStream os = null;
    private static ObjectInputStream is = null;
    private static String toReceive = "";
    private static BufferedReader inputLine = null;
    private static boolean closed = false;
    private static boolean isLogeed = false;
    private static boolean isOtherThread = false;
    private static boolean isReading = false;
    private static byte [] mybytearray = null;
    private static byte [] downLoadArray=null;
    private static byte[][] arrayO= null;
    private static int curr=0;
    private static String fileID="";
    private static String senderName="";
    private static File fileDown=null;
    private static String fileDownloadPath=null;
    public boolean errorFlag=false;
    public static void main(String[] args) {


        int portNumber = 2222;
        String host = "localhost";


        try {
            clientSocket = new Socket(host, portNumber);
            clientSocket.setSoTimeout(30000);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new ObjectOutputStream(clientSocket.getOutputStream());
            is = new ObjectInputStream(clientSocket.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + host);
        } catch (IOException e) {
            System.err.println("I/O Exception at " + host);
        }


        if (clientSocket != null && os != null && is != null) {
            try {
                new Thread(new Client()).start();

                while (true) {
                    /*
                     if(isLogeed==true && isReading==false) {
                         os.writeObject("justchecking");
                        String responseLine="";
                         responseLine = (String) is.readObject();
                         System.out.println(responseLine);

                        if(responseLine.contains("login")){
                            System.out.println("brr");
                        }
                        if(responseLine.contains("nofilegive")){
                           // os.writeObject("hola");
                        }

                        // input e je file name dibe oita buffer e jabe
                         String inputStr=inputLine.readLine().trim();
                         if(inputStr.contains("logout")){
                             System.out.println("Done For");
                             os.writeObject("logout");
                             break;
                         }
                        String[] lineArray = inputStr.split(" ");
                         System.out.println(lineArray.length);
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
                        os.writeObject(filePath+" "+toReceive);
                        responseLine="";
                        responseLine = (String) is.readObject();


                        if(responseLine.contains("overflowed")){
                            System.out.println("NOT POSSIBLE: TOO BIG TO HANDLE");
                            os.writeObject("not possible");
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


                            continue;

                        }

                     }



                     else if(isLogeed==false && isReading==false){
                         String responseLine="";
                         responseLine=(String)is.readObject();
                         if(responseLine.contains("login done")) {
                             isLogeed = true;
                             System.out.println(responseLine);

                             continue;
                         }

                         System.out.println(responseLine);
                         os.writeObject(inputLine.readLine().trim());

                     }
                     if(isReading==true){

                         String responseLine = "succeed";

                         os.writeObject(arrayO[curr]);
                         curr += 1;
                         responseLine = (String) is.readObject();
                         System.out.println(responseLine);
                         if (!responseLine.contains("some")) {
                             curr = 0;
                             chunks = 0;
                             isReading = false;
                             fileREAD = true;
                             continue;
                         }
                     }
                    */


                    //sleep for 3000ms (approx)
                    long timeToSleep = 300;
                    long start, end, slept;
                    boolean interrupted = false;

                    while(timeToSleep > 0){
                        start=System.currentTimeMillis();
                        try{
                            Thread.sleep(timeToSleep);
                            break;
                        }
                        catch(InterruptedException e){

                            //work out how much more time to sleep for
                            end=System.currentTimeMillis();
                            slept=end-start;
                            timeToSleep-=slept;
                            interrupted=true;
                        }
                    }

                    if(interrupted){
                        //restore interruption before exit
                        Thread.currentThread().interrupt();
                    }

                    String inputStr=inputLine.readLine().trim();
                    String[] lineArray = inputStr.split(" ");
                    if(lineArray.length==2) {
                        System.out.println(lineArray.length);
                        String filePath = new File("").getAbsolutePath();
                        filePath = filePath + "\\" + lineArray[1];
                        File file = new File(filePath);
                        FileInputStream fis = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        mybytearray=new byte[(int)file.length()];
                        bis.read(mybytearray,0,(int)file.length());
                        toReceive = lineArray[0];
                        filePath=filePath+" "+toReceive;
                        System.out.println(filePath);
                        os.writeObject(filePath);
                    }

                    else if(lineArray.length==1) {
                        if(inputStr.contains("NO"))
                            os.writeObject(fileID+" "+"NO"+" "+senderName);
                        else if(inputStr.contains("YES"))
                            os.writeObject(fileID+" "+"YES"+" "+senderName);
                        else if(inputStr.contains("CONTINUE")){
                            continue;
                        }
                        else
                            os.writeObject(inputStr);
                    }
                    if(inputStr.contains("logout")) {
                        os.writeObject("logout");
                        closed=true;
                        break;
                    }

                }
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    @Override
    public void run() {
        while (!closed) {
            //sleep for 3000ms (approx)
            long timeToSleep = 300;
            long start, end, slept;
            boolean interrupted = false;

            while(timeToSleep > 0){
                start=System.currentTimeMillis();
                try{
                    Thread.sleep(timeToSleep);
                    break;
                }
                catch(InterruptedException e){

                    //work out how much more time to sleep for
                    end=System.currentTimeMillis();
                    slept=end-start;
                    timeToSleep-=slept;
                    interrupted=true;
                }
            }

            if(interrupted){
                //restore interruption before exit
                Thread.currentThread().interrupt();
            }

        //    System.out.println("shit shi");
            if(isLogeed==false && isReading==false) {
                try {
                    response = (String) is.readObject();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
                if (response.contains("login")) {
                    isLogeed = true;
                    continue;
                }

            }
            else if(isLogeed==true && isReading==false && fileREAD==false) {
                try {
                    response = (String) is.readObject();
                }
                catch (SocketTimeoutException t){
                    continue;
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(response);

                if (response.contains(" ")){
                    String[] lineArray = response.split(" ");
                    if(lineArray.length==3){
                        fileID=lineArray[0];
                        senderName=lineArray[1];
                        try {
                            System.out.println("Write< \"YES\" <ENTER> \"CONTINUE\" >to receive, \"NO\" to decline");
                            os.writeObject(senderName+" "+inputLine.readLine().trim()+" "+fileID);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (response.contains("overflowed")) {
                    System.out.println("TRANSITION NOT POSSIBLE__BUFFER OVERFLOW");
                }
                else if (response.contains("ready")) {
                    String numberOnly = response.replaceAll("[^0-9]", "");
                    System.out.println(numberOnly);
                    chunks = Integer.parseInt(numberOnly);
                    arrayO = new byte[chunks][chunkSize];
                    for (int i = 0; i < chunks; i++) {
                        for (int j = 0; j < chunkSize; j++) {
                            if((i*chunkSize +j)<mybytearray.length)
                                arrayO[i][j] = mybytearray[i * chunkSize + j];
                            //   System.out.println(mybytearray);
                        }
                    }
                    isReading=true;curr=0;
                }
                else if(response.contains("RECEIVE")){
                    fileREAD=true;
                    System.out.println("Receive_found_Trail");
                  //  String numberOnly = response.replaceAll("[^0-9]", "");
                    String stringArray[]=response.split(",");
                    System.out.println(stringArray[2]);
                    fileDownloadPath= new File("").getAbsolutePath();
                    fileDownloadPath = fileDownloadPath + "\\fileID" + stringArray[0];
                    fileDown=new File(fileDownloadPath);
                    curr=0;
                    chunks = Integer.parseInt(stringArray[2]);
                    arrayO = new byte[chunks][chunkSize];
                    try {
                        os.writeObject("startSending");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
            ///ajker kaj eitukur moddhei :p yolo


            else if(isLogeed==true && isReading==true && fileREAD==false){
                //bitStuff(arrayO[curr]);
                try {

                    String toSendString= fromByteArrayToBinaryString(arrayO[curr])+'c'+checkSum(fromByteArrayToBinaryString(arrayO[curr]))+'c'+curr;
                    System.out.println("Frame to send : "+ toSendString);
                //    System.out.println("Bits to send - Stuffed : "+ bitStuff(fromByteArrayToBinaryString(arrayO[curr])));
                    System.out.println("Frame to send - Stuffed : " + bitStuff(toSendString));
                 //   os.writeObject(bitStuff(fromByteArrayToBinaryString(arrayO[curr])));
                    if(curr%13==0 && errorFlag==true) {
                        String errorFrame=toSendString;
                        errorFrame=errorFrame.replaceAll("1001","0111");
                        os.writeObject(bitStuff(errorFrame));
                        errorFlag=false;
                    }
                    else {
                        os.writeObject(bitStuff(toSendString));
                        errorFlag=true;
                    }
                //    os.writeObject(toSendString);
                    System.out.println("Frame no. "+curr+ " sent to server");

                }
                catch (IOException e){
                    e.printStackTrace();
                }
                /*
                try {
                    os.writeObject(arrayO[curr]);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                curr += 1;
           //     long testTime = System.currentTimeMillis();
                try {
                    response = (String) is.readObject();
                    System.out.println(response);
                    if (!response.contains("ServerResponseCHUNK_RECEIVED")) {
                        if(response.contains("resendFrame") ) {
                            curr=Integer.valueOf(response.replaceAll("[^0-9]", ""));
                            continue;
                        }
                        else {
                            //         System.out.println(response);
                            curr = 0;
                            chunks = 0;
                            isReading = false;
                            // fileREAD = true;
                            continue;
                        }
                    }
                }
                catch (SocketTimeoutException e) {
                    curr-=1;
                    System.out.println("TIMEOUT OCCURRED, SENDING AGAIN FRAME" +curr);
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                /*if(testTime >= (System.currentTimeMillis()+ 29*1000)) { //multiply by 1000 to get milliseconds
                    response="ERROR";
                    System.out.println("TIME OUT");
                } */


            }


            //////////ajker kaj eitukur moddhei



            else if(isLogeed==true && fileREAD==true ){
                try {
                    arrayO[curr]= (byte[]) is.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("receivedFileCHUNK "+curr);
            //    System.out.println( byteArrayToString(arrayO[curr]));
             //   if(bitDeStuff(bitStuff(arrayO[curr])).equals(fromByteArrayToBinaryString(arrayO[curr]))) System.out.println("yoho");
             //   System.out.println(bitDeStuff(bitStuff(arrayO[curr])));
             //   System.out.println(fromByteArrayToBinaryString(arrayO[curr]));
            //    if(Arrays.equals(bitDeStuff(bitStuff(arrayO[curr])),arrayO[curr])) System.out.println("yoloho");
              //  System.out.println(byteArrayToString(stringToByteArray(byteArrayToString(arrayO[curr]))));
                 System.out.println(fromByteArrayToBinaryString(arrayO[curr]));
                curr++;
                if(curr>=chunks){
                    fileREAD=false;
                    curr=0;
                    downLoadArray=new byte[chunks*chunkSize];
                    for (int i = 0; i < chunks; i++) {
                        for (int j = 0; j < chunkSize; j++) {
                            downLoadArray[i * chunkSize + j]=arrayO[i][j];
                        }
                    }
                    FileOutputStream fileOutputStream= null;
                    try {
                        fileOutputStream = new FileOutputStream(fileDown);
                        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
                        bufferedOutputStream.write(downLoadArray,0,downLoadArray.length);
                        bufferedOutputStream.flush();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        os.writeObject("downloadComplete");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else try {
                    os.writeObject("startSending");
                   // System.out.println("receivedFileCHUNL");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



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
  public long checkSum(String string){
      //   int count=0;
      //  long returnValue;
      byte byteArr[] = new byte[(string.length()+7)/8];
      byteArr=fromBinaryStringToByteArray(string);
       /* Checksum checksum = new CRC32();
        checksum.update(byteArr,0,byteArr.length);
        returnValue= checksum.getValue(); */
      long retValue = 1;
      for (byte b : byteArr) {
          retValue ^=b;
      }
      return retValue%(string.length()+7)/8;

      /*for(int i=0;i<string.length();i++){
            if(string.charAt(i)=='1'){
                count++;
            }
        }*/
      //return returnValue;
  }
  public String byteArrayToString(byte[] byteArr){
      //byte[] b = new byte[]{10};
      String string="";
      BitSet bitset = BitSet.valueOf(byteArr);

      //System.out.println("Length of bitset = " + bitset.length());
      for (int i=0; i<bitset.length(); ++i) {
         // System.out.println("bit " + i + ": " + bitset.get(i));
          if(bitset.get(i))
              string+="1";
          else string+="0";
      }
      return string;
  }
  public byte[] stringToByteArray(String string){
     // byte [] byteArray=new byte[(string.length()+7)/8];
      byte[] bytes = new byte[(string.length() + 7) / 8];
      for (int i=0; i<string.length(); i++) {
          if (string.charAt(i)=='1') {
              bytes[bytes.length-i/8-1] |= 1<<(i%8);
          }
      }
      return bytes;

  }
    String fromByteArrayToBinaryString(byte[] bytes){
        String s="";
        for(int i=0; i<8*bytes.length; i++){
            if((bytes[i/8]<< i%8 & 0x80)==0)
                s+='0';
            else s+='1';
        }
        return s;
    }
    byte[] fromBinaryStringToByteArray(String s){
        byte[] returnByteArr = new byte[(s.length()+8-1)/8];
        for(int i = 0; i < s.length(); i++)
            if(s.charAt(i) == '1')
                returnByteArr[i/8] = (byte)(returnByteArr[i/8]|(0x80>>>(i%8)));
        return returnByteArr;
    }


  public String bitStuff(String byteArr){
        String s="";
        String returnString="";
        int counter=0,flag=0;
        s=byteArr;
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i) == '1'){
                returnString = returnString + s.charAt(i);
                counter++;
            }
            else if(s.charAt(i)=='0') {
                returnString = returnString + s.charAt(i);
                counter = 0;
            }
            else if(s.charAt(i)=='c') {
                returnString = returnString + 'c';
                counter=0;
            }
            else{
                returnString=returnString+s.charAt(i);
                counter=0;
            }
            if(counter == 5) {
                returnString = returnString + '0';
                counter = 0;
            }


      }
      return "01111110"+returnString+"01111110";
  }

    public String bitDeStuff(String s){
        //String s="";
        String returnString="";
        s=s.replace("01111110","");
        int counter=0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i) == '1') {
                counter++;
                returnString = returnString + s.charAt(i);

            }
            else if(s.charAt(i) == '0'){
                returnString = returnString + s.charAt(i);
                counter = 0;
            }
            else{
                returnString = returnString + s.charAt(i);
                counter=0;
            }
            if(counter == 5){
                if((i+2)!=s.length())
                    returnString = returnString + s.charAt(i+2);
                else
                    returnString=returnString + '1';
                i=i+2;
                counter = 1;
            }
        }
        //   return fromBinaryStringToByteArray(returnString);
        return returnString;
    }

}