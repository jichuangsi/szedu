package cn.com.szedu.service.impl;

import cn.com.szedu.entity.IntermediateTable.SchoolCurriculumRelation;
import cn.com.szedu.entity.IntermediateTable.SchoolUserRelation;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.entity.SchoolInfo;
import cn.com.szedu.model.student.SchoolInfoModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.ISchoolInfoRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ISchoolCurriculumRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ISchoolUserRelationRepository;
import cn.com.szedu.service.IBackSchoolService;
import cn.com.szedu.service.UserInfoService;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BackSchoolServiceImpl implements IBackSchoolService {
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ISchoolUserRelationRepository schoolUserRelationRepository;

    @Resource
    private ISchoolCurriculumRelationRepository schoolCurriculumRelationRepository;

    @Resource
    private IOpLogRepository opLogRepository;

    @Override
    public void addSchool(UserInfoForToken userInfo,SchoolInfoModel model) {
        //保存学校信息
        SchoolInfo info=schoolInfoRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMSCHOOLINFOMODELSCHOOLINFO(model));
        for (SchoolCurriculumRelation r:model.getSchoolCurriculumRelations()) {
            r.setSchoolId(info.getId());
        }
        //保存学校可使用的课程信息
        schoolCurriculumRelationRepository.saveAll(model.getSchoolCurriculumRelations());
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加考试");
        opLogRepository.save(opLog);
    }

    //学校内添加教师
    @Override
    public void saveTeacherBySchool(String schoolId, String teacherId) {
        SchoolUserRelation relation=new SchoolUserRelation();
        relation.setSchoolId(schoolId);
        relation.setUid(teacherId);
        schoolUserRelationRepository.save(relation);
    }
}
