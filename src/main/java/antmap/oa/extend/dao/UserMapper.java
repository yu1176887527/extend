package antmap.oa.extend.dao;

import antmap.oa.extend.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectByPrimaryKey(Integer id);
}
