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
        int c;
        
        int available = inputStream.available();
        System.out.println(available);
    }
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        SynacorChallenge synacor = new SynacorChallenge();
        //synacor.readText("challenge.bin");
        try{
            readBinary("challenge.bin");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
