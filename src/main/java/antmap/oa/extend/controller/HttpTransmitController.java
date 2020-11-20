package antmap.oa.extend.controller;

import antmap.oa.extend.model.Result;
import antmap.oa.extend.service.HttpTransmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("transmit") //http转发服务
public class HttpTransmitController {
    @Autowired
    private HttpTransmitService httpTransmitService;

    /**
     * 转发post请求
     * @param url 转发地址
     * @param request 转发数据
     * @return
     */
    @PostMapping("posthttp")
    @ResponseBody
    public Result postHttp(String url,HttpServletRequest request){
        return httpTransmitService.postHttp(url,request);
    }

    @PostMapping("httpfile")
    @ResponseBody
    public Result httpFile(String fileUrl,String fileType){
        return httpTransmitService.httpFile(fileUrl,fileType);
    }
}
