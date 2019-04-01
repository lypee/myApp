package ly.service;

import ly.common.ServerResponse;
import ly.pojo.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public interface UserService   {

    /**
     * 用户登陆 只判断用户是否合法 不做其它判断
     * 仅做第一个登陆的接口的判断
     * @return User != null : success
     */
     ServerResponse<User> login(String userOpenId , String userName) ;

    /**
     * 根据userId 获取UserName
     * @return 非null: 用户存在
     */
    String getUserNameByUserId(int userId) ;
    /**
     * 根据userId查找用户
     * @return User == Null : 用户不存在
     */
    User findUserByUserId(int userId);
    /**
     * 根据用户openId查找用户id
     * @return null || 0 : 用户不存在
     */
    Integer findUserIdByUserOpenId(String userOpenId) ;

}
