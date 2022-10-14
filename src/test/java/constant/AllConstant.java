package constant;



import static util.WebConfig.BASE_CONFIG;

public class AllConstant {
    //Адрес портала
    public static final String URL = BASE_CONFIG.getYaUrl();

    //Поиск ошибок в логах консоли разработчика
    public static final String CONSOLE_ERRORS_500 = "[5][0-9][0-9]";
    public static final String CONSOLE_ERRORS_400 = "[4][0-9][0-9]";
    public static final String CONSOLE_ERRORS_ALL = "[4-5][0-9][0-9]";

}
