
package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;
import pages.CheckoutPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SauceDemoE2ETest extends BaseTest {

    private void loginAsStandardUser() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                properties.getProperty("username"),
                properties.getProperty("password")
        );
    }
// the below code contains the validations of the sause demo page
    @Test
    public void completeEndToEndPurchase() {
        loginAsStandardUser();

        InventoryPage inventory = new InventoryPage(driver);

        Assert.assertTrue(inventory.isInventoryDisplayed(),
                "Products page should be displayed");

        Assert.assertEquals(inventory.getProductCount(), 6,
                "Expected six demo products");

        inventory.addProduct("Sauce Labs Backpack");
        inventory.addProduct("Sauce Labs Bike Light");

        Assert.assertEquals(inventory.getCartCount(), "2",
                "Cart badge should show two items");

        inventory.openCart();

        CartPage cart = new CartPage(driver);

        Assert.assertTrue(cart.isCartDisplayed());
        Assert.assertEquals(cart.getCartItemCount(), 2);
        Assert.assertTrue(
                cart.containsProduct("Sauce Labs Backpack"));
        Assert.assertTrue(
                cart.containsProduct("Sauce Labs Bike Light"));

        cart.clickCheckout();

        CheckoutPage checkout = new CheckoutPage(driver);

        checkout.enterCustomerDetails(
                "Test", "User", "500001");

        checkout.continueCheckout();

        Assert.assertEquals(checkout.getSummaryItemCount(), 2,
                "Order summary should contain two items");

        String total = checkout.getOrderTotal();
        Assert.assertTrue(total.startsWith("Total: $"),
                "Order total should be displayed");

        checkout.finishOrder();

        Assert.assertEquals(
                checkout.getConfirmationMessage(),
                "Thank you for your order!",
                "Order confirmation should be displayed");
    }

    @Test
    public void invalidLoginShowsError() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("invalid_user", "wrong_password");

        Assert.assertTrue(
                loginPage.getErrorMessage().contains(
                        "Username and password do not match"),
                "An invalid login error should be displayed");
    }

    @Test
    public void checkoutRequiresFirstName() {
        loginAsStandardUser();

        InventoryPage inventory = new InventoryPage(driver);
        inventory.addProduct("Sauce Labs Backpack");
        inventory.openCart();

        CartPage cart = new CartPage(driver);
        cart.clickCheckout();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.enterCustomerDetails("", "User", "500001");
        checkout.continueCheckout();

        Assert.assertTrue(
                checkout.getErrorMessage().contains(
                        "First Name is required"),
                "Checkout should require a first name");
    }
}
