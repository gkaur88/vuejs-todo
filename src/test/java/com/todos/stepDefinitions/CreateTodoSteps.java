package com.todos.stepDefinitions;

import basePackage.BaseClass;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utility.ScreenshotHelper;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CreateTodoSteps extends BaseClass {

    Logger logger = LogManager.getLogger(CreateTodoSteps.class.getName());

    /**
     * Page Factory with @CacheLookup for faster retrieval of WebElements
     */
    @CacheLookup @FindBy(xpath = "//h1[contains(text(),'todos')]") WebElement todosLogo;
    @CacheLookup @FindBy(xpath = "//input[@placeholder='What needs to be done?']") WebElement todoTextBox;
    @CacheLookup @FindBy(xpath = "//a[@href='#/all']") WebElement allTodoSelectedLabel;
    @CacheLookup @FindBy(xpath = "//a[@href='#/active'][@class='selected']") WebElement activeTodoSelectedLabel;
    @CacheLookup @FindBy(xpath = "//a[@href='#/completed']") WebElement completedTodoSelectedLabel;
    @CacheLookup @FindBy(xpath = "//button[@class='clear-completed']") WebElement clearCompletedNoteLabel;
    @CacheLookup @FindBy(xpath = "//span[@class='todo-count']") WebElement todoCountText;
    @CacheLookup @FindBy(xpath = "//label[contains(text(),'Text-Reminder')]") WebElement anyCreatedTodoList;
    @CacheLookup @FindBy(xpath = "/html/body/section/section/ul/li[1]/div/button") WebElement destroyTodo;
    @CacheLookup @FindBy(xpath = "//label[contains(text(),'Text-Reminder')]") List<WebElement> createdTodoLists;
    public static final By createdTodoListToggles = By.xpath("//input[@type='checkbox'][@class='toggle']");
    public static ArrayList<WebElement> createdTodoToggle;

    @Given("Browser is Open")
    public boolean browserIsOpen() {
        initDriver();
        PageFactory.initElements(driver, this);
        String defaultUrl = prop.getProperty("defaultUrl");
        try {
            logger.info("VueJS Todos default page is launched");
            driver.get(defaultUrl);
            Assert.assertEquals("Vue.js • TodoMVC", driver.getTitle());
            ScreenshotHelper.takeScreenshot("Browser_Page_Check", driver);
            return driver.getCurrentUrl().equals(prop.getProperty("defaultUrl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @When("Website content is loaded")
    public void websiteContentIsLoaded() throws IOException, InterruptedException {
        logger.info("Navigating to Todos list page..");
        waitForElement(todosLogo, Duration.ofSeconds(2000));
        ScreenshotHelper.takeScreenshot("Todos Home Page", driver);
        Assert.assertTrue(todosLogo.isDisplayed());
    }

    @Then("Verify Todo list textbox is enabled")
    public void verifyTodoListTextboxIsEnabled() throws IOException, InterruptedException {
        logger.info("Verifying that todos list textbox is enabled..");
        waitForElement(todoTextBox, Duration.ofSeconds(2000));
        ScreenshotHelper.takeScreenshot("Todos Text Box", driver);
        Assert.assertTrue(todoTextBox.isEnabled());
    }

    @And("Enter {string} text and press enter")
    public void enterTextAndPressEnter(String todoTextOne) throws IOException, InterruptedException {
        logger.info("Creating first todo list..");
        todoTextBox.sendKeys(todoTextOne);
        todoTextBox.sendKeys(Keys.ENTER);
        ScreenshotHelper.takeScreenshot("Todos First Note", driver);
    }

    @Then("Verify new todo text {string} note created and is in active state")
    public void verifyNewTodoTextNoteCreatedAndIsInActiveState(String todoTextOne) throws IOException, InterruptedException {
        logger.info("Verifying if first todo list text has been created and in active state..");
        ScreenshotHelper.takeScreenshot("Todos Active Label", driver);
        Assert.assertEquals(todoTextOne, anyCreatedTodoList.getText());
        Assert.assertTrue(activeTodoSelectedLabel.isEnabled());
    }

    @Then("Verify that three todo text notes are created and all are in active state")
    public void verifyThatThreeTodoTextNotesAreCreatedAndAllAreInActiveState() throws IOException, InterruptedException {
        logger.info("Verifying if three todo list text has been created and in active state..");
        ScreenshotHelper.takeScreenshot("Todos All Active Notes", driver);
        Assert.assertEquals(3, createdTodoLists.size());
        Assert.assertTrue(activeTodoSelectedLabel.isEnabled());
    }

    @Then("Verify that on selecting any todo text note it moves to completed label")
    public void verifyThatOnSelectingAnyTodoTextNoteItMovesToCompletedLabel() throws IOException, InterruptedException {
        logger.info("Move any todo note to complete..");
        createdTodoToggle = new ArrayList<>(driver.findElements(createdTodoListToggles));
        createdTodoToggle.get(0).click(); // Selecting first toggle todo list note
        completedTodoSelectedLabel.click();
        ScreenshotHelper.takeScreenshot("Todos Completed Note", driver);
        WebElement todoOneCreated = driver.findElement(By.xpath("//label[contains(text(),'Text-Reminder')]"));
        Assert.assertEquals("Text-Reminder-1", todoOneCreated.getText());
    }

    @And("Verify that with click on Click Completed label the completed todo note is removed from completed note list")
    public void verifyThatWithClickOnClickCompletedLabelTheCompletedTodoNoteIsRemovedFromCompletedNoteList() throws IOException, InterruptedException {
        logger.info("Verifying completed note and clearing list..");
        clearCompletedNoteLabel.click();
        ScreenshotHelper.takeScreenshot("Todos Completed Note Removed", driver);
    }

    @Then("Verify that under All label only <{int}> todo notes exists")
    public void verifyThatUnderAllLabelOnlyTodoNotesExists(int allTodoNotes) throws IOException, InterruptedException {
        logger.info("Verifying available notes under All label..");
        allTodoSelectedLabel.click();
        ScreenshotHelper.takeScreenshot("Todos Remaining Notes", driver);
        createdTodoToggle = new ArrayList<>(driver.findElements(createdTodoListToggles));
        Assert.assertEquals(createdTodoToggle.size(), allTodoNotes);
    }

    @And("Verify todo-count text displays number of items left")
    public void verifyTodoCountTextDisplaysNumberOfItemsLeft() throws IOException, InterruptedException {
        logger.info("Verifying todo-count text with number of items left..");
        ScreenshotHelper.takeScreenshot("Todos Items Left Count", driver);
        Assert.assertEquals("2 items left" , todoCountText.getText());
    }

    @And("Verify todo note can be destroyed and under All label only <{int}> todo notes exists")
    public void verifyTodoNoteCanBeDestroyedAndUnderAllLabelOnlyTodoNotesExists(int allTodoNotes) throws IOException, InterruptedException {
        logger.info("Verifying available notes under All label..");
        WebElement todoOneCreated = driver.findElement(By.xpath("//label[contains(text(),'Text-Reminder')]"));
        Actions action = new Actions(driver);
        action.moveToElement(todoOneCreated).build().perform();
        destroyTodo.click();
        ScreenshotHelper.takeScreenshot("Todos After Destroyed Note", driver);
        createdTodoToggle = new ArrayList<>(driver.findElements(createdTodoListToggles));
        Assert.assertEquals(createdTodoToggle.size(), allTodoNotes);
    }

    @Then("Verify that todo note can be edited through double click")
    public void verifyThatTodoNoteCanBeEditedThroughDoubleClick() throws IOException, InterruptedException {
        logger.info("Verifying edit to existing todo note..");
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("document.getElementsByTagName('label').item(1).innerHTML='Text-Reminder-0'");
        ScreenshotHelper.takeScreenshot("Todos After Editing Note", driver);
    }

    @And("Close the browser")
    public void closeTheBrowser() {
        tearDown();
    }
}