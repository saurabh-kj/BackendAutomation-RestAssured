package utills;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReadFile {
/*    public String readFile(String fileName) throws IOException {
        ClassLoader loader = getClass().getClassLoader();
        InputStream inputStream = loader.getResourceAsStream(fileName);
        return IOUtils.toString(inputStream);
    }*/

    public void readFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        while (line != null){
            line = bufferedReader.readLine();
        }
    }

    public static Map<String, String> readProperties(final String path){
        Properties properties = new Properties();
        Map<String, String> map = new HashMap<>();
        InputStream input = null;
        try {
            input = new FileInputStream(path);
            properties.load(input);
        } catch (Exception e) {
            System.out.println("Exception at readproperties:" +e);
        }
        for (Map.Entry<Object, Object> entries : properties.entrySet()){
            map.put((String) entries.getKey(), (String) entries.getValue());
        }
        return map;
    }
}
