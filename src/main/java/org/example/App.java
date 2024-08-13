package org.example;

import org.example.util.FileHelper;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args )
    {
        String meta = FileHelper.readFile("src/meta-json/test.json");
        String data = FileHelper.readFile("src/data-json/test.json");
        try {
            ValidateMsg msg = ValidateMsg.validate(meta, data);
            System.out.println(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
