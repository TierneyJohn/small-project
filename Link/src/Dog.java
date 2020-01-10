public class Dog implements Comparable<Dog> {
    private String name;
    private int age;
    private People people;

    public Dog(String name, int age, People people) {
        this.name = name;
        this.age = age;
        this.people = people;
    }

    @Override
    public int compareTo(Dog dog) {
        return this.age - dog.age;
    }

    @Override
    public boolean equals(Object obj) {
       if (this == obj) {
           return true;
       }
       if (!(obj instanceof Dog)) {
           return false;
       }
       return this.age == ((Dog) obj).age &&
               this.name.equals(((Dog) obj).name) &&
               this.people.equals(((Dog) obj).people);
    }

    @Override
    public String toString() {
        return "【狗信息】 名字： " + this.name +
                " 年龄： " + this.age +
                " 主人： " + this.people.getName();
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.name + this.age + this.people + super.hashCode());
    }
}
