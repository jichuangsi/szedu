package cn.com.szedu.service.impl;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.IntegralRecord;
import cn.com.szedu.entity.IntegralRule;
import cn.com.szedu.entity.Message;
import cn.com.szedu.entity.StudentInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.Student.StudentInfoModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IIntegralRuleRepository;
import cn.com.szedu.repository.IStudentInfoRespository;
import cn.com.szedu.repository.IntegralRecordRepository;
import cn.com.szedu.repository.MessageRepository;
import cn.com.szedu.service.BackTokenService;
import cn.com.szedu.service.StudentInfoService;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
@Service
public class StudentInfoServiceImpl implements StudentInfoService {

    @Resource
    private BackTokenService backTokenService;
    @Resource
    private IStudentInfoRespository studentInfoRespository;
    @Resource
    private IntegralRecordRepository integralRecordRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private IIntegralRuleRepository integralRuleRepository;

    /**
     * 学生登录(根据账号和手机号)
     * @param model
     * @return
     * @throws UserServiceException
     */
    @Override
    public StudentInfoModel loginStudent(StudentInfoModel model) throws UserServiceException {

        StudentInfoModel model1=null;
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPhone())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        StudentInfo userInfo=studentInfoRespository.findByAccountAndPhone(model.getAccount(),model.getPhone());
        if (userInfo==null){
            throw new UserServiceException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }

        String user=JSONObject.toJSONString(MappingEntity3ModelCoverter.CONVERTERFROMBACKSTUDENTINFO(userInfo));
        try {
            String acc=backTokenService.createdToken(user);
            model1=MappingEntity3ModelCoverter.CONVERTERFROMBACKSTUDENTMODEL(userInfo);
            model1.setAccessToken(acc);
            Integer integral = qiandao(userInfo.getId(), "签到");
            if (integral > 0) {
                model1.setSignin(false);//不能签
            } else {
                model1.setSignin(true);//能签
            }
            return model1;
        }catch (UnsupportedEncodingException e){
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
        //修改学生积分
        StudentInfo studentInfo = studentInfoRespository.findById(userInfo.getUserId());
        IntegralRule integralRule=integralRuleRepository.findByRoleAndType("学生","签到");
        if (studentInfo.getIntegral() == null) {
            studentInfo.setIntegral(0);
        }
        Integer integral = qiandao(userInfo.getUserId(), "签到");
        if (integral <integralRule.getCount()) {
            studentInfo.setIntegral(studentInfo.getIntegral() +integralRule.getIntegral());//学生签到积分
            studentInfoRespository.save(studentInfo);
            //积分记录
            IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                    "签到", "学生登录签到", userInfo.getUserId(), userInfo.getUserName(),
                    integralRule.getIntegral(), System.currentTimeMillis());
            integralRecordRepository.save(integralRecord);
            //系统信息
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String s = df.format(new Date());// new Date()为获取当前系统时间

            String messages = "您今日已成功签到获得"+integralRule.getIntegral()+"积分。" + s;
            Message message = new Message(studentInfo.getId(), studentInfo.getName(), messages, "N");
            messageRepository.save(message);
        }
        StudentInfo teacherInfo1 = studentInfoRespository.findById(userInfo.getUserId());
        return teacherInfo1.getIntegral();
    }


    /**
     * 判断签到次数
     *
     * @param userId
     * @param name
     * @return
     */
    private Integer qiandao(String userId, String name){
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
}
