import Util.SeleniumDriverSetUp;
import Util.SeleniumUtils;
import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.Iterator;
import java.util.List;


public abstract class AnswerBot {

    public void navigateScanAndSubmitForm() {
        String name;
        String newURL;

        Faker faker = new Faker();

        List<WebElement> buttons = SeleniumDriverSetUp.driver.findElements(By.tagName("button"));
        List<WebElement> inputFields = SeleniumDriverSetUp.driver.findElements(By.tagName("input"));

        Iterator<WebElement> elementIterator;
        if (buttons.size() != 0) {
            elementIterator = buttons.iterator();
        } else {
            elementIterator = inputFields.iterator();
        }

        while (elementIterator.hasNext()) {
            try {
                name = elementIterator.next().getText();
            } catch (StaleElementReferenceException e) {
                List<WebElement> button = SeleniumDriverSetUp.driver.findElements(By.tagName("button"));
                Iterator<WebElement> elementIterator1 = button.iterator();
                name = elementIterator1.next().getText();
            }

            //need to add more button names
            if (name.equals("Start now") || name.equals("Next") || name.equals("Continue") || name.equals("Accept and send")
                    || name.contains("Apply")) {

                SeleniumUtils.clickUsingJavaScript(name);
                // wait for page to load
                SeleniumUtils
                        .waitForLoad(SeleniumDriverSetUp.driver);

                buttons = SeleniumDriverSetUp.driver.findElements(By.tagName("button"));
                elementIterator = buttons.iterator();

                if (SeleniumDriverSetUp.driver.findElements(By.tagName("input")).size() != 0) {
                    SeleniumUtils
                            .waitForLoad(SeleniumDriverSetUp.driver);
                    List<WebElement> inputTag = SeleniumDriverSetUp.driver.findElements(By.tagName("input"));
                    Iterator<WebElement> elementIterator1 = inputTag.iterator();
                    String type = elementIterator1.next().getAttribute("type");

                    if (type.equals("radio") && (!inputTag.stream().findAny().get().isSelected())) {
                        SeleniumUtils
                                .waitForLoad(SeleniumDriverSetUp.driver);
                        inputTag.stream().findAny().get().click();
                    }
                    if (type.equals("text")) {
                        SeleniumUtils
                                .waitForLoad(SeleniumDriverSetUp.driver);
                        inputTag.forEach(x -> x.sendKeys(faker.color().name()));
                    }
                    if (type.equals("checkbox")) {
                        SeleniumUtils
                                .waitForLoad(SeleniumDriverSetUp.driver);
                        inputTag.forEach(WebElement::click);
                    }
                }
            }
        }
    }
}