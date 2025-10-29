package starter.check;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

public class NavigatPageObjects extends PageObject {
    @FindBy(xpath = "//textarea[@title='Search']")
    public WebElementFacade searchbar;


}
