/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package synacorchallenge;

import java.io.*;
import static java.lang.System.in;
import java.util.StringTokenizer;



/**
 *
 * @author jkester
 */
public class SynacorChallenge {

    /**
     *
     * @param fileName
     * @throws java.io.IOException
     */
    
    //Just reads the text from the file
    public static void readText(String fileName) throws IOException{
        Reader reader = new FileReader(fileName);
        
        //allows us to read in whole line at a time
        BufferedReader bufferedReader = new BufferedReader(reader);
        String nextLine;
        
        while((nextLine = bufferedReader.readLine()) != null){
            //Tokenizer lets us extract non-space characters
            StringTokenizer tokenizer = new StringTokenizer(nextLine);
            while(tokenizer.hasMoreTokens()){
                System.out.print(tokenizer.nextToken() + " ");
            }
            System.out.print("\n");
        }
        bufferedReader.close();
    }
    
    public static void readBinary(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(fileName);
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        
        int available = inputStream.available();
        //System.out.println(available + " bytes to read");
        
        byte byte1;
        byte byte2;
        
        int count = 0;
        int index = 0;
        
        //directions are stored as little endian pairs, kept the bytes separate
        int[] directions = new int[available/2];
        
        int[] register = new int[8];
        
         
        while(count < available){
            
            byte1 = dataInputStream.readByte();
            byte2 = dataInputStream.readByte();
            
            directions[index] = getIntValue(byte2, byte1);
            //System.out.println(directions[index]);
            
            index++;
            count+=2;
        }
        
        int sizeDirs = directions.length;
        
        for(int i = 0; i < sizeDirs; i++){
            
            int a;
            int b;
            int c;
            
            
            switch(directions[i]){
                case 0://end program
                    System.out.println("case 0");
                    System.exit(0);
                    
                case 6://jump to value in a
                    System.out.println("case 6");
                    System.out.println(directions[i+1]);
                    a = getStoredValue(directions[i+1], directions);
                    System.out.println("jump to: " + a);
                    i = a;
                    break;
                    
                case 7://jump to b if a != 0
                    System.out.println("case 7");
                    a = getStoredValue(directions[i+1], directions);
                    b = getStoredValue(directions[i+2], directions);
                    System.out.println("a: " + a);
                    System.out.println("b: " + b);
                    if(a != 0){
                        i = b-1;
                    }
                    else {
                        i += 2;
                    }
                    break;
                    
                case 8://jump to b if a == 0
                    System.out.println("case 8");
                    a = getStoredValue(directions[i+1], directions);
                    b = getStoredValue(directions[i+2], directions);
                    System.out.println("a: " + a);
                    System.out.println("b: " + b);
                    if(a == 0){
                        i = b-1;
                    }
                    else {
                        i += 2;//should this be incrementing just like in 7?
                    }
                    break;
                    
                case 19://print to a screen
                    //System.out.println("case 19");
                    a = directions[i+1];
                    System.out.print(new Character((char)a).toString());
                    i++;
                    break;
                    
                case 21://do nothing
                    //System.out.println("case 21");
                    break;
                    
                default:
                    System.out.println("failing to continue at instruction: " + directions[i]);
                    //System.exit(0);
                    break;
            }
        }

    }
    
    public static int getIntValue(byte b1, byte b2){
        
        short shortVal = twoBytesToShort(b1, b2);
        int intVal = shortVal >= 0 ? shortVal : 0x10000 + shortVal;
        
        return intVal;
    }
    
    public static short twoBytesToShort(byte b1, byte b2) {
          return (short) ((b1 << 8) | (b2 & 0xFF));
    }
    
    /*
    * numbers 0..32767 mean a literal value
    * numbers 32768..32775 instead mean registers 0..7
    * numbers 32776..65535 are invalid
    */
    public static int getStoredValue(int direction, int[] directions) {
        if(direction > -1 && direction < 32767) {
            return direction;
        }
        else if(direction > 32767 && direction < 32776) {
            return directions[direction%32768];
        }
        else return -1;
    }
    
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        SynacorChallenge synacor = new SynacorChallenge();
        //SynacorChallenge.readText("challenge.bin");
        
        try{
            readBinary("challenge.bin");
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
    
}
