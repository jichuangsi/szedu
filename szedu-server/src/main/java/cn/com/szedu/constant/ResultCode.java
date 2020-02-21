package cn.com.szedu.constant;

public class ResultCode {
	public static final String SUCESS = "0010";//成功
	public static final String PARAM_MISS = "0020";//缺少参数
	public static final String PARAM_ERR = "0021";//参数不正确
	public static final String TOKEN_MISS = "0030";//缺少token
	public static final String TOKEN_CHECK_ERR = "0031";//token校验异常
	public static final String SYS_ERROR = "0050";//系统内部异常
	public static final String SYS_BUSY = "0051";//熔断
	
	public static final String SUCESS_MSG = "成功";
	public static final String PARAM_MISS_MSG = "缺少参数";
	public static final String PARAM_ERR_MSG = "参数不正确";
	public static final String TOKEN_MISS_MSG = "缺少token";
	public static final String TOKEN_CHECK_ERR_MSG = "token校验异常";
	public static final String SYS_ERROR_MSG = "系统繁忙";
	public static final String SYS_BUSY_MSG = "系统繁忙";

	public static final String SELECT_NULL_MSG = "查无此信息";
	public final static String ACCOUNT_ISEXIST_MSG = "账号已存在";
	public final static String ACCOUNT_NOTEXIST_MSG = "账号不存在，或者账户密码错误";
	public static final String FILE_ISNOT_EXIST = "文件不存在";
	public static final String USER_IS_EXIST = "账号已存在";
	public static final String EXCEL_IMPORT_MSG = "excel导入失败";
	public static final String PWD_DIFFERENT_MSG="密码不一致";
	public static final String CATEGORY_ISEXIT_MSG="该分类下仍有文章";
	public static final String EXAMTESTPAPER_ISEXIT_MSG="考试下仍有该试卷";
	public static final String RESOURCE_ISNOT_EXIST = "该资源不存在";

	public static final String STUDENTID_IS_EXIST="学号已存在";
	public static final String SIGNIN_IS_EXIST="已签到";
	public static final String COURSE_IS_INVALID="已过上课时间";
	public static final String TEST_IS_EXIST="不能重复提交试卷";
	public static final String USERLIKE_ISNOT_EXIST="操作失败";
}
