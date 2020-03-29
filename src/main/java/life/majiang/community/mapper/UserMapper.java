package life.majiang.community.mapper;


import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
  @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
  void insert(User user);

  @Select("select * from user u where u.token = #{token}")
  User findBytoken(@Param("token")String token);  //是类到时候把token放进sql 不是类写注解

}
