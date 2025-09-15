package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class for configuration management and JSON processing
 * Provides centralized access to API endpoints and JSON file operations
 */
public class JavaUtilities {

    private static String baseUrl = PropertiesLoader.configFile().get("baseUrl");
    private static String loyaltyBalanceEndPoint = PropertiesLoader.configFile().get("loyaltyBalanceEndPoint");
    private static String applyPointsEndPoint = PropertiesLoader.configFile().get("applyPointsEndPoint");
    private static String useWalletEndPoint = PropertiesLoader.configFile().get("useWalletEndPoint");
    private static String walletBalanceEndPoint = PropertiesLoader.configFile().get("walletBalanceEndPoint");
    private static String walletTransactionsEndPoint = PropertiesLoader.configFile().get("walletTransactionsEndPoint");
    private static String refundTriggerEndPoint = PropertiesLoader.configFile().get("refundTriggerEndPoint");
    private static String ordersEndPoint = PropertiesLoader.configFile().get("ordersEndPoint");
    private static String healthEndPoint = PropertiesLoader.configFile().get("healthEndPoint");

    public static String getBaseUrl() {
        return baseUrl;
    }
    public static String getLoyaltyBalanceEndPoint() {
        return loyaltyBalanceEndPoint;
    }
    public static String getApplyPointsEndPoint() {
        return applyPointsEndPoint;
    }
    public static String getUseWalletEndPoint() {
        return useWalletEndPoint;
    }
    public static String getWalletBalanceEndPoint() {
        return walletBalanceEndPoint;
    }
    public static String getWalletTransactionsEndPoint() {
        return walletTransactionsEndPoint;
    }
    public static String getRefundTriggerEndPoint() {
        return refundTriggerEndPoint;
    }
    public static String getOrdersEndPoint() {
        return ordersEndPoint;
    }
    public static String getHealthEndPoint() {
        return healthEndPoint;
    }

    public static String jsonReader(String jsonFilePath) throws FileNotFoundException, IOException {

        final Gson g = new Gson();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJ;
        Object o = g.fromJson(new FileReader(jsonFilePath), Object.class);

        prettyJ = gson.toJson(o);

        return prettyJ;

    }
}
