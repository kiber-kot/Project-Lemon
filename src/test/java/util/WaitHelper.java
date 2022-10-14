package util;

import io.qameta.allure.Step;
import lombok.SneakyThrows;

public class WaitHelper {

    @SneakyThrows
    public static void sleep(long millis){
            Thread.sleep(millis);
    }

    @Step("Ожидание загрузки страницы")
    public static void waitLoader(String xpath){
        for(int i = 0; i < 5; i++){
            var isLoad = ElementUtil.getPageLoader(xpath);
            if(isLoad){
                sleep(500);
                i++;
            } else {
                return;
            }
        }
    }

}
