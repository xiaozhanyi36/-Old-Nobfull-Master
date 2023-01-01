package net.ccbluex.liquidbounce.utils.misc;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class FileUtils {
    public static void unpackFile(File file,String name) throws IOException {
        if(!file.exists()) {
            FileOutputStream fos=new FileOutputStream(file);
            IOUtils.copy(FileUtils.class.getClassLoader().getResourceAsStream(name), fos);
            fos.close();
        }
    }
    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
