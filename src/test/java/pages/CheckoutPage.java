
package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By orderComplete =
            By.className("complete-header");
    private final By summaryItems =
            By.className("cart_item");
    private final By errorMessage =
            By.cssSelector("[data-test='error']");
    private final By summaryTotal =
            By.className("summary_total_label");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver, Duration.ofSeconds(10));
    }


    public void enterCustomerDetails(
            String first, String last, String zip) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName))
                .clear();
        driver.findElement(firstName).sendKeys(first);

        driver.findElement(lastName).clear();
        driver.findElement(lastName).sendKeys(last);

        driver.findElement(postalCode).clear();
        driver.findElement(postalCode).sendKeys(zip);
    }



    public void continueCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton))
                .click();
    }


    public int getSummaryItemCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                summaryItems));
        return driver.findElements(summaryItems).size();
    }

    public String getOrderTotal() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        summaryTotal)).getText();
    }

    public void finishOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(
                finishButton)).click();
    }

    public String getConfirmationMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        orderComplete)).getText();
    }

    public String getErrorMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        errorMessage)).getText();
    }
}
