package ly.service.Impl;

import ly.common.ServerResponse;
import ly.dao.UserMapper;
import ly.pojo.User;
import ly.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService  {
    @Autowired
    private UserMapper userMapper ;

    /**
     *
     * @param userOpenId
     * @return 建造了userId的user .
     */
    @Override
    public ServerResponse<User> login(String userOpenId , String userName) {
        //用户名或密码为空
        if (StringUtils.isEmpty(userOpenId)) {
            return ServerResponse.createByError();
        }

        //操作是否合法  :
        Integer userId = userMapper.CheckLogin(userOpenId);
        User user = new User.userBuilder().buildUserId(userId).buildUserOpenId(userOpenId).build();
        //用户不存在
        if (userId == -1){
            Integer res = userMapper.insertUser(userOpenId  , userName) ;
            return res  > 0 ? ServerResponse.createBySuccess(user) : ServerResponse.createByError() ;
        }

//        user.setUserPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);


    }


    /**
     * 根据用户id获取userName
     * @param userId
     * @return
     */
    @Override
    public String getUserNameByUserId(int userId) {
        return userMapper.getUserNameByUserId(userId) ;
    }

    @Override
    public User findUserByUserId(int userId) {
        return userMapper.findUserByUserid(userId) ;
    }

    @Override
    public Integer findUserIdByUserOpenId(String userOpenId) {
        return userMapper.getUserIdByUserOpenId(userOpenId) ;
    }


}
