package cn.com.szedu.constant;

public enum  CourseStatus {
    UNPUBLISH("N", 0), PUBLISH("P", 1),HAVING("H",2), FINISH("F", 3);//未发布，已发布，上课中，结束
    private String name;
    private int index;

    // 构造方法
    private CourseStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (CourseStatus c : CourseStatus.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static CourseStatus getCourseStatus(String name) {
        for (CourseStatus c : CourseStatus.values()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
