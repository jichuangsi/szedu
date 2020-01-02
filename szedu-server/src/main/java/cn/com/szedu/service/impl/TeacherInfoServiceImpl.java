package cn.com.szedu.service.impl;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.LoginMap;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.CourseUserRelation;
import cn.com.szedu.entity.IntermediateTable.MessageUserRelation;
import cn.com.szedu.entity.IntermediateTable.ResourceTeacherInfoRelation;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.IDNameModel;
import cn.com.szedu.model.SandMessageModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.MessageModel;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.ICourseUserRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IMessageUserRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IResourceTeacherInfoRelationRepository;
import cn.com.szedu.service.BackTokenService;
import cn.com.szedu.service.TeacherInfoService;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TeacherInfoServiceImpl implements TeacherInfoService {

    @Resource
    private ITeacherInfoRepository teacherInfoRepository;
    @Resource
    private BackTokenService backTokenService;
    @Resource
    private IResourceTeacherInfoRelationRepository resourceTeacherInfoRelationRepository;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private IntegralRecordRepository integralRecordRepository;
    @Resource
    private IMessageUserRelationRepository messageUserRelationRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private IIntegralRuleRepository integralRuleRepository;
    @Resource
    private IClassInfoMapper classInfoMapper;
    @Resource
    private ICourseUserRelationRepository courseUserRelationRepository;
    //记录登录用户


    @Override
    /**
     * 老师登录
     */
    public TeacherModel loginTeacher(TeacherModel model) throws UserServiceException {
        TeacherModel model1 = null;
        if (StringUtils.isEmpty(model.getAcount()) || StringUtils.isEmpty(model.getPassword())) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        TeacherInfo userInfo = teacherInfoRepository.findByAccountAndPwd(model.getAcount(), Md5Util.encodeByMd5(model.getPassword()));
        if (userInfo == null) {
            throw new UserServiceException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        List<CourseUserRelation> tcr = courseUserRelationRepository.findAllByUid(userInfo.getId());//老师课堂
        List<ResourceTeacherInfoRelation> trr = resourceTeacherInfoRelationRepository.findByTeacherId(userInfo.getId());//老师资源
        String user = JSONObject.toJSONString(MappingEntity3ModelCoverter.CONVERTERFROMBACKUSERUSER(userInfo));
        try {
            String acc = backTokenService.createdToken(user);
            model1 = MappingEntity3ModelCoverter.CONVERTERFROMBACKTEACHERINFO(userInfo);
            model1.setAccessToken(acc);
            model1.setCountCourse(tcr.size());
            model1.setCountResource(trr.size());
            Integer integral = qiandao(userInfo.getId(), "签到");
            if (integral > 0) {
                model1.setSignin(false);//不能签
            } else {
                model1.setSignin(true);//能签
            }

          Map<String,Long> p=new HashMap<>();
            p.put(userInfo.getId(), System.currentTimeMillis() / 1000);
            LoginMap.setMap(p);
            return model1;
        } catch (UnsupportedEncodingException e) {
            throw new UserServiceException(e.getMessage());
        }

    }

    /**
     * 签到积分
     *
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    @Override
    public Integer signin(UserInfoForToken userInfo) throws UserServiceException {
        //修改老师积分
        TeacherInfo teacherInfo = teacherInfoRepository.findExsitById(userInfo.getUserId());
        if (teacherInfo.getIntegral() == null) {
            teacherInfo.setIntegral(0);
        }
        Integer integral = qiandao(userInfo.getUserId(), "签到");
        if (integral <= 0) {
            teacherInfo.setIntegral(teacherInfo.getIntegral() + 1);//老师等级积分
            teacherInfoRepository.save(teacherInfo);
            //积分记录
            IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                    "签到", "老师登录签到", userInfo.getUserId(), userInfo.getUserName(),
                    1, System.currentTimeMillis());
            integralRecordRepository.save(integralRecord);
            //系统信息
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String s = df.format(new Date());// new Date()为获取当前系统时间

            String messages = "您今日已成功签到获得1积分。" + s;
            Message message = new Message(teacherInfo.getId(), teacherInfo.getName(), messages, "N");
            messageRepository.save(message);
        }/*else {
            throw new UserServiceException(ResultCode.SIGNIN_IS_EXIST);
        }*/
        TeacherInfo teacherInfo1 = teacherInfoRepository.findExsitById(userInfo.getUserId());
        return teacherInfo1.getIntegral();
    }

    /**
     * 查看老师积分
     *
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     * @throws UserServiceException
     */
    @Override
    public Page<IntegralRecord> getTeacherIntegral(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException {
        pageNum = pageNum - 1;
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<IntegralRecord> records = integralRecordRepository.findAll((Root<IntegralRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("operatorId"), userInfo.getUserId()));
                /*if(!StringUtils.isEmpty(userInfo.getUserName())){
                    predicateList.add(criteriaBuilder.equal(root.get("operatorName"),userInfo.getUserName()));
                }*/
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return records;
    }

    /**
     * 查看系统消息
     *
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<Message> getTeacherMessage(UserInfoForToken userInfo, int pageNum, int pageSize) {
        pageNum = pageNum - 1;
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<Message> records = messageRepository.findAll((Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("recipientId"), userInfo.getUserId()));
            predicateList.add(criteriaBuilder.equal(root.get("send"), "Y"));//已发送
            predicateList.add(criteriaBuilder.equal(root.get("senderid"), null));//系统
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return records;
    }

    /**
     * 未读消息数(根据老师)
     *
     * @param userInfo
     * @return
     */
    @Override
    public Integer getTeacherMessageCount(UserInfoForToken userInfo) {
        return messageRepository.countByRecipientIdAndAlreadyReadIsFalse(userInfo.getUserId());
    }

    /**
     * 30分钟加积分
     *
     * @param
     * @throws UserServiceException
     */
   /* @Override
    public void getUserLogin(UserInfoForToken userInfo) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        long end = System.currentTimeMillis();
        //修改老师积分
        TeacherInfo teacherInfo = teacherInfoRepository.findExsitById(userInfo.getUserId());
        if (userInfo.getLoginTime() + 30 >= end) {
            System.out.println("40秒");
            Integer integral = qiandao(userInfo.getUserId(), "在线");
            if (integral <= 0) {
                teacherInfo.setIntegral(teacherInfo.getIntegral() + 5);//老师等级积分
                teacherInfoRepository.save(teacherInfo);
                //积分记录
                IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                        "在线", "老师在线30分钟", userInfo.getUserId(), userInfo.getUserName(),
                        5, System.currentTimeMillis());
                integralRecordRepository.save(integralRecord);
            }
        }
    }*/

    /**
     * 在线30分钟积分记录
     *
     * @param userId
     */
    @Override
    public void getUserLogin(String userId) {
        IntegralRule integralRule = integralRuleRepository.findByRoleAndType("老师", "在线");
        //修改老师积分
        TeacherInfo teacherInfo = teacherInfoRepository.findExsitById(userId);
        Integer integral = qiandao(userId, "在线");
        if (integral <= 0) {
            teacherInfo.setIntegral(teacherInfo.getIntegral() + integralRule.getIntegral());//老师等级积分
            teacherInfoRepository.save(teacherInfo);
            //积分记录
            IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                    "在线", "老师在线30分钟", userId, teacherInfo.getName(),
                    integralRule.getIntegral(), System.currentTimeMillis());
            integralRecordRepository.save(integralRecord);
        }
    }

    /**
     * 已读系统信息
     *
     * @param userInfo
     * @throws UserServiceException
     */
    @Override
    public void updateTeacherMessage(UserInfoForToken userInfo) throws UserServiceException {
        Message message = messageRepository.findByRecipientIdAndSenderid(userInfo.getUserId(), null);//系统信息
        message.setAlreadyRead(true);
        messageRepository.save(message);//已读
    }


    /**
     * 积分规则
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<IntegralRule> integralRule(int pageNum, int pageSize) {
        pageNum = pageNum - 1;
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<IntegralRule> records = integralRuleRepository.findAll((Root<IntegralRule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return records;
    }

    /**
     * 给老师发送系统信息(定时30秒查一次)
     *
     * @param userInfo
     * @return
     */
    @Override
    public Integer sendMessageByTeacher(UserInfoForToken userInfo) {
        Integer count = messageRepository.countByRecipientIdAndSendAndSenderid(userInfo.getUserId(), "N", null);
        Message message = messageRepository.findByRecipientIdAndSendAndSenderid(userInfo.getUserId(), "N", null);
        message.setSend("Y");//修改为发送状态
        messageRepository.save(message);
        return count;//返回发送消息数
    }

    /**
     * 判断签到次数
     *
     * @param userId
     * @param name
     * @return
     */
    private Integer qiandao(String userId, String name) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(new Date().getTime());
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        Integer integral = integralRecordRepository.countByOperatorIdAndFunctionAndCreateTimeGreaterThanAndCreateTimeLessThan(userId, name, calendar.getTimeInMillis(), calendar1.getTimeInMillis());
        return integral;
    }

    /**
     * 获取老师信息
     *
     * @param userInfo
     * @return
     */
    private TeacherModel getTeacherDetail(TeacherInfo userInfo) {
        TeacherModel model1 = null;
        List<CourseUserRelation> tcr = courseUserRelationRepository.findAllByUid(userInfo.getId());//老师课堂
        //List<ResourceTeacherInfoRelation> trr = resourceTeacherInfoRelationRepository.findByTeacherId(userInfo.getId());//老师资源
        //List<CourseWare> trr=
        model1 = MappingEntity3ModelCoverter.CONVERTERFROMBACKTEACHERINFO(userInfo);
        model1.setCountCourse(tcr.size());
        // model1.setCountResource(trr.size());
        return model1;
    }

    /**
     * 获取互动消息
     *
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
   /* @Override
    public Page<Message> getTeacherInteractionMessage(UserInfoForToken userInfo, int pageNum, int pageSize) {
        pageNum = pageNum - 1;
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<Message> records = messageRepository.findAll((Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("recipientId"), userInfo.getUserId()));
            predicateList.add(criteriaBuilder.equal(root.get("send"),"Y"));//已发送
            predicateList.add(criteriaBuilder.notEqual(root.get("senderid"),null));//互动
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return records;
    }*/

    /**
     * 获取互动消息(根据用户，不分发送接收者)
     *
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Message> getTeacherInteractionMessage(UserInfoForToken userInfo, int pageNum, int pageSize) {
        List<Message> messages = classInfoMapper.getMessageByUser(userInfo.getUserId(), (pageNum - 1) * pageSize, pageSize);
        return messages;
    }

    /**
     * 发送互动信息
     *
     * @param userInfo
     * @param
     */
    @Override
    public void addInteractionMessage(UserInfoForToken userInfo, SandMessageModel model) {
        for (IDNameModel i : model.getRecipient()) {
            Message message = MappingEntity3ModelCoverter.CONVERTERFROMBACKMESSAGEMODEL(userInfo, model);
            message.setRecipientId(i.getId());
            message.setRecipientName(i.getName());
            Message message1 = messageRepository.save(message);//保存信息
            MessageUserRelation mur = new MessageUserRelation();//信息id和用户id,一条信息对应多个用户
            mur.setmId(message1.getId());
            mur.setuId(i.getId());//接收者
            messageUserRelationRepository.save(mur);
            MessageUserRelation mur2 = new MessageUserRelation();//信息id和用户id,一条信息对应多个用户
            mur2.setmId(message1.getId());
            mur2.setuId(userInfo.getUserId());//发送者
            messageUserRelationRepository.save(mur2);
        }

    }

    /**
     * 修改积分规则(积分内容，积分，次数)
     *
     * @param userInfo
     * @param integralRule
     */
    @Override
    public void updateintegralRule(UserInfoForToken userInfo, IntegralRule integralRule) {
        IntegralRule integralRule1 = integralRuleRepository.findFirstById(integralRule.getId());
        if (integralRule1 != null) {
            integralRule1.setContent(integralRule.getContent());//类容
            integralRule1.setCount(integralRule.getCount());//次数
            integralRule1.setIntegral(integralRule.getIntegral());//积分
            integralRuleRepository.save(integralRule1);
        }

    }

    /**
     * 获取除了自己以外的所有老师
     *
     * @param userInfo
     * @return
     */
    @Override
    public List<TeacherModel> getAllTeacher(UserInfoForToken userInfo) {
        List<TeacherModel> modelList = new ArrayList<TeacherModel>();
        List<TeacherInfo> teacherInfo = teacherInfoRepository.findByIdNot(userInfo.getUserId());//除了自己
        for (TeacherInfo t : teacherInfo) {
            TeacherModel teacherModel = MappingEntity3ModelCoverter.CONVERTERFROMBACKTEACHERINFO(t);
            modelList.add(teacherModel);
        }
        return modelList;
    }

    @Override
    public void addMessage(UserInfoForToken userInfo, MessageModel message) {
        Message message1=MappingEntity3ModelCoverter.CONVERTERFROMBACKMESSAGE(userInfo,message);
        messageRepository.save(message1);
    }

    /***
     * 积分
     * @param userInfo
     * @param integralRecord
     */
    @Override
    public void addintegral(UserInfoForToken userInfo, IntegralRecord integralRecord) {
        integralRecord.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        integralRecordRepository.save(integralRecord);
    }
}

