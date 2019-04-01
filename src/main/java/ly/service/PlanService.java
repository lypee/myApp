package ly.service;


import ly.common.ServerResponse;
import ly.pojo.Plan;
import ly.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public interface PlanService {
    /**
     * 为用户添加计划
     */
    int addPlanByUser(Plan plan);
    /**
     * 删除当前用户的对应计划
     */
    /**
     * 计划查重
     * @return true 该用户已经存在相同名称的计划
     */
    boolean checkRepeadByUserIdAndPlanContent(int userId , String planContent) ;
    /**
     * 根据planId 获得该计划完成的次数
     * @return 0
     */
    int getFinishTimesByPlanId(int planId) ;
    /**
     * 计划被完成一次
     */
    void finishOnceByPlanId(int planId) ;
    /**
     * 计划被完成
     */
    void setPlanIsFinish(int planId) ;
    /**
     * 获得本月计划的内容
     */
    List<String> getPlanContentThisMonth(User user);
    /**
     * 获得本周的计划内容
     */
    List<String> getPlanContentThisWeek(User user) ;
    /**
     * 根据planId 删除计划
     */
    ServerResponse delPlanByPlanId(Integer planId) ;
}
