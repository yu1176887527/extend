package antmap.oa.extend.service.impl;

import antmap.oa.extend.model.Result;
import antmap.oa.extend.service.HttpTransmitService;
import antmap.oa.extend.utils.Function;
import antmap.oa.extend.utils.HttpConnectionPoolUtil;
import antmap.oa.extend.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Service
public class HttpTransmitServiceImpl implements HttpTransmitService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HttpTransmitService.class);

    @Override
    public Result postHttp(String url, HttpServletRequest request){
        Result result = new Result();
        String msg;
        try{
            HttpClient client = HttpConnectionPoolUtil.getHttpClient(url);            //client对象
            HttpUtils httpUtils=new HttpUtils();
            HttpPost httpPost =httpUtils.postMethod(url,request);    //创建get请求
            HttpResponse response = client.execute(httpPost);   	//执行post请求
            int code = response.getStatusLine().getStatusCode();
            String r = EntityUtils.toString(response.getEntity());
            if (code == HttpStatus.SC_OK) {
                log.info("调用请求转发服务成功");
                result.setMessage("响应成功");
                result.setData(r);
            } else {
                log.info("调用请求转发服务失败");
                result.setSuccess(false);
                result.setMessage("响应失败");
            }
        }catch (Exception ex){
            msg=ex.toString();
            log.info(msg);
            result.setSuccess(false);
            result.setMessage(msg);
        }
        return result;
    }

    @Override
    public Result httpFile(String fileUrl,String fileType){
        Result result=new Result();
        result.setSuccess(false);
        String msg;
        try{
            log.info("开始下载网络文件:"+fileUrl);
            if(fileUrl==null||fileUrl.isEmpty()){
                msg="调用文件下载未传递参数fileUrl";
                log.info(msg);
                result.setMessage(msg);
                return result;
            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String tempDir=Function.getTempfileDir();
            String filedir=tempDir+sdf.format(new Date());
            File dir=new File(filedir);
            if(!dir.exists()){
                log.info("创建目录:"+filedir);
                dir.mkdir();
            }
            filedir=filedir+File.separator;
            if(fileType==null||fileType.isEmpty()){
                log.info("未传递参数fileType,开始自动获取网络文件的文件类型");
                fileType=HttpUtils.getContentType(fileUrl);
                log.info("解析文件数据判断文件类型为:"+fileType);
            }else if(!fileType.startsWith(".")){
                msg="调用文件下载参数fileType必须以.开头";
                log.info(msg);
                result.setMessage(msg);
                return result;
            }
            String filename=Function.getGuid()+fileType;
            boolean isSuccess=HttpUtils.downloadHttpUrl(fileUrl,filedir,filename);
            String fileFullPath=filedir+filename;
            if(!isSuccess){
                log.info("无法将网络文件存入本地,原因：网络文件不存在;网络文件地址:"+fileUrl);
                result.setMessage("文件不存在："+fileUrl);
                return result;
            }
            log.info("成功将网络文件存入本地，地址："+fileFullPath);
            String fileBase64Str=Function.encryptToBase64(fileFullPath);
            if(fileBase64Str==null){
                msg="文件转base64失败";
                log.info(msg);
                result.setMessage(msg);
                return result;
            }
            HashMap data=new HashMap();
            data.put("fileBase64Str",fileBase64Str);
            data.put("fileType",fileType);
            result.setData(data);
            msg="网络文件下载并转换成功";
            log.info(msg);
            result.setMessage(msg);
            result.setSuccess(true);
        }catch (Exception ex){
            result.setSuccess(false);
            msg=ex.toString();
            log.info(msg);
            result.setMessage(msg);
        }
        return result;
    }
}
