package cn.com.szedu.service;

import cn.com.szedu.entity.CourseWareShare;
import cn.com.szedu.entity.UserPosition;
import cn.com.szedu.repository.ICourseWareShareRespository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseWareShareService {
    @Resource
    private ICourseWareShareRespository courseWareShareRespository;

    @Transactional(rollbackFor = Exception.class)
    public void saveCourseWareShare(CourseWareShare courseWareShare){
        courseWareShareRespository.save(courseWareShare);
    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourseWareShare(String id){
        courseWareShareRespository.deleteByid(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCourseWareShareByCourseWareId(String id){
        courseWareShareRespository.deleteByid(id);
    }

    public List<CourseWareShare> findcourseWareIdsBy(List<UserPosition> userPositions){
        List<String> courseWareIds=new ArrayList<>();
        List<CourseWareShare> courseWareShares=new ArrayList<>();
        if(userPositions!=null){
            for (UserPosition user:userPositions) {
                List<CourseWareShare> userCourseWareShares=courseWareShareRespository.findByGradeidAndSubjectid(user.getGradeid(),user.getSubjectid());
                userCourseWareShares.forEach(courseWareShare ->
                        courseWareShares.add(courseWareShare)
                );
            }
        }
        return  courseWareShares;
    }
    //分页
    public List<CourseWareShare> findcourseWareIdsByPage(List<UserPosition> userPositions ,int pageNum, int pageSize){
        List<String> courseWareIds=new ArrayList<>();
        List<CourseWareShare> courseWareShares=new ArrayList<>();
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        if(userPositions!=null){
            for (UserPosition user:userPositions) {
                Page<CourseWareShare> page=courseWareShareRespository.findAll((Root<CourseWareShare> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
                    List<Predicate> predicateList = new ArrayList<>();
                    if(courseWareIds!=null && courseWareIds.size()!=0){
                        CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("id"));
                        for (String id : courseWareIds) {
                            in.value(id);
                        }
                        predicateList.add(in);
                    }
                    return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
                },pageable);
                List<CourseWareShare> userCourseWareShares=courseWareShareRespository.findByGradeidAndSubjectid(user.getGradeid(),user.getSubjectid());
                userCourseWareShares.forEach(courseWareShare ->
                        courseWareShares.add(courseWareShare)
                );
            }
        }
        return  courseWareShares;
    }
}
