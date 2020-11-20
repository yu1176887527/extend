package antmap.oa.extend.service.impl;

import antmap.oa.extend.dao.UserMapper;
import antmap.oa.extend.model.Result;
import antmap.oa.extend.model.User;
import antmap.oa.extend.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public Result getUser(Integer id){
        Result result=new Result();
        try{
            User user=userMapper.selectByPrimaryKey(id);
            result.setData(user);
        }catch (Exception ex){
            result.setSuccess(false);
            result.setMessage(ex.toString());
        }
        return result;
    }
}
