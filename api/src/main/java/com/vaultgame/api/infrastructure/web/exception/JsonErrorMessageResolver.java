package com.vaultgame.api.infrastructure.web.exception;

import com.vaultgame.api.domain.exception.DomainException;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.databind.exc.InvalidTypeIdException;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.exc.UnrecognizedPropertyException;

public final class JsonErrorMessageResolver {

    private static final String PRODUCT_DETAILS_KINDS =
            "GAME, BOARD_GAME, HARDWARE, PERIPHERAL, ACCESSORY, FURNITURE, ACTION_FIGURE, MERCHANDISE, DIGITAL_CARD";

    private JsonErrorMessageResolver() {}

    public static String resolve(Throwable throwable) {
        DomainException domain = findDomainException(throwable);
        if (domain != null) {
            return domain.getMessage();
        }

        Throwable current = throwable;
        while (current != null) {
            String message = messageFor(current);
            if (message != null) {
                return message;
            }
            current = current.getCause();
        }

        return "Invalid request body";
    }

    private static String messageFor(Throwable throwable) {
        if (throwable instanceof InvalidFormatException invalidFormat) {
            return "Invalid value '%s' for %s"
                    .formatted(invalidFormat.getValue(), fieldReference(invalidFormat));
        }
        if (throwable instanceof InvalidTypeIdException invalidTypeId) {
            if (invalidTypeId.getTypeId() == null) {
                return "Missing type discriminator in JSON. For product details use property 'kind' with one of: "
                        + PRODUCT_DETAILS_KINDS;
            }
            return "Unknown type '%s' in JSON. For product details 'kind' must be one of: "
                    .formatted(invalidTypeId.getTypeId())
                    + PRODUCT_DETAILS_KINDS;
        }
        if (throwable instanceof UnrecognizedPropertyException unrecognized) {
            return "Unknown property '%s' in %s"
                    .formatted(unrecognized.getPropertyName(), fieldReference(unrecognized));
        }
        if (throwable instanceof MismatchedInputException mismatched) {
            String reference = fieldReference(mismatched);
            if (reference != null && !reference.isBlank()) {
                return "Invalid or missing value for %s".formatted(reference);
            }
            return "Request body does not match the expected JSON structure";
        }
        if (throwable instanceof StreamReadException streamRead) {
            var location = streamRead.getLocation();
            return "Malformed JSON at line %d, column %d"
                    .formatted(location.getLineNr(), location.getColumnNr());
        }
        return null;
    }

    private static String fieldReference(MismatchedInputException exception) {
        String reference = exception.getPathReference();
        if (reference != null && !reference.isBlank()) {
            return reference;
        }
        if (exception.getPath() != null && !exception.getPath().isEmpty()) {
            return exception.getPath().getLast().getPropertyName();
        }
        return null;
    }

    private static DomainException findDomainException(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof DomainException domainException) {
                return domainException;
            }
            current = current.getCause();
        }
        return null;
    }
}
