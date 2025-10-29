package starter.check;

public class ClassReturns {
    public String sayHello() {
        return "Hello from ClassReturns!";
    }

    public int sayNumber() {
        ClassReturns1 classReturns1 = new ClassReturns1();

        int num = classReturns1.returnNumber();
        System.out.println("Number: " + num);

        return num;
    }
}
