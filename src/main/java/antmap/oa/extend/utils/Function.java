package antmap.oa.extend.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

public class Function {

    /**
     * 获取根目录路径
     * @return
     */
    public static String getBaseDir(){
        Properties properties = System.getProperties();
        String path = properties.getProperty("user.dir");
        if (properties.getProperty("os.name").toLowerCase().contains("win")) {
            path += "\\";
        }else {
            path += "/";
        }
        return path;
    }

    /**
     * 获取临时文件目录
     * @return
     */
    public static String getTempfileDir(){
        String baseDir=getBaseDir();
        String tempfileDir=baseDir+"tempfile";
        File tempDir=new File(tempfileDir);
        if(!tempDir.exists()){
            tempDir.mkdir();
        }
        return tempfileDir+File.separator;
    }

    /**
     * 获取guid
     * @return
     */
    public static String getGuid(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 文件转base64
     * @param filePath
     * @return
     */
    public static String encryptToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
