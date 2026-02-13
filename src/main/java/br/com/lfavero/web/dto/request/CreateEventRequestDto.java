package br.com.lfavero.web.dto.request;

import java.time.LocalDate;

import br.com.lfavero.validations.ValidDateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

@ValidDateRange
public class CreateEventRequestDto {
    @Size(min = 5, message = "Nome deve ter no mínimo 5 caracteres")
    @NotBlank(message = "Nome do Evento é obrigatório")
    public String nomeEvento;

    @FutureOrPresent(message = "A data deve ser igual ou maior que hoje")
    @NotNull(message = "Data Inicial do Evento é obrigatório")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate dataInicialEvento;

    @FutureOrPresent(message = "A data final não pode ser no passado")
    @NotNull(message = "Data Final do Evento é obrigatório")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate dataFinalEvento;

    @Positive(message = "Instituição inválida")
    @NotNull(message = "Instituição é obrigatório")
    public Long institutionId;

}
