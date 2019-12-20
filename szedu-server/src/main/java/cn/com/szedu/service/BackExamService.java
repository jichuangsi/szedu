package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.ICourseWareMapper;
import cn.com.szedu.entity.Exam;
import cn.com.szedu.entity.IntermediateTable.ExamUserRelation;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IExamRepository;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IExamUserRelationRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BackExamService {
    @Resource
    private IExamRepository examRepository;

    @Resource
    private ICourseWareMapper courseWareMapper;

    @Resource
    private IOpLogRepository opLogRepository;

    @Resource
    private IExamUserRelationRepository examUserRelationRepository;

    //添加考试
    @Transactional(rollbackFor = Exception.class)
    public void addExam(UserInfoForToken userInfo, Exam exam) throws UserServiceException {
        if (StringUtils.isEmpty(exam.getExamName())|| StringUtils.isEmpty(exam.getStartTime())
                || StringUtils.isEmpty(exam.getCourse()) || StringUtils.isEmpty(exam.getCreatorId())
                || StringUtils.isEmpty(exam.getTestTimeLength())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Exam exam1= examRepository.save(exam);
        //插入中间表
        ExamUserRelation relation=new ExamUserRelation(exam1.getId(),exam1.getCreatorId());
        examUserRelationRepository.save(relation);
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加考试");
        opLogRepository.save(opLog);
    }

    //修改考试信息
    public void updateExam(Exam exam) throws UserServiceException {
        if (StringUtils.isEmpty(exam.getExamName())|| StringUtils.isEmpty(exam.getStartTime())
                || StringUtils.isEmpty(exam.getCourse()) || StringUtils.isEmpty(exam.getTestTimeLength())
                || StringUtils.isEmpty(exam.getId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Exam exam1=examRepository.findFirstByid(exam.getId());
        exam1.setExamName(exam.getExamName());
        exam1.setCourse(exam.getCourse());
        exam1.setStartTime(exam.getStartTime());
        exam1.setTestTimeLength(exam.getTestTimeLength());
        examRepository.save(exam1);
    }

    //删除考试信息
    public void deleteExam(UserInfoForToken userInfo,String id) throws UserServiceException {
        if (StringUtils.isEmpty(id)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        examRepository.deleteById(id);
        OpLog opLog=new OpLog(userInfo.getUserName(),"删除","删除考试");
        opLogRepository.save(opLog);
    }


    //考试列表
    public PageInfo<Exam> getExamList(String name,Integer pageNum,Integer pageSize){
        List<Exam> examList=null;
        if (!(StringUtils.isEmpty(name))){
            examList=courseWareMapper.getAllExamByname("%"+name+"%");
        }else {
            examList=courseWareMapper.getAllExam();
        }
        PageInfo page=new PageInfo();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        page.setTotal(examList.size());
        page.setList(examList);
        return page;
    }
}
