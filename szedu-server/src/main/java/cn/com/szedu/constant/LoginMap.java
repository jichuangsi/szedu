package cn.com.szedu.constant;

import cn.com.szedu.service.impl.TeacherInfoServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class LoginMap {

    private static Map<String,Long> map=new HashMap<String,Long>();

    public static Map<String, Long> getMap() {
        return map;
    }

    public static void setMap(Map<String, Long> map) {
        LoginMap.map = map;
    }
}
