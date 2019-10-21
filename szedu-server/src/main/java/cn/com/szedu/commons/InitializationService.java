package cn.com.szedu.commons;

import cn.com.szedu.service.BackUserService;
import cn.com.szedu.service.UserInfoService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class InitializationService {
    /*@Resource
    private BackUserService backUserService;*/
    @Resource
    private UserInfoService userInfoService;

    @PostConstruct
    public void insertSuperMan(){
        try {
            userInfoService.insertSuperMan();
            /*backUserService.insertSuperMan();*/
        } catch (Exception e) {
        }
    }
}
