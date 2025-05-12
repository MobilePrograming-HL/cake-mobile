package nix.cake.android.constant;

public class Constants {
    public static final String DB_NAME = "room";
    public static final String PREF_NAME = "mvvm.prefs";

    public static final String VALUE_BEARER_TOKEN_DEFAULT="NULL";

    //Local Action manager
    public static final String ACTION_EXPIRED_TOKEN ="ACTION_EXPIRED_TOKEN";
    public static final String INSTAGRAM_LOGIN_URL = "https://www.instagram.com/accounts/login/";
    public static final String INSTAGRAM_URL = "https://www.instagram.com/";

    public static final Integer KIND_PROVINCE = 1;
    public static final Integer KIND_DISTRICT = 2;
    public static final Integer KIND_COMMUNE = 3;
    public static final String PROVINCE = "Province";
    public static final String DISTRICT = "District";
    public static final String COMMUNE = "Commune";
    public static final int TYPE_EDIT = 0;
    public static final int TYPE_CREATE = 1;
    public static final int PAGE_START = 0;
    public static final int SIZE_ITEM = 10000;
    public static final String TYPE_CHOOSE_ADDRESS = "TYPE_CHOOSE_ADDRESS";
    public static final Integer ORDER_STATUS_ALL = null;
    public static final int ORDER_STATUS_PENDING = 1;
    public static final int ORDER_STATUS_PROCESSING = 2;
    public static final int ORDER_STATUS_SHIPPING = 3;
    public static final int ORDER_STATUS_DELIVERED = 4;
    public static final int ORDER_STATUS_CANCELED = 5;
    public static final String STANDARD_SHIPPING = "Standard Shipping";
    public static final String FAST_SHIPPING = "Fast Shipping";
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final int TYPE_ORDER_BUY_NOW = 1;
    public static final int TYPE_ORDER_OTHER = 2;
    private Constants(){

    }
}
