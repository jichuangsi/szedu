package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.ICourseWareMapper;
import cn.com.szedu.entity.Course;
import cn.com.szedu.entity.IntermediateTable.CourseUserRelation;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.ICourseRepository;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ICourseUserRelationRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BackCourseService {
    @Resource
    private ICourseRepository courseRepository;
    @Resource
    private ICourseWareMapper courseWareMapper;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private ICourseUserRelationRepository courseUserRelationRepository;

    //添加课堂
    @Transactional(rollbackFor = Exception.class)
    public void addCourse(UserInfoForToken userInfo, Course course) throws UserServiceException {
        if (StringUtils.isEmpty(course.getCourseTitle())|| StringUtils.isEmpty(course.getStartTime())
                || StringUtils.isEmpty(course.getSubject()) || StringUtils.isEmpty(course.getContent())
                || StringUtils.isEmpty(course.getCourseTimeLength())|| StringUtils.isEmpty(course.getTeacherId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Course course1=courseRepository.save(course);
        //中间表
        CourseUserRelation courseUserRelation=new CourseUserRelation(course1.getId(),course1.getTeacherId());
        courseUserRelationRepository.save(courseUserRelation);
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加课堂");
        opLogRepository.save(opLog);
    }

    //修改课堂信息
    public void updateCourse(Course course) throws UserServiceException {
        if (StringUtils.isEmpty(course.getCourseTitle())|| StringUtils.isEmpty(course.getStartTime())
                || StringUtils.isEmpty(course.getSubject()) || StringUtils.isEmpty(course.getContent())
                || StringUtils.isEmpty(course.getCourseTimeLength())|| StringUtils.isEmpty(course.getTeacherId())
                || StringUtils.isEmpty(course.getId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Course course1=courseRepository.findFirstByid(course.getId());
        course1.setCourseTitle(course.getCourseTitle());
        course1.setSubject(course.getSubject());
        course1.setTeacherId(course.getTeacherId());
        course1.setCourseTimeLength(course.getCourseTimeLength());
        course1.setStartTime(course.getStartTime());
        course1.setContent(course.getContent());
        courseRepository.save(course1);
    }

    //删除课堂信息
    public void deleteCourse(UserInfoForToken userInfo,String id) throws UserServiceException {
        if (StringUtils.isEmpty(id)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        courseRepository.deleteById(id);
        OpLog opLog=new OpLog(userInfo.getUserName(),"删除","删除课堂");
        opLogRepository.save(opLog);
    }


    //课堂列表
    public PageInfo<Course> getCourseList(String name, Integer pageNum, Integer pageSize){
        List<Course> coursesList=null;
        if (!(StringUtils.isEmpty(name))){
            coursesList=courseWareMapper.getAllCourseByName("%"+name+"%");
        }else {
            coursesList=courseWareMapper.getAllCourse();
        }
        PageInfo page=new PageInfo();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        page.setTotal(coursesList.size());
        page.setList(coursesList);
        return page;
    }

    //添加课堂2
    public void  addCourse2(UserInfoForToken userInfoForToken,Course course){
        courseRepository.save(course);
        //添加中间表

    }
}
