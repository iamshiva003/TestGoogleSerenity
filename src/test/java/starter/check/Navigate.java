package starter.check;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import starter.validate.RemoteInvoker;

import static net.serenitybdd.core.Serenity.getDriver;

public class Navigate extends NavigatPageObjects{

    public static String URL = "https://www.google.com/";

    /**
     * This method initializes the webdriver and sets implicit wait
     */
    public void setupDriver() {
        getDriver().get(URL);
        getDriver().manage().window().maximize();
    }

    public void searchItem() {
        getSearchbar().sendKeys("Pen");
        Actions actions = new Actions(getDriver());
        actions.sendKeys(Keys.ENTER).build().perform();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        RemoteInvoker.invoke("starter.check.Navigate", "setupDriver");
        RemoteInvoker.invoke("starter.check.Navigate", "searchItem");
    }
}
