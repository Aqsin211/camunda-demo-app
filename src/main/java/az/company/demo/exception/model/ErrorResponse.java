package az.company.demo.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private List<String> messages;
    private OffsetDateTime timestamp;
}