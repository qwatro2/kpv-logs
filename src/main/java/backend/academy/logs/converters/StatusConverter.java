package backend.academy.logs.converters;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class StatusConverter implements Converter<Integer> {
    @Override
    public String convert(Integer value) {
        HashMap<Predicate<Integer>, Function<Integer, String>> predicateFunctionHashMap = new HashMap<>();
        predicateFunctionHashMap.put(this::infoPredicate, this::convertInfo);
        predicateFunctionHashMap.put(this::successPredicate, this::convertSuccess);
        predicateFunctionHashMap.put(this::redirectionPredicate, this::convertRedirection);
        predicateFunctionHashMap.put(this::clientPredicate, this::convertClient);
        predicateFunctionHashMap.put(this::serverPredicate, this::convertServer);

        for (Predicate<Integer> predicate : predicateFunctionHashMap.keySet()) {
            if (predicate.test(value)) {
                return predicateFunctionHashMap.get(predicate).apply(value);
            }
        }
        return "Unknown";
    }

    private boolean infoPredicate(Integer value) {
        return 100 <= value && value <= 103;
    }

    private String convertInfo(Integer value) {
        return switch (value) {
            case 100 -> "Continue";
            case 101 -> "Switching Protocols";
            case 102 -> "Processing";
            case 103 -> "Early Hints";
            default -> null;
        };
    }

    private boolean successPredicate(Integer value) {
        return 200 <= value && value <= 208 || value == 226;
    }

    private String convertSuccess(Integer value) {
        return switch (value) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 203 -> "Non-Authoritative Information";
            case 204 -> "No Content";
            case 205 -> "Reset Content";
            case 206 -> "Partial Content";
            case 207 -> "Multi-Status";
            case 208 -> "Already Reported";
            case 226 -> "IM Used";
            default -> null;
        };
    }

    private boolean redirectionPredicate(Integer value) {
        return 300 <= value && value <= 309 && value != 306;
    }

    private String convertRedirection(Integer value) {
        return switch (value) {
            case 300 -> "Multiple Choices";
            case 301 -> "Moved Permanently";
            case 302 -> "Found";
            case 303 -> "See Other";
            case 304 -> "Not Modified";
            case 305 -> "Use Proxy";
            case 307 -> "Temporary Redirect";
            case 308 -> "Permanent Redirect";
            default -> null;
        };
    }

    private boolean clientPredicate(Integer value) {
        return 400 <= value && value <= 429 && value != 419 && value != 420 || value == 431 || value == 451;
    }

    private String convertClient(Integer value) {
        if (value < 410) {
            return convertClient40x(value);
        }
        if (value < 420) {
            return convertClient41x(value);
        }
        if (value < 430) {
            return convertClient42x(value);
        }
        return switch (value) {
            case 431 -> "Request Header Fields Too Large";
            case 451 -> "Unavailable For Legal Reasons";
            default -> null;
        };
    }

    private boolean serverPredicate(Integer value) {
        return 500 <= value && value <= 511 && value != 509;
    }

    private String convertServer(Integer value) {
        return switch (value) {
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            case 504 -> "Gateway Timeout";
            case 505 -> "HTTP Version Not Supported";
            case 506 -> "Variant Also Negotiates";
            case 507 -> "Insufficient Storage";
            case 508 -> "Loop Detected";
            case 510 -> "Not Extended";
            case 511 -> "Network Authentication Required";
            default -> null;
        };
    }

    private String convertClient40x(Integer value) {
        return switch (value) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 402 -> "Payment Required";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 406 -> "Not Acceptable";
            case 407 -> "Proxy Authentication Required";
            case 408 -> "Request Timeout";
            case 409 -> "Conflict";
            default -> null;
        };
    }

    private String convertClient41x(Integer value) {
        return switch (value) {
            case 410 -> "Gone";
            case 411 -> "Length Required";
            case 412 -> "Precondition Failed";
            case 413 -> "Content Too Large";
            case 414 -> "URI Too Long";
            case 415 -> "Unsupported Media Type";
            case 416 -> "Range Not Satisfiable";
            case 417 -> "Expectation Failed";
            case 418 -> "I'm a teapot";
            default -> null;
        };
    }

    private String convertClient42x(Integer value) {
        return switch (value) {
            case 421 -> "Misdirected Request";
            case 422 -> "Unprocessable Content";
            case 423 -> "Locked";
            case 424 -> "Failed Dependency";
            case 425 -> "Too Early";
            case 426 -> "Upgrade Required";
            case 428 -> "Precondition Required";
            case 429 -> "Too Many Requests";
            default -> null;
        };
    }
}
