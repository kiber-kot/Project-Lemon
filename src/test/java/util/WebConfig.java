package util;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Config.Sources({"classpath:${env}.properties"})
public interface WebConfig extends Config {

    WebConfig BASE_CONFIG = ConfigFactory.create(WebConfig.class, System.getenv(), System.getProperties());

    @Key("ya.url")
    @DefaultValue("https://ya.ru")
    String getYaUrl();

    @Key("http.connection.timeout")
    @DefaultValue("3000")
    String httpConnectionTimeout();

    @Key("log.network")
    @DefaultValue("true")
    boolean logNetwork();

    @Key("enabled.test.NameClass")
    @DefaultValue("false")
    boolean enableTest();

}