package io.cucumber.pro;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

public class Stepdefs {

    private int bellyCount;

    @Given("^I have already eaten (\\d+) cucumbers$")
    public void i_have_already_eaten_cucumbers(int bellyCount) {
        this.bellyCount = bellyCount;
    }

    @When("^I eat (\\d+) cucumbers$")
    public void i_eat_cucumbers(int eaten) {
        bellyCount -= eaten;
    }

    @Then("^I should have (\\d+) cucumbers in my belly$")
    public void i_should_have_cucumbers_in_my_belly(int expected) {
        assertEquals(expected, bellyCount);
    }
}
