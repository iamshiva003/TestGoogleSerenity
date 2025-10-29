package starter.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import starter.check.Navigate;

public class CheckStepDefinitions {
    
    Navigate navigate = new Navigate();

    @Given("User sets up the driver")
    public void setupDriver() {
        navigate.setupDriver();
    }


    @When("search for an item")
    public void searchForAnItem() {
        navigate.searchItem();
    }
}
