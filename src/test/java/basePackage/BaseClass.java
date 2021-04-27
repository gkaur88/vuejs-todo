package basePackage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseClass {
	Logger logger = LogManager.getLogger(BaseClass.class.getName());
	public static WebDriver driver;
	public static Properties prop;

	public BaseClass() {
		prop = new Properties();
		try {
			FileInputStream conf_file = new FileInputStream("src/test/config/config.properties");
			prop.load(conf_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static WebDriver initDriver() {
		String Log4jConfigPath="log4j.properties";
		PropertyConfigurator.configure(Log4jConfigPath);
		String browserName = prop.getProperty("browser");

		if(browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		else if(browserName.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		else {
			System.out.println("Please pass valid browser name :"+browserName);
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Util.IMPLICIT_WAIT,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(Util.PAGE_LOAD_TIMEOUT,TimeUnit.SECONDS);
		return driver;
	}

	public void tearDown(){
        driver.close();
        driver.quit();
    }

	/**
    * wait until expected element is visible
    * @param   element     element to be expected
    * @param   timeout     Maximum timeout time in milliseconds. WebDriverWait LONG is deprecated. Using Duration.
    */
    public static void waitForElement(WebElement element, Duration timeout) {
        try {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.elementToBeClickable(element));

        } catch (Exception e) {
        e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
