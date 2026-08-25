package az.company.demo.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessages {

    NOT_FOUND("Resource not found"),
    BAD_REQUEST("Bad request"),
    INSUFFICIENT_STOCK("Insufficient stock"),
    INTERNAL_SERVER_ERROR("Internal server error");

    private final String message;
}