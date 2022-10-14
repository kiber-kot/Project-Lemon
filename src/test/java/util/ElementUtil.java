package util;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.commons.io.FileUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;


import java.io.*;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.fail;
import static util.WebConfig.BASE_CONFIG;

public class ElementUtil extends MainUtil {

    private static final Object clock = new Object();

    private static final String httpConnectionTimeout =  BASE_CONFIG.httpConnectionTimeout();


    @Step("Получить текст по xpath {0}")
    public static String getText(String xpath){
        try{
            return getDriver().findElement(By.xpath(xpath)).getText();
        } catch (NoSuchElementException e) {
            saveScreenshot();
            fail("Ошибка, элемент xpath = " + xpath + " не найден на странице");
            return "";
        } catch (ElementNotInteractableException e) {
            saveScreenshot();
            fail("Ошибка, элемент присутствует в HTML DOM, он не находится в состоянии, с которым можно взаимодействовать: элемент = " + xpath);
            return "";
        }
    }

    @Step("Нажать на элемент {0}")
    public static void click(String xpath) {
        try {
            getDriver().findElement(By.xpath(xpath)).click();
        } catch (NoSuchElementException e) {
            saveScreenshot();
            fail("Ошибка, элемент xpath = " + xpath + " не найден на странице");
        } catch (ElementNotInteractableException e) {
            saveScreenshot();
            fail("Ошибка, элемент присутствует в HTML DOM, он не находится в состоянии, с которым можно взаимодействовать: элемент = " + xpath);
        }
    }
    @Step("Нажать на элемент {0}")
    public static void clickCss(String css) {
        try {
            getDriver().findElement(By.cssSelector(css)).click();
        } catch (NoSuchElementException e) {
            saveScreenshot();
            fail("Ошибка, элемент xpath = " + css + " не найден на странице");
        } catch (ElementNotInteractableException e) {
            saveScreenshot();
            fail("Ошибка, элемент присутствует в HTML DOM, он не находится в состоянии, с которым можно взаимодействовать: элемент = " + css);
        }
    }

    @Step("Ввести текст по xpath {0} со значением {1}")
    public static void sendKeys(String xpath, CharSequence value) {
        try {
            getDriver().findElement(By.xpath(xpath)).sendKeys(value);
        } catch (NoSuchElementException e) {
            saveScreenshot();
            fail("Ошибка, элемент xpath = " + xpath + " не найден на странице со значением " + value);
        } catch (ElementNotInteractableException e) {
            saveScreenshot();
            fail("Ошибка, элемент присутствует в HTML DOM, он не находится в состоянии, с которым можно взаимодействовать: элемент = " + xpath);
        }
    }



    @Step("Загрузка страницы")
    public static boolean getPageLoader(String locator){
        try {
            return getDriver().findElement(By.cssSelector(locator)).isDisplayed();
        } catch (NoSuchElementException e){
            return false;
        }

    }

    @Step("Проверить что по xpath {0}, ожидаемое значение {1}")
    public static void equals(String xpath, String expected) {
        try {
            var value = getDriver().findElement(By.xpath(xpath)).getText();
            if (!expected.equals(value)) {
                saveScreenshot();
                fail("Ошибка, ожидаемый результат = " + expected + " не равен фактическому " + value);
            }
        } catch (NoSuchElementException e) {
            saveScreenshot();
            fail("Ошибка, элемент xpath = " + xpath + " не найден на странице");
        } catch (ElementNotInteractableException e) {
            saveScreenshot();
            fail("Ошибка, элемент присутствует в HTML DOM, он не находится в состоянии, с которым можно взаимодействовать: элемент = " + xpath);
        }
    }
    @Step("Проверить что по css {0}, ожидаемое значение {1}")
    public static void equalsCss(String css, String expected) {
        try {
            var value = getDriver().findElement(By.cssSelector(css)).getText();
            if (!expected.equals(value)) {
                saveScreenshot();
                fail("Ошибка, ожидаемый результат = " + expected + " не равен фактическому " + value);
            }
        } catch (NoSuchElementException e) {
            saveScreenshot();
            fail("Ошибка, элемент xpath = " + css + " не найден на странице");
        } catch (ElementNotInteractableException e) {
            saveScreenshot();
            fail("Ошибка, элемент присутствует в HTML DOM, он не находится в состоянии, с которым можно взаимодействовать: элемент = " + css);
        }
    }

    @Step("Переход по ссылке")
    public static void goToUrl(String url) {
        try {
            var config = RestAssured.config = RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().
                    setParam("http.connection.timeout",Integer.parseInt(httpConnectionTimeout)).
                    setParam("http.socket.timeout",Integer.parseInt(httpConnectionTimeout)));
            given().config(config).get(url);
            getDriver().get(url);
            System.out.println("Стенд = " + getDriver().getCurrentUrl());
        } catch (Exception e){
            if( e instanceof ConnectTimeoutException){
                saveScreenshot();
                fail("Ошибка соединения с сервером по адресу = " + url );
            } else {
                saveScreenshot();
                fail("Ошибка, что-то пошло не так... Результат ошибки = " + e.getMessage());
            }
        }
    }

    @Step("Наведение курсора на элемент по xpath {0}")
    public static void moveToElement(String xpath) {
        try {
            Actions builder = new Actions(getDriver());
            builder.moveToElement(getDriver().findElement(By.xpath(xpath))).perform();
        } catch (NoSuchElementException e) {
            saveScreenshot();
            fail("Ошибка, элемент xpath = " + xpath + " не найден на странице");
        }
    }

    /**
     Номера окон начинаются с 0
     **/
    @Step("Переход в новое окно под номером {0}")
    public static void switchToWindow(int numberWindow) {
        var windows = getDriver().getWindowHandles();
        getDriver().switchTo().window(windows.stream().toList().get(numberWindow));
    }

    /**
     * Анаотацию EnabledIf() использовать строго над классом
     * (над методом использовать нельзя, так как не будет происходить закрытие браузера)
     * Пример:
     * @EnabledIf(value = "util.ElementUtil#idDisabled", disabledReason = "Тест снят с прогона")
     * @return Возвращает false - если класс не надо прогонять, иначе тест будет запущен
     */
    @Step("Игнорирование теста")
    private static boolean idDisabled(){
        return BASE_CONFIG.enableTest();
    }

    @Step ("Скриншот ошибки")
    @Attachment(value = "Скриншот ошибки, мяу")
    public static byte[] saveScreenshot() {
        var scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
        return scrFile;
    }

    public static void generateAllureEnvironment(){
        File source = new File("src/test/resources/environment.properties");
        File dest = new File("target/allure-results");

        var list = List.of("STAND=" + BASE_CONFIG.getYaUrl());
        try {
            synchronized (clock){
                var patch = dest.getAbsoluteFile().getCanonicalPath();
                FileUtils.copyToDirectory(source, dest);
                FileUtils.writeLines(new File(patch + "/environment.properties"), list);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step ("Поиск соответствия слову {1} на странице")

    public static void searchWord (String xpath, String value){
        ChromeDriver driver = new ChromeDriver();


    }

}
