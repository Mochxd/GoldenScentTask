package utils;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties loader utility for configuration management
 * Handles loading and caching of configuration properties from config.properties
 */
public class PropertiesLoader {
    public static Map<String, String> propLoader = new HashMap<String, String>();
    public static Properties propMain = new Properties();

    public static Map<String, String> configFile() {
        try {
                FileInputStream fisTest = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
                propMain.load(fisTest);
                            propLoader.put("baseUrl", propMain.getProperty("baseUrl"));
                            propLoader.put("loyaltyBalanceEndPoint", propMain.getProperty("loyaltyBalanceEndPoint"));
                            propLoader.put("applyPointsEndPoint", propMain.getProperty("applyPointsEndPoint"));
                            propLoader.put("useWalletEndPoint", propMain.getProperty("useWalletEndPoint"));
                            propLoader.put("walletBalanceEndPoint", propMain.getProperty("walletBalanceEndPoint"));
                            propLoader.put("walletTransactionsEndPoint", propMain.getProperty("walletTransactionsEndPoint"));
                            propLoader.put("refundTriggerEndPoint", propMain.getProperty("refundTriggerEndPoint"));
                            propLoader.put("ordersEndPoint", propMain.getProperty("ordersEndPoint"));
                            propLoader.put("healthEndPoint", propMain.getProperty("healthEndPoint"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return propLoader;
    }
}