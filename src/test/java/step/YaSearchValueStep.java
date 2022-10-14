package step;

import io.qameta.allure.Step;
import page.YaSearchValuePage;
import util.ElementUtil;

public class YaSearchValueStep implements YaSearchValuePage {

    @Step("Проверка поиска что результат на странице соответствует переданному значению {0}")
    public void searchEquals(String searchValue){
        ElementUtil.equals(MAIN_SEARCH_VALUE, searchValue);
    }
}
