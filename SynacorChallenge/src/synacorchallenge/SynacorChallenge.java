/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package synacorchallenge;

import java.io.*;
import static java.lang.System.in;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Stack;



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
    
    private static int[] registers = new int[8];
    private static int[] directions = new int[1 << 15];
    private static Stack stack = new Stack<Integer>();
    private static int i = 0;
    private static String command = "";
    
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
             
         
        while(count < available){
            
            byte1 = dataInputStream.readByte();
            byte2 = dataInputStream.readByte();
            
            directions[index] = getIntValue(byte2, byte1);
            //System.out.println(directions[index]);
            
            index++;
            count+=2;
        }
        
        int sizeDirs = directions.length;
            
        while(i < sizeDirs){
            runCmd(directions[i]);
        }
    }
    
    public static void runCmd(int cmd) {
        int a;
        int b;
        int c;


        switch(cmd){
            case 0://end program
                //System.out.println("instruction 0 - exit");
                System.exit(0);

            case 1://set reg a to value of b
                //System.out.println("instruction 1 - set reg a to value of b");
                //System.out.println("directions i+1 = " + directions[i+1]);
                //System.out.println("directions i+2 = " + directions[i+2]);
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                //System.out.println("a: " + a);
                //System.out.println("b: " + b);
                registers[a] = b;
                i += 3;
                break;

            case 2://push <a> onto the stack
                stack.addElement(getStoredValue(directions[i+1]));
                i += 2;
                break;

            case 3://remove the top element from the stack and write it into <a>; empty stack = error
                int popped = (int)stack.pop();
                a = getRegister(directions[i+1]);
                registers[a] = popped;
                i += 2;
                break;

            case 4://set <a> to 1 if <b> is equal to <c>; set it to 0 otherwise
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                c = getStoredValue(directions[i+3]);
                registers[a] = b == c ? 1 : 0;

                i += 4;
                break;

            case 5://set <a> to 1 if <b> is greater than <c>; set it to 0 otherwise
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                c = getStoredValue(directions[i+3]);
                registers[a] = b > c ? 1 : 0;
                i += 4;
                break;

            case 6://jump to value in a
                //System.out.println("instruction 6 - jump to location of value in a");
                //System.out.println(directions[i+1]);
                a = getStoredValue(directions[i+1]);
                //System.out.println("jump to: " + a);
                i = a;
                break;

            case 7://jump to b if a != 0
                //System.out.println("instruction 7 - jump to b if a != 0");
                a = getStoredValue(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                //System.out.println("a: " + a);
                //System.out.println("b: " + b);
                if(a != 0){
                    i = b;
                }
                else {
                    i += 3;
                }
                break;

            case 8://jump to b if a == 0
                //System.out.println("instruction 8 - jump to b if a == 0");
                a = getStoredValue(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                //System.out.println("a: " + a);
                //System.out.println("b: " + b);
                if(a == 0){
                    i = b;
                }
                else {
                    i += 3;//should this be incrementing just like in 7?
                }
                break;

            case 9://assign into <a> the sum of <b> and <c> (modulo 32768)
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                c = getStoredValue(directions[i+3]);
                registers[a] = (b + c)%32768;
                i += 4;
                break;

            case 10://store into <a> the product of <b> and <c> (modulo 32768
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                c = getStoredValue(directions[i+3]);
                registers[a] = (b*c)%32768;
                i += 4;
                break;

            case 11://store into <a> the remainder of <b> divided by <c>
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                c = getStoredValue(directions[i+3]);
                registers[a] = (b%c);
                i += 4;
                break;

            case 12://stores into <a> the bitwise and of <b> and <c>
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                c = getStoredValue(directions[i+3]);
                registers[a] = b & c;
                i += 4;
                break;

            case 13://stores into <a> the bitwise or of <b> and <c>
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                c = getStoredValue(directions[i+3]);
                registers[a] = b | c;
                i += 4;
                break;

            case 14://stores 15-bit bitwise inverse of <b> in <a>
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                registers[a] = (~b) & 32767;
                i += 3;
                break;

            case 15://read memory at address <b> and write it to <a>
                a = getRegister(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                //int mem = getStoredValue(directions[b]);//this might be the correct one
                int mem = directions[b];
                registers[a] = mem;
                i += 3;
                break;

            case 16://write the value from <b> into memory at address <a>

                a = getStoredValue(directions[i+1]);
                b = getStoredValue(directions[i+2]);
                directions[a] = b;
                i += 3;    
                break;

            case 17://write the address of the next instruction to the stack and jump to <a>
                stack.addElement(i+2);
                a = getStoredValue(directions[i+1]);
                i = a;
                break;

            case 18://remove the top element from the stack and jump to it; empty stack = halt
                int el = (int)stack.pop();
                i = el;
                break;

            case 19://print to a screen
                //System.out.println("case 19");
                a = getStoredValue(directions[i+1]);
                System.out.print((char)a);
                i += 2;
                break;

            case 20:/*read a character from the terminal and write its ascii code
                to <a>; it can be assumed that once input starts,
                it will continue until a newline is encountered;
                this means that you can safely read whole lines
                from the keyboard and trust that they will be fully read
                */
                a = getRegister(directions[i+1]);
                try {
                    int inChar = System.in.read();
                    registers[a] = inChar;
                    if(inChar == 10) {
                        checkCmd();
                        command = "";
                    }
                    else command += (char)inChar;
                    i += 2;
                }
                catch(Exception e) {
                    System.out.println("something went wrong");
                    System.exit(0);
                }
                
                break;

            case 21://do nothing
                //System.out.println("case 21");
                i++;
                break;

            default:
                System.out.println("failing to continue at instruction: " + directions[i]);
                System.exit(0);
                break;
        }
        
    }
    
    public static void checkCmd() {
        switch(command) {
            case "exit":
                System.exit(0);
                break;
            case "save":
                System.out.println("save command");
                break;
            default:
                System.out.println("other command");
                break;
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
    public static int getStoredValue(int direction) {
        if(direction > -1 && direction < 32768) {
            return direction;
        }
        else if(direction > 32767 && direction < 32776) {
            return registers[direction%32768];
        }
        else {
            System.out.println("something went wrong, youf value is too high");
            System.exit(0);
            return 0;
        }
    }
    
    public static int getRegister(int reg) {
        if(reg > 32767 && reg < 32776) {
            return reg%32768;
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
