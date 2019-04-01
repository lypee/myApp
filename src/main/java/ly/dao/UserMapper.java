package ly.dao;

import ly.pojo.User;
import ly.utils.MyMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends MyMapper<User> {
    /**
     * 检查数据库中是否有改用户
     * @return true : 返回userId  ; false : -1  ;
     */
    @Select("SELECT ifnull(max(`user_id`),-1)  as userId FROM `user` WHERE open_id = #{userOpenId} ")
    Integer CheckLogin(String userOpenId ) ;
    /**
     * 插入用户
     */
    @Insert("insert into `user`(open_id , user_name) values(#{userOpenId} , #{userName})")
    int insertUser(String userOpenId , String userName);
    /**
     * 根据每个用户的唯一openid获得对应的user_id
     */
    @Select("select ifnull(max(`user_id`) ,-1 ) as `user_id` from `user`  where open_id = #{userOpenId}" )
    int getUserIdByUserOpenId(String UserOpenId);
    /**
     * 根据用户id获取用户ing
     */
    @Select("select user_name from `user` where user_id = #{userId}")
    String getUserNameByUserId(int userId) ;
    /**
     * 根据Id查找用户
     */
    @Select("select user_id as userId  , user_name as userName , open_id as userOpenId from `user` where user_id = #{userId}")
    User findUserByUserid(int userId) ;

    /**
     * 获取用户的等级
     * @return 0 :普通用户 ; 3 管理员 ; 5 超级管理员 ; -1 不存在用户
     */
    @Select ("select ifnull(max(role),-1) as role from `user` where open_id = #{userOpenId}")
    Integer getUserLevelByUserOpenId(String userOpenId);
}
