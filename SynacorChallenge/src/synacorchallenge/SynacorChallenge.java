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
        System.out.println(available + " bytes to read");
        
        byte byte1;
        byte byte2;
        
        int count = 0;
        int index = 0;
        
        //directions are stored as little endian pairs, kept the bytes separate
        byte[][] directions = new byte[2][available/2];
        
         
        
        while(count < available){
            
            byte1 = dataInputStream.readByte();
            byte2 = dataInputStream.readByte();
            
            String s1 = String.format("%8s", Integer.toBinaryString(byte1 & 0xFF)).replace(' ', '0');
            String s2 = String.format("%8s", Integer.toBinaryString(byte2 & 0xFF)).replace(' ', '0');
            
            
            short shortVal = twoBytesToShort(byte2, byte1);//since it is little-endian, bytes are reversed
            int intVal = shortVal >= 0 ? shortVal : 0x10000 + shortVal;//convert to unsigned int
            //System.out.println(intVal);
            //System.out.println(s2 + s1);
            
            //System.out.println(new Character((char)intVal).toString());
            
            directions[0][index] = byte2;
            directions[1][index] = byte1;
            index++;
            count+=2;
        }
        
        int sizeDirs = directions.length*directions[0].length/2;
        System.out.println(sizeDirs);
        
        for(int i = 0; i < sizeDirs; i++){
            
            short shortVal = twoBytesToShort(directions[0][i], directions[1][i]);//since it is little-endian, bytes are reversed
            int intVal = shortVal >= 0 ? shortVal : 0x10000 + shortVal;//convert to unsigned int
            
            switch(intVal){
                case 0://end program
                    System.exit(0);
                case 1://set register a to val of b
                    int nextVal6 = getValue(directions[0][i+1], directions[1][i+1]);
                    System.out.println("nextVal6: " + nextVal6);
                    break;
                case 6://jump
                    int nextIntVal1 = getValue(directions[0][i+1], directions[1][i+1]);
                    //System.out.println("jump to: " + nextIntVal1);
                    i = nextIntVal1-1;
                    break;
                case 7://jump to be if a != 0
                    int nextVal3 = getValue(directions[0][i+1], directions[1][i+1]);
                    if(nextVal3 != 0){
                        int nextVal4 = getValue(directions[0][i+2], directions[1][i+2]);
                        //System.out.println(nextVal4);
                        i = nextVal4-1;
                    }
                    else {
                        //System.out.println("nextVal: " + nextVal3);
                        i += 2;
                    }
                    break;
                    
                case 8://jump to b if a == 0
                    int nextVal5 = getValue(directions[0][i+1], directions[1][i+1]);
                    if(nextVal5 == 0){
                        int nextVal4 = getValue(directions[0][i+2], directions[1][i+2]);
                        //System.out.println(nextVal4);
                        i = nextVal4-1;
                    }
                    else {
                        //System.out.println("nextVal: " + nextVal5);
                        //i += 2;//should this be incrementing just like in 7?
                    }
                    break;
                case 19://print to screen
                    int nextIntVal2 = getValue(directions[0][i+1], directions[1][i+1]);
                    System.out.print(new Character((char)nextIntVal2).toString());
                    i++;
                    break;
                case 21://do nothing
                    break;
                default:
                    System.out.println("instruction: " + intVal);
                    System.exit(0);
            }
        }

    }
    
    public static int getValue(byte b1, byte b2){
        
        short shortVal = twoBytesToShort(b1, b2);
        int intVal = shortVal >= 0 ? shortVal : 0x10000 + shortVal;
        
        return intVal;
    }
    
    public static short twoBytesToShort(byte b1, byte b2) {
          return (short) ((b1 << 8) | (b2 & 0xFF));
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
