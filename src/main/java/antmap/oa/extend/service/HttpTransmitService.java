package antmap.oa.extend.service;

import antmap.oa.extend.model.Result;

import javax.servlet.http.HttpServletRequest;

public interface HttpTransmitService {
    Result postHttp(String url, HttpServletRequest request);

    Result httpFile(String fileUrl,String fileType);
}
