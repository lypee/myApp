package ly.dao;

import ly.pojo.Plan;
import ly.pojo.User;
import ly.utils.MyMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface PlanMapper extends MyMapper<Plan> {

    /**
     * 为用户添加计划
     */
   @Insert("INSERT INTO `plan` (`user_id` , `user_name` , plan_content) values(#{userId},#{userName},#{planContent})" )
    public int createNewPlanForUser(Plan plan);
 /**
  * 删除计划
  * @return 0 : 失败 ; 1 成功
  */
  @Delete ("delete from `plan` where plan_id = #{planId}")
  public int delPlanByPlanId(Integer planId) ;
 /**
     * 根据userId and plan_content 查重
     */
    @Select("SELECT ifnull(max(user_id),-1) as user_id  FROM plan where user_id = #{userId} and plan_content = #{planContent} limit 1 ")
    public int checkRepeadByUserIdAndPlanContent(int userId , String planContent);
    /**
     * 根据计划id获得被完成的次数
     * @return -1 : planId 为非法值 .
     */
    @Select("select IFNULL(max(finish_times),-1) as finish_times from plan where plan_id  = #{planId};")
    public int getFinishTimesByPlanId(int planId) ;
    /**
     * 对应计划被完成一次
     */
    @Update("UPDATE plan SET `finish_times` = `finish_times` + 1 WHERE plan_id = #{plan_id}")
    public void finishOnceByPlanId(int planId) ;

 /**
  * 将计划设置为已完成(status == 1)
  * @param planId
  */
    @Update("update plan set `status` = 1 where plan_id = #{planId}")
    void setPlanIsFinish(int planId) ;
 /**
  * 查询本月内制定的计划
  */
 @Select("select plan_content from plan where DATE_FORMAT(create_time ,'%Y%m') = DATE_FORMAT(CURDATE() , '%Y%m')  AND user_name = #{userName}")
  List<String> getPlanContentThisMonth(User user);

 /**
  * 查询本周的计划
  */
 @Select("SELECT plan_content FROM plan WHERE YEARWEEK(DATE_FORMAT(create_time , '%Y-%m-%d')) = YEARWEEK(NOW())  and user_name = #{userName}")
 List<String> getPlanContentThisWeek(User user) ;
}
