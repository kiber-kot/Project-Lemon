package step;

import enumirated.SearchValue;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import page.YaMainPage;
import util.ElementUtil;

public class YaMainMainStep implements YaMainPage {

    @Step("Проверка поиска в ya.ru")
    public void search(SearchValue searchValue){
        ElementUtil.sendKeys(INPUT_SEARCH, searchValue.getValue());
        ElementUtil.sendKeys(INPUT_SEARCH, Keys.ENTER);
    }

}
