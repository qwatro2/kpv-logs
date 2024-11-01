package backend.academy.logs.converters;

import backend.academy.logs.types.StatusCodes;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class StatusConverter implements Converter<Integer> {
    @Override
    public String convert(Integer value) {
        HashMap<Predicate<Integer>, Function<Integer, String>> predicateFunctionHashMap = new HashMap<>();
        predicateFunctionHashMap.put(StatusCodes::isInfoCode, this::convertInfo);
        predicateFunctionHashMap.put(StatusCodes::isSuccessCode, this::convertSuccess);
        predicateFunctionHashMap.put(StatusCodes::isRedirectionCode, this::convertRedirection);
        predicateFunctionHashMap.put(StatusCodes::isClientCode, this::convertClient);
        predicateFunctionHashMap.put(StatusCodes::isServerCode, this::convertServer);

        for (Predicate<Integer> predicate : predicateFunctionHashMap.keySet()) {
            if (predicate.test(value)) {
                return predicateFunctionHashMap.get(predicate).apply(value);
            }
        }
        return "Unknown";
    }

    private String convertInfo(Integer value) {
        return switch (value) {
            case StatusCodes.CONTINUE -> "Continue";
            case StatusCodes.SWITCHING_PROTOCOLS -> "Switching Protocols";
            case StatusCodes.PROCESSING -> "Processing";
            case StatusCodes.EARLY_HINTS -> "Early Hints";
            default -> null;
        };
    }

    private String convertSuccess(Integer value) {
        return switch (value) {
            case StatusCodes.OK -> "OK";
            case StatusCodes.CREATED -> "Created";
            case StatusCodes.ACCEPTED -> "Accepted";
            case StatusCodes.NON_AUTHORITATIVE_INFORMATION -> "Non-Authoritative Information";
            case StatusCodes.NO_CONTENT -> "No Content";
            case StatusCodes.RESET_CONTENT -> "Reset Content";
            case StatusCodes.PARTIAL_CONTENT -> "Partial Content";
            case StatusCodes.MULTI_STATUS -> "Multi-Status";
            case StatusCodes.ALREADY_REPORTED -> "Already Reported";
            case StatusCodes.IM_USED -> "IM Used";
            default -> null;
        };
    }

    private String convertRedirection(Integer value) {
        return switch (value) {
            case StatusCodes.MULTIPLE_CHOICES -> "Multiple Choices";
            case StatusCodes.MOVED_PERMANENTLY -> "Moved Permanently";
            case StatusCodes.FOUND -> "Found";
            case StatusCodes.SEE_OTHER -> "See Other";
            case StatusCodes.NOT_MODIFIED -> "Not Modified";
            case StatusCodes.USE_PROXY -> "Use Proxy";
            case StatusCodes.TEMPORARY_REDIRECT -> "Temporary Redirect";
            case StatusCodes.PERMANENT_REDIRECT -> "Permanent Redirect";
            default -> null;
        };
    }

    private String convertClient(Integer value) {
        Function<Integer, String> convertFunction;
        if (StatusCodes.is40xCode(value)) {
            convertFunction = this::convertClient40x;
        } else if (StatusCodes.is41xCode(value)) {
            convertFunction = this::convertClient41x;
        } else if (StatusCodes.is42xCode(value)) {
            convertFunction = this::convertClient42x;
        } else if (StatusCodes.is4xxCodeOther(value)) {
            convertFunction = this::convertClientOther;
        } else {
            convertFunction = (v) -> null;
        }
        return convertFunction.apply(value);
    }

    private String convertServer(Integer value) {
        return switch (value) {
            case StatusCodes.INTERNAL_SERVER_ERROR -> "Internal Server Error";
            case StatusCodes.NOT_IMPLEMENTED -> "Not Implemented";
            case StatusCodes.BAD_GATEWAY -> "Bad Gateway";
            case StatusCodes.SERVICE_UNAVAILABLE -> "Service Unavailable";
            case StatusCodes.GATEWAY_TIMEOUT -> "Gateway Timeout";
            case StatusCodes.HTTP_VERSION_NOT_SUPPORTED -> "HTTP Version Not Supported";
            case StatusCodes.VARIANT_ALSO_NEGOTIATES -> "Variant Also Negotiates";
            case StatusCodes.INSUFFICIENT_STORAGE -> "Insufficient Storage";
            case StatusCodes.LOOP_DETECTED -> "Loop Detected";
            case StatusCodes.NOT_EXTENDED -> "Not Extended";
            case StatusCodes.NETWORK_AUTHENTICATION_REQUIRED -> "Network Authentication Required";
            default -> null;
        };
    }

    private String convertClient40x(Integer value) {
        return switch (value) {
            case StatusCodes.BAD_REQUEST -> "Bad Request";
            case StatusCodes.UNAUTHORIZED -> "Unauthorized";
            case StatusCodes.PAYMENT_REQUIRED -> "Payment Required";
            case StatusCodes.FORBIDDEN -> "Forbidden";
            case StatusCodes.NOT_FOUND -> "Not Found";
            case StatusCodes.METHOD_NOT_ALLOWED -> "Method Not Allowed";
            case StatusCodes.NOT_ACCEPTABLE -> "Not Acceptable";
            case StatusCodes.PROXY_AUTHENTICATION_REQUIRED -> "Proxy Authentication Required";
            case StatusCodes.REQUEST_TIMEOUT -> "Request Timeout";
            case StatusCodes.CONFLICT -> "Conflict";
            default -> null;
        };
    }

    private String convertClient41x(Integer value) {
        return switch (value) {
            case StatusCodes.GONE -> "Gone";
            case StatusCodes.LENGTH_REQUIRED -> "Length Required";
            case StatusCodes.PRECONDITION_FAILED -> "Precondition Failed";
            case StatusCodes.CONTENT_TOO_LARGE -> "Content Too Large";
            case StatusCodes.URI_TOO_LONG -> "URI Too Long";
            case StatusCodes.UNSUPPORTED_MEDIA_TYPE -> "Unsupported Media Type";
            case StatusCodes.RANGE_NOT_SATISFIABLE -> "Range Not Satisfiable";
            case StatusCodes.EXPECTATION_FAILED -> "Expectation Failed";
            case StatusCodes.IM_A_TEAPOT -> "I'm a teapot";
            default -> null;
        };
    }

    private String convertClient42x(Integer value) {
        return switch (value) {
            case StatusCodes.MISDIRECTED_REQUEST -> "Misdirected Request";
            case StatusCodes.UNPROCESSABLE_CONTENT -> "Unprocessable Content";
            case StatusCodes.LOCKED -> "Locked";
            case StatusCodes.FAILED_DEPENDENCY -> "Failed Dependency";
            case StatusCodes.TOO_EARLY -> "Too Early";
            case StatusCodes.UPGRADE_REQUIRED -> "Upgrade Required";
            case StatusCodes.PRECONDITION_REQUIRED -> "Precondition Required";
            case StatusCodes.TOO_MANY_REQUESTS -> "Too Many Requests";
            default -> null;
        };
    }

    private String convertClientOther(Integer value) {
        return switch (value) {
            case StatusCodes.REQUEST_HEADER_FIELDS_TOO_LARGE -> "Request Header Fields Too Large";
            case StatusCodes.UNAVAILABLE_FOR_LEGAL_REASONS -> "Unavailable For Legal Reasons";
            default -> null;
        };
    }
}
