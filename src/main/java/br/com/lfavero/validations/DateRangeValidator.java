package br.com.lfavero.validations;

import br.com.lfavero.web.dto.request.CreateEventRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator
        implements ConstraintValidator<ValidDateRange, CreateEventRequestDto> {

    @Override
    public boolean isValid(CreateEventRequestDto dto,
                           ConstraintValidatorContext context) {

        if (dto.dataInicialEvento == null || dto.dataFinalEvento == null) {
            return true;
        }

        return !dto.dataFinalEvento.isBefore(dto.dataInicialEvento);
    }
}