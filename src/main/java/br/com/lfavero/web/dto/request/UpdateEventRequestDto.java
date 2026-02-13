package br.com.lfavero.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class UpdateEventRequestDto{

    @Size(min = 5, message = "Nome deve ter no mínimo 5 caracteres")
    public String nomeEvento;

    @FutureOrPresent(message = "Data final não pode ser no passado")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate dataFinalEvento;

}
