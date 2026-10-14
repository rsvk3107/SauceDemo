
package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver, Duration.ofSeconds(10));
    }

    public boolean isCartDisplayed() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        cartTitle)).getText().equals("Your Cart");
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public boolean containsProduct(String productName) {
        By product = By.xpath(
                "//div[@class='cart_item']" +
                        "//div[@class='inventory_item_name' and normalize-space()="
                        + xpathLiteral(productName) + "]"
        );

        return !driver.findElements(product).isEmpty();
    }

    public void removeProduct(String productName) {
        By removeButton = By.xpath(
                "//div[@class='cart_item']" +
                        "[.//div[@class='inventory_item_name' and normalize-space()="
                        + xpathLiteral(productName) + "]]" +
                        "//button[contains(@class,'cart_button')]"
        );

        wait.until(ExpectedConditions.elementToBeClickable(
                removeButton)).click();
    }


    public void clickCheckout() {
        wait.until(
                ExpectedConditions.elementToBeClickable(checkoutButton)
        ).click();

        wait.until(
                ExpectedConditions.urlContains("checkout-step-one.html")
        );
    }


    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }

        String[] parts = value.split("'", -1);
        StringBuilder result = new StringBuilder("concat(");

        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                result.append(", \"'\", ");
            }
            result.append("'").append(parts[i]).append("'");
        }

        return result.append(")").toString();
    }
}
