package util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputReader {
    public static String readFile(String filename) {
        Path path=Paths.get(filename);
        byte[] data=null;
        try{
            data = Files.readAllBytes(path);
        }catch (Exception e){

        }
        String result=new String(data);
        return result;
    }
}