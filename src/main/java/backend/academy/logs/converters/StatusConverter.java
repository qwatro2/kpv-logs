package backend.academy.logs.converters;

public class StatusConverter implements Converter<Integer> {
    @Override
    public String convert(Integer value) {
        if (100 <= value && value <= 103) {
            return convertInfo(value);
        }
        if (200 <= value && value <= 208 || value == 226) {
            return convertSuccess(value);
        }
        if (300 <= value && value <= 309 && value != 306) {
            return convertRedirection(value);
        }
        if (400 <= value && value <= 429 && value != 419 && value != 420 || value == 431 || value == 451) {
            return convertClient(value);
        }
        if (500 <= value && value <= 511 && value != 509) {
            return convertServer(value);
        }
        return "Unknown";
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
