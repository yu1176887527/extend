package antmap.oa.extend.utils;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class HttpUtils {
    /**
     * 返回post方法
     *
     * @param uri 要请求的地址
     * @param request 前台请求对象
     * @author piper
     * @data 2018/7/3 11:19
     */
    public HttpPost postMethod(String uri, HttpServletRequest request) {
        StringEntity entity = null;
        if (request.getContentType().contains("json")) {
            entity = jsonData(request);  //填充json数据
        } else {
            entity = formData(request);  //填充form数据
        }
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", request.getHeader("Content-Type"));
        httpPost.setEntity(entity);
        return httpPost;
    }

    /**
     * 处理post请求 form数据 填充form数据
     *
     * @param request 前台请求
     * @author piper
     * @data 2018/7/17 18:05
     */
    private UrlEncodedFormEntity formData(HttpServletRequest request) {
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            List<NameValuePair> pairs = new ArrayList<>();  //存储参数
            Enumeration<String> params = request.getParameterNames();  //获取前台传来的参数
            while (params.hasMoreElements()) {
                String name = params.nextElement();
                pairs.add(new BasicNameValuePair(name, request.getParameter(name)));
            }
            //根据参数创建参数体，以便放到post方法中
            urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, request.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlEncodedFormEntity;
    }

    /**
     * 处理post请求 json数据
     *
     * @param request 前台请求
     * @author piper
     * @data 2018/7/17 18:05
     */
    private StringEntity jsonData(HttpServletRequest request) {
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(request.getInputStream(), request.getCharacterEncoding());
            BufferedReader reader = new BufferedReader(is);
            //将json数据放到String中
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            //根据json数据创建请求体
            return new StringEntity(sb.toString(), request.getCharacterEncoding());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 下载网络文件---返回下载后的文件存储路径
     *
     * @param url 文件地址
     * @param dir 存储目录
     * @param fileName 存储文件名
     * @return
     */
    public static boolean downloadHttpUrl(String url, String dir, String fileName) {
        boolean flag=false;
        try {
            URL httpurl = new URL(url);
            HttpURLConnection urlcon = (HttpURLConnection) httpurl.openConnection();
            if(urlcon.getResponseCode()>=400){
                return flag;
            }
            File dirfile = new File(dir);
            if (!dirfile.exists()) {
                dirfile.mkdirs();
            }
            FileUtils.copyURLToFile(httpurl, new File(dir+fileName));
            flag=true;
        } catch (Exception e) {
            flag=false;
        }
        return flag;
    }

    /**
     * 方式二
     * 该方式支持本地文件，也支持http/https远程文件
     * @param fileUrl
     */
    public static String getContentType(String fileUrl) throws Exception {
        String extension=null;
        HttpClient client = HttpConnectionPoolUtil.getHttpClient(fileUrl);
        HttpGet httpGet = new HttpGet(fileUrl);
        HttpResponse response = client.execute(httpGet);
        String contentType=response.getEntity().getContentType().getValue();
        if(contentType.indexOf(";")!=-1){
            contentType=contentType.substring(0,contentType.indexOf(";"));
        }
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType mimeType = allTypes.forName(contentType);
        extension= mimeType.getExtension();
        return extension;
    }

}
