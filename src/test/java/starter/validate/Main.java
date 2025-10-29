package starter.validate;

public class Main {
    public static void main(String[] args) throws Exception {
        RemoteInvoker.invoke("starter.check.Navigate", "setupDriver");
        RemoteInvoker.invoke("starter.check.Navigate", "searchItem");
    }
}
