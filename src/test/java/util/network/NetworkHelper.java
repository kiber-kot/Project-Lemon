package util.network;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v110.network.Network;
import util.MainUtil;
import util.WaitHelper;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Данный класс предназначен для работы с консолью разработчика по вкладке Network, а именно получать логи.
 * Пример использования:
 * Перед выполнения проверки где надо собирать логи из консоли пишем такой код:
 * NetworkHelper networkHelper = new NetworkHelper();
 * var network = networkHelper.starter();
 * <p>
 * Далее идет тестовй метод
 * ElementUtil.goToUrl(URL);
 * После используем закрытие сессии и получение объекта с данными по нужному регулярное выражение статус кода
 * networkHelper.checkStatusCodeInNetworkConsole(network, "[4-5][0-9][0-9]");
 * <p>
 * Возвращает List<DevToolEntity>
 */
@Slf4j
public class NetworkHelper extends MainUtil {

    @Step("Начало логирования в консоли разработчика в Network")
    public NetworkEntity starter() {
        ChromeDriver driver = (ChromeDriver) getDriver();
        DevTools devTool = driver.getDevTools();
        devTool.createSession();
        devTool.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        NetworkEntity networkEntity = new NetworkEntity();
        networkEntity.setDevTool(devTool);
        List<DevToolEntity> toolEntityList = new ArrayList<>();
        networkEntity.getDevTool().addListener(Network.responseReceived(), requestSent -> {
            DevToolEntity devToolEntity = new DevToolEntity();
            devToolEntity.setUrl(requestSent.getResponse().getUrl());
            devToolEntity.setStatusCode(requestSent.getResponse().getStatus().toString());
            devToolEntity.setStatusText(requestSent.getResponse().getStatusText());
            devToolEntity.setHeaderResponse(requestSent.getResponse().getHeaders().toJson());
            if (requestSent.getResponse().getRequestHeaders().isPresent()) {
                devToolEntity.setRequestHeaders(requestSent.getResponse().getRequestHeaders().get());
            }
            toolEntityList.add(devToolEntity);
            networkEntity.setDevToolEntityList(toolEntityList);
        });
        return networkEntity;
    }

    @Step("Окончание логирования. Поиск в консоли разработчика ошибок в Response с регулярным выражением (Статус код) = {1}")
    public void checkStatusCodeInNetworkConsole(NetworkEntity network, String statusCode) {
        synchronized (this) {
            WaitHelper.sleep(500);
            List<DevToolEntity> resultLog = new ArrayList<>();
            try {
                resultLog = network.getDevToolEntityList()
                        .parallelStream()
                        .filter(status -> status.getStatusCode().matches(statusCode))
                        .toList();
            } catch (ConcurrentModificationException e) {
                fail("Произошла ошибка связанная с многопоточностью в методе checkStatusCodeInNetworkConsole()");
            }
            WaitHelper.sleep(1000);
            network.getDevTool().close();
            if (resultLog.size() != 0) {
                fail("В логах консоли разработчика присутствуют ошибки: \n" + resultLog);
            }
        }
    }

}