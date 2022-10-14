package tc;

import enumirated.SearchValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import step.YaMainMainStep;
import step.YaSearchValueStep;
import util.ElementUtil;
import util.MainUtil;

import static constant.AllConstant.*;


@DisplayName("01_Тест_Авторизация")
public class YaTest extends MainUtil  {
    private final YaMainMainStep yaMainStep = new YaMainMainStep();
    private final YaSearchValueStep yaSearchValueStep = new YaSearchValueStep();


    @Test
    @DisplayName("Авторизация клиента Юр.Лица")
    public void yaTest() {
        ElementUtil.goToUrl(URL);
        yaMainStep.search(SearchValue.WIKI);
        yaSearchValueStep.searchEquals("Википедия");
    }

}



