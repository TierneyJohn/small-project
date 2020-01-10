import java.util.Arrays;

public class ILinkTest {
    public static void main(String[] args) {
        ILink<People> peopleLink = new LinkImpl<>();
        peopleLink.add(new People("张三", 20, Sex.MALE));
        peopleLink.add(new People("李四", 30, Sex.MALE));
        peopleLink.add(new People("王五", 40, Sex.MALE));
        peopleLink.add(new People("张而", 10, Sex.FEMALE));
        System.out.println(peopleLink.query(new People("李四", 30, Sex.MALE)));
        System.out.println(peopleLink.query(new People("王五", 40, Sex.FEMALE)));
        peopleLink.remove(new People("王五", 40, Sex.MALE));
        peopleLink.update(new People("张三", 20, Sex.MALE), new People("张三", 20, Sex.FEMALE));
        peopleLink.add(new People("王五", 40, Sex.MALE));
        peopleLink.add(new People("张三", 20, Sex.MALE));
        System.out.println(Arrays.toString(peopleLink.toArray()));
    }
}
