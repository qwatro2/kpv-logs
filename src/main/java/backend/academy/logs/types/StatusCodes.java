package backend.academy.logs.types;

public class StatusCodes {
    public static final int CONTINUE = 100;
    public static final int SWITCHING_PROTOCOLS = 101;
    public static final int PROCESSING = 102;
    public static final int EARLY_HINTS = 103;

    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NON_AUTHORITATIVE_INFORMATION = 203;
    public static final int NO_CONTENT = 204;
    public static final int RESET_CONTENT = 205;
    public static final int PARTIAL_CONTENT = 206;
    public static final int MULTI_STATUS = 207;
    public static final int ALREADY_REPORTED = 208;
    public static final int IM_USED = 226;

    public static final int MULTIPLE_CHOICES = 300;
    public static final int MOVED_PERMANENTLY = 301;
    public static final int FOUND = 302;
    public static final int SEE_OTHER = 303;
    public static final int NOT_MODIFIED = 304;
    public static final int USE_PROXY = 305;
    public static final int TEMPORARY_REDIRECT = 307;
    public static final int PERMANENT_REDIRECT = 308;

    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int PAYMENT_REQUIRED = 402;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int NOT_ACCEPTABLE = 406;
    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int CONFLICT = 409;

    public static final int GONE = 410;
    public static final int LENGTH_REQUIRED = 411;
    public static final int PRECONDITION_FAILED = 412;
    public static final int CONTENT_TOO_LARGE = 413;
    public static final int URI_TOO_LONG = 414;
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int RANGE_NOT_SATISFIABLE = 416;
    public static final int EXPECTATION_FAILED = 417;
    public static final int IM_A_TEAPOT = 418;

    public static final int MISDIRECTED_REQUEST = 421;
    public static final int UNPROCESSABLE_CONTENT = 422;
    public static final int LOCKED = 423;
    public static final int FAILED_DEPENDENCY = 424;
    public static final int TOO_EARLY = 425;
    public static final int UPGRADE_REQUIRED = 426;
    public static final int PRECONDITION_REQUIRED = 428;
    public static final int TOO_MANY_REQUESTS = 429;

    public static final int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;

    public static final int UNAVAILABLE_FOR_LEGAL_REASONS = 451;

    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;
    public static final int VARIANT_ALSO_NEGOTIATES = 506;
    public static final int INSUFFICIENT_STORAGE = 507;
    public static final int LOOP_DETECTED = 508;
    public static final int NOT_EXTENDED = 510;
    public static final int NETWORK_AUTHENTICATION_REQUIRED = 511;

    private static final int UNUSED_1 = 306;
    private static final int UNUSED_2 = 419;
    private static final int UNUSED_3 = 420;
    private static final int UNUSED_4 = 509;

    public static boolean isInfoCode(Integer value) {
        return CONTINUE <= value && value <= EARLY_HINTS;
    }

    public static boolean isSuccessCode(Integer value) {
        return OK <= value && value <= ALREADY_REPORTED || value == IM_USED;
    }

    public static boolean isRedirectionCode(Integer value) {
        return MULTIPLE_CHOICES <= value && value <= PERMANENT_REDIRECT && value != UNUSED_1;
    }

    public static boolean isClientCode(Integer value) {
        return BAD_REQUEST <= value && value <= TOO_MANY_REQUESTS && value != UNUSED_2 && value != UNUSED_3
            || value == REQUEST_HEADER_FIELDS_TOO_LARGE || value == UNAVAILABLE_FOR_LEGAL_REASONS;
    }

    public static boolean isServerCode(Integer value) {
        return INTERNAL_SERVER_ERROR <= value && value <= NETWORK_AUTHENTICATION_REQUIRED && value != UNUSED_4;
    }

    public static boolean is40xCode(Integer value) {
        return isClientCode(value) && value <= CONFLICT;
    }

    public static boolean is41xCode(Integer value) {
        return isClientCode(value) && value <= IM_A_TEAPOT;
    }

    public static boolean is42xCode(Integer value) {
        return isClientCode(value) && value <= TOO_MANY_REQUESTS;
    }

    public static boolean is4xxCodeOther(Integer value) {
        return value == REQUEST_HEADER_FIELDS_TOO_LARGE || value == UNAVAILABLE_FOR_LEGAL_REASONS;
    }
}
