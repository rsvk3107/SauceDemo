
package base;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.chrome.ChromeOptions;


public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Properties properties;

    @BeforeMethod
    public void setUp() throws Exception {
        properties = new Properties();

        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "config.properties not found");
            }

            properties.load(input);
        }

        String browser = System.getProperty(
                "browser",
                properties.getProperty("browser", "chrome")
        );

        switch (browser.toLowerCase()) {
            case "firefox" -> driver = new FirefoxDriver();
            case "edge" -> driver = new EdgeDriver();
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();

                options.addArguments("--start-maximized");
                options.addArguments("--disable-notifications");
                options.addArguments("--disable-save-password-bubble");

                // Disable password leak detection via command line
                options.addArguments("--disable-features=PasswordLeakDetection");

                // Disable password manager & breach check preferences
                java.util.Map<String, Object> prefs = new java.util.HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);

                options.setExperimentalOption("prefs", prefs);

                driver = new ChromeDriver(options);

            }
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts()
                .implicitlyWait(Duration.ZERO);

        wait = new WebDriverWait(
                driver, Duration.ofSeconds(10));

        driver.get(properties.getProperty("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
