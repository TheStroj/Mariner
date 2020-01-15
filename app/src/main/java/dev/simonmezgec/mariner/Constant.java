package dev.simonmezgec.mariner;

/** Class containing all the constants used throughout the app. */
public class Constant {

    /* Preferences constants. */
    public static final String SHARED_PREFERENCES = "preferences";
    public static final String PREFERENCES_READY_TO_SEARCH = "preferences_ready_to_search";
    public static final String PREFERENCES_TOTAL_DATA_ASSETS = "total_data_assets";

    /* General constants. */
    public static final String DATABASE_NAME = "mariner_database";
    public static final String OCEAN_TOKENS = "OCEAN";
    public static final String READY_TO_SEARCH = "ready_to_search";
    public static final String DISPLAY_WARNING = "display_warning";
    public static final String CURRENT_SORT = "current_sort";
    public static final String CURRENT_SORT_FAVORITES = "current_sort_favorites";

    /* Network utilities constants. */
    public static final String BASE_SEARCH_URL
            = "https://agent.oceanprotocol.com/api/assets/searchtext";
    public static final String TEXT_PARAMETER = "text";
    public static final String OFFSET_PARAMETER = "offset";
    public static final String OFFSET_VALUE = "10000";
    public static final String HOSTNAME = "8.8.8.8";
    public static final int PORT = 53;
    public static final int TIMEOUT = 1500;

    /* JSON parsing constants. */
    public static final String JSON_RESULTS = "results";
    public static final String JSON_PUBLIC_KEY = "publicKey";
    public static final String JSON_ID = "id";
    public static final String JSON_OWNER = "owner";
    public static final String JSON_SERVICE = "service";
    public static final String JSON_ATTRIBUTES = "attributes";
    public static final String JSON_MAIN = "main";
    public static final String JSON_TYPE = "type";
    public static final String JSON_NAME = "name";
    public static final String JSON_AUTHOR = "author";
    public static final String JSON_LICENSE = "license";
    public static final String JSON_PRICE = "price";
    public static final String JSON_DATE_CREATED = "dateCreated";
    public static final String JSON_DATE_PUBLISHED = "datePublished";
    public static final String JSON_FILES = "files";
    public static final String JSON_CONTENT_TYPE = "contentType";
    public static final String JSON_CONTENT_LENGTH = "contentLength";
    public static final String JSON_ADDITIONAL_INFORMATION = "additionalInformation";
    public static final String JSON_TAGS = "tags";
    public static final String JSON_CATEGORIES = "categories";
    public static final String JSON_DESCRIPTION = "description";
    public static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String JSON_UNKNOWN_VALUE = "unknown";
    public static final String JSON_UNKNOWN_NUMBER = "0";
    public static final String JSON_COMMONS_LINK = "https://commons.oceanprotocol.com/asset/";

    /* Formatting constants. */
    public static final String DECIMAL_FORMAT = "0.#";
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String TIME_ZONE = "CET";
}
