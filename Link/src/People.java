/**
 * 用于验证Link类的可行性
 */
public class People implements Comparable<People> {

    private String name;
    private int age;
    private Sex sex;

    @Override
    public int compareTo(People people) {
        return this.age - people.age;
    }

    /**
     * 覆写equals方法
     *
     * @param object 要比较的数据
     * @return 相同返回true，不同返回false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof People)) {
            return false;
        }
        return this.age == ((People) object).age &&
                this.name.equals(((People) object).name) &&
                this.sex.equals(((People) object).sex);
    }

    public People(String name, int age,Sex sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "【角色信息】 姓名： " + this.name + " 年龄： " + this.age + " 性别： " + this.sex.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}

enum Sex {
    MALE("男"), FEMALE("女");

    private String title;

    Sex(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        switch (title) {
            case "MALE" : return "男";
            case "FEMALE" : return "女";
            default: return title;
        }
    }

}