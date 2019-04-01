package ly.service.Impl;

import ly.common.ServerResponse;
import ly.dao.PlanMapper;
import ly.pojo.Plan;
import ly.pojo.User;
import ly.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanMapper planMapper ;
    @Override
    public int addPlanByUser(Plan plan) {
        return planMapper.createNewPlanForUser(plan) ;
    }

    @Override
    public boolean checkRepeadByUserIdAndPlanContent(int userId, String planContent) {
        //该用户存在相同计划
       if(planMapper.checkRepeadByUserIdAndPlanContent(userId  , planContent) >  0 ) return true ;
       //该用户不存在相同计划1
       return false ;
    }

    @Override
    public int getFinishTimesByPlanId(int planId) {
        return planMapper.getFinishTimesByPlanId(planId) ;
    }

    @Override
    public void finishOnceByPlanId(int planId) {
        planMapper.finishOnceByPlanId(planId);
    }

    @Override
    public void setPlanIsFinish(int planId) {
        planMapper.setPlanIsFinish(planId);
    }

    @Override
    public List<String> getPlanContentThisMonth(User user) {
        List<String> ansList = planMapper.getPlanContentThisMonth(user) ;
        return ansList ;
    }

    @Override
    public List<String> getPlanContentThisWeek(User user) {
        List<String> ansList = planMapper.getPlanContentThisWeek(user);
        return ansList ;
    }

    @Override
    public ServerResponse delPlanByPlanId(Integer planId) {
       int ansNumber = planMapper.delPlanByPlanId(planId) ;
       //只有为1 才是成功删除 ; 0 -->删除失败或无返回值
       if(ansNumber == 1 ) {
           return ServerResponse.createBySuccess();
       }
       return ServerResponse.createByError();
    }

}
