package antmap.oa.extend.controller;

import antmap.oa.extend.model.Result;
import antmap.oa.extend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")//测试类
public class TestController {
    @Autowired
    private UserService userService;

    @RequestMapping("getuser")
    @ResponseBody
    public Result getUser(Integer id){
        return userService.getUser(id);
    }
}
