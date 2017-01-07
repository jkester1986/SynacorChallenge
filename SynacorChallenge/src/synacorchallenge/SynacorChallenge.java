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
            //System.out.println("hmmm");
            
            short shortVal = twoBytesToShort(directions[0][i], directions[1][i]);//since it is little-endian, bytes are reversed
            int intVal = shortVal >= 0 ? shortVal : 0x10000 + shortVal;//convert to unsigned int
            
            switch(intVal){
                case 0:
                    System.exit(0);
                case 19:
                    short nextShortVal = twoBytesToShort(directions[0][i+1], directions[1][i+1]);
                    //System.out.println(nextShortVal);
                    int nextIntVal = nextShortVal >= 0 ? nextShortVal : 0x10000 + nextShortVal;//convert to unsigned int
                    System.out.print(new Character((char)nextIntVal).toString());
                    //System.out.println(directions[i+1]);
                    i++;
                    break;
                case 21:
                    break;
            }
        }

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
