package br.com.lfavero.exception;

import br.com.lfavero.web.dto.response.ErrorResponse;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.WebApplicationException;
import jakarta.validation.ConstraintViolationException;
import com.fasterxml.jackson.core.JsonParseException;

import org.jboss.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG =
            Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {

        LOG.error("Erro capturado:", exception);

        if (exception instanceof ConstraintViolationException e) {
            String message = e.getConstraintViolations()
                    .iterator()
                    .next()
                    .getMessage();

            return buildResponse(400, "Validation Error", message);
        }

        if (exception instanceof JsonParseException) {
            return buildResponse(
                    400,
                    "JSON Error",
                    "Formato de JSON inválido"
            );
        }

        if (exception instanceof EventNotFoundException e) {
            return buildResponse(
                    404,
                    "Not Found",
                    e.getMessage()
            );
        }

        if (exception instanceof PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof org.hibernate.exception.ConstraintViolationException h) {
                String constraint = h.getConstraintName();
                if (constraint != null &&
                        constraint.toLowerCase().contains("uk")) {
                    return buildResponse(
                            409,
                            "Conflict",
                            "Registro duplicado"
                    );
                }

                if (constraint != null &&
                        constraint.toLowerCase().contains("fk")) {
                    return buildResponse(
                            400,
                            "Bad Request",
                            "Relacionamento inválido"
                    );
                }
            }
            return buildResponse(
                    500,
                    "Database Error",
                    "Erro ao acessar banco de dados"
            );
        }

        if (exception instanceof WebApplicationException e) {
            int status = e.getResponse().getStatus();
            return buildResponse(
                    status,
                    "Request Error",
                    e.getMessage()
            );
        }

        return buildResponse(
                500,
                "Internal Server Error",
                "Erro interno no servidor"
        );
    }

    private Response buildResponse(
            int status,
            String error,
            String message
    ) {
        ErrorResponse response =
                new ErrorResponse(status, error, message);

        return Response
                .status(status)
                .entity(response)
                .build();
    }
}