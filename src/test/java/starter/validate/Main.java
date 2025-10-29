package starter.validate;

public class Main {
    public static void main(String[] args) throws Exception {
        RemoteInvokerAdvanced.invoke("starter.check.Navigate", "setupDriver");
        RemoteInvokerAdvanced.invoke("starter.check.Navigate", "searchItem");

        String message = RemoteInvokerAdvanced.invoke("starter.check.ClassReturns", "sayHello").toString();
        int messageNum = Integer.parseInt(RemoteInvokerAdvanced.invoke("starter.check.ClassReturns", "sayNumber").toString());
        System.out.println("Message: " + message);
        System.out.println("MessageNum: " + messageNum);

        String name = RemoteFieldAccessor.getFieldValue("starter.check.Navigate", "name", null).toString();
        System.out.println("Name: " + name);


    }
}
