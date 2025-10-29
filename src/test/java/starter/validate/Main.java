package starter.validate;

public class Main {
    public static void main(String[] args) throws Exception {
        RemoteInvokerAdvanced.invoke("starter.check.Navigate", "setupDriver");
        RemoteInvokerAdvanced.invoke("starter.check.Navigate", "searchItem");
    }
}
