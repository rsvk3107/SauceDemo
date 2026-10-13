
package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageTitle =
            By.className("title");
    private final By productItems =
            By.className("inventory_item");
    private final By cartBadge =
            By.className("shopping_cart_badge");
    private final By cartLink =
            By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver, Duration.ofSeconds(10));
    }

    public boolean isInventoryDisplayed() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        pageTitle)).getText().equals("Products");
    }

    public int getProductCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                productItems));
        return driver.findElements(productItems).size();
    }


    public void addProduct(String productName) {
        By productNameLocator = By.xpath(
                "//*[contains(@class,'inventory_item_name') " +
                        "and normalize-space(text())='" + productName + "']"
        );

        wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .visibilityOfElementLocated(productNameLocator)
        );

        By addButton = By.xpath(
                "//*[contains(@class,'inventory_item_name') " +
                        "and normalize-space(text())='" + productName + "']" +
                        "/ancestor::div[contains(@class,'inventory_item')]" +
                        "//button[contains(@class,'btn_inventory')]"
        );

        wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .elementToBeClickable(addButton)
        ).click();
    }


    public String getCartCount() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        cartBadge)).getText();
    }

    public void openCart() {
        driver.findElement(cartLink).click();
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
