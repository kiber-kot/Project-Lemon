package util;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import util.network.NetworkHelper;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import static constant.AllConstant.CONSOLE_ERRORS_500;
import static constant.AllConstant.CONSOLE_ERRORS_ALL;
import static util.WebConfig.BASE_CONFIG;

public class MainUtil {

    private static WebDriver driver;

    private static util.Logger logger;

    public MainUtil() {
        starter();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(ChromeDriver driver) {
        MainUtil.driver = driver;
    }

    @BeforeEach
    void init() {
        starter();
    }


    @AfterEach
    void loggerAfter(){
        if(BASE_CONFIG.logNetwork()){
            logger.getNetworkHelper().checkStatusCodeInNetworkConsole(logger.getNetworkEntity() , CONSOLE_ERRORS_ALL);
        }
    }

    @AfterEach
    void after() {
        if (driver != null) {
            if(driver.getWindowHandles().size() > 1){
                getDriver().quit();
                setDriver(null);
            }else {
                getDriver().close();
                setDriver(null);
            }
        }
        ElementUtil.generateAllureEnvironment();
    }

    private void starter() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            var os = System.getProperty("os.name").toLowerCase();
            if (os.contains("mac os") || os.contains("windows")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
            } else {
                options.addArguments("--headless");
                options.addArguments("--window-size=2440, 1900");
                options.addArguments("--session-override true");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("user-agent=\"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)\"");
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(options);
                Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
            }
            if(BASE_CONFIG.logNetwork()){
                logger = starterLog();
            }
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        }
    }

    private static util.Logger starterLog(){
        var network = new NetworkHelper();
        var entity = network.starter();
        return new util.Logger(network, entity);
    }

}
