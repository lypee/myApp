package ly.controller;


import ly.common.Const;
import ly.common.ResponseCode;
import ly.common.ServerResponse;
import ly.pojo.Plan;
import ly.pojo.User;
import ly.service.PlanService;
import ly.service.UserService;
import ly.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/plan")
public class planController extends BasicController{
    @Autowired
    private UserService userService ;
//    @Autowired
//    private PlanMapper planMapper ;
    @Autowired
    private PlanService planService ;
    private UUID uuid  = UUID.randomUUID() ;
    @PostMapping("/addNewPlan")
    @ResponseBody
    public ServerResponse addNewPlan(HttpSession httpSession ,String planContent) {
        User user = (User)httpSession.getAttribute(Const.CURRENT_USER);
        Plan plan = new Plan.planBuilder().buildUserId(user.getUserId()).buildUserName(user.getUserName()).buildPlanContent(planContent).buildStatus(0).build() ;
        if (!StringUtils.isBlank(userService.getUserNameByUserId(plan.getUserId()))) {
//            System.out.println(userService.getUserNameByUserId(plan.getUserId()));
            //返回值有效 -->用户存在 继续操作
            //注入UserName ;
//            User user = null ;
//            user.setUserName(userService.getUserNameByUserId(plan.getUserId()));
            //新计划查重
            if(planService.checkRepeadByUserIdAndPlanContent(plan.getUserId() , plan.getPlanContent()) )
            {
                return ServerResponse.createByErrorMessage("您已经存在相同计划了噢") ;
            }
            int ansNumber = planService.addPlanByUser(plan) ;
            if(ansNumber > 0 )
            {
                return ServerResponse.createBySuccess("添加计划成功");
            }else {
                return ServerResponse.createByErrorMessage("添加计划失败");
            }
        }else{
            return ServerResponse.createByErrorMessage("当前" +
                    "计划不存在对应用户,请重试");
        }
    }

    @PostMapping("/delPlanByPlanId")
    @ResponseBody
    public ServerResponse delPlanByPlanId(HttpSession httpSession,@RequestBody Plan plan) {
        if(plan == null || plan.getPlanId() == 0 ) return  ServerResponse.createByErrorMessage("传入错误参数");
        else{
            if(planService.delPlanByPlanId(plan.getPlanId()).isSuccess())
            {
                return ServerResponse.createBySuccess("成功删除计划");
            }
            return ServerResponse.createByErrorMessage("删除失败或不存在该计划") ;
        }
    }
    @PostMapping("/getFinishTimesByPlanId")
    @ResponseBody
    @CachePut(value = "finishTimes" , key = "getTargetClass() +getMethodName() + #p0")
    public ServerResponse getFinishTimesByPlanId(@RequestBody Integer planId) {
//        int planId = plan.getPlanId() ;
        int ansCount = planService.getFinishTimesByPlanId(planId) ;
      if(ansCount  == -1)
      {
          return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode() ,"非法参数");
      }
        //此处也可以返回一个plan 将传入的plan的finishTimes设置为 从mysql中读取的值
        return ServerResponse.createBySuccess(ansCount) ;
    }
    /**
     * 计划完成一次
     * 可叠加
     *
     */
    @PostMapping(value = "/finishOnceByPlanId",consumes = "application/json")
    @ResponseBody
    public ServerResponse finishOnceByPlanId(@RequestBody Plan plan , HttpSession httpSession , HttpServletRequest httpServletRequest )//,@RequestBody  Plan plan
    {

        Integer planId = plan.getPlanId() ;
        if(planId < 1 )
        {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode() , "传入非法参数");
        }
//        String callback = httpServletRequest.getParameter("callback") ;
//        Plan plan = (Plan)httpServletRequest.getAttribute("planId");
//        System.out.println("请求的UserName: "  + plan.getUserName());
       try{
           System.out.println("请求的id: "  + planId);
       }catch (Exception e)
       {
           e.printStackTrace();
       }
        planService.finishOnceByPlanId(planId);
        return ServerResponse.createBySuccess("该计划又完成了一次噢~") ;
    }

    /**
     * 计划状态更改
     */
    @PostMapping("/setPlanIsFinish")
    @ResponseBody
public ServerResponse setPlanIsFinish(@RequestBody  Plan plan)
    {
        planService.setPlanIsFinish(plan.getPlanId());
        return ServerResponse.createBySuccess("已完成") ;
    }
    /**
     * 获得该用户本月的全部计划
     *      * 如果获得计划数量 不需要再写mapper 直接get List.size 就行 . 下同
     */
    @PostMapping("/getPlanContentThisMonth")
    @ResponseBody
    public ServerResponse getPlanContentThisMonth(@RequestBody  User user , String userToken)
    {
        //验证User是否合法 -->ttl 权限

            if(checkIsLoginByUserToken(user , userToken).isSuccess()) {
                return ServerResponse.createBySuccess(planService.getPlanContentThisMonth(user));
            }
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEET_LOGIN.getCode() , "需要登陆或登陆信息错误");
    }
    /**
     * 获得用户的本周全部计划
.
     */
    @PostMapping("/getPlanContentThisWeek")
    @ResponseBody
    public ServerResponse getPlanContentThisWeek(@RequestBody  User user ,String userToken)
    {
        if(checkIsLoginByUserToken(user , userToken).isSuccess()){
            //判断本周是否有计划
            List<String> ansList = planService.getPlanContentThisWeek(user) ;
            if(ansList == null || ansList.size() == 0){
                return ServerResponse.createByErrorMessage("本周无计划噢~") ;
            }else {
                return ServerResponse.createBySuccess(ansList);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEET_LOGIN.getCode() , "需要登陆或登陆信息错误");
    }

    /**
     * 获得本周的计划总数
     */
    @PostMapping("getCountOfPlanThisWeek")
    public ServerResponse getCountOfPlanThisWeek(HttpSession httpSession) {
        User user = (User)httpSession.getAttribute(Const.CURRENT_USER) ;
        String userTokne = (String)httpSession.getAttribute(Const.USER_TOKEN);
        if (checkIsLoginByUserToken(user , userTokne).isSuccess()) {
            //获得对应用户的本周计划数量
            return ServerResponse.createBySuccess(planService.getPlanContentThisWeek(user).size()) ;
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEET_LOGIN.getCode() , "需要登陆 ") ;
    }
    /**
     * 根据redis key 查看用户是否处于登陆状态 / 合法用户
     * @return true:  : 合法状态  ;
     * @return false : 非法状态  ;
     */
    private ServerResponse checkIsLoginByUserToken(User user, String userToken) {


        User newUser = JsonUtil.string2Obj(shardedJedis.get(Const.USER_REDIS_SESSION+":"+userToken) , User.class) ;
        if(shardedJedis.exists(Const.USER_REDIS_SESSION+ ":" +userToken) && StringUtils.equals(user.getUserName(),newUser.getUserName())) {
            //判断获取的string 和userName是否相同 防止横向越权
            return ServerResponse.createBySuccess() ;
        }
        return ServerResponse.createByError()  ;

        }


}
