package br.com.lfavero.web.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

public class CreateEventRequestDto {

    @NotNull(message = "Nome do Evento é obrigatório")
    public String nomeEvento;

    @NotNull(message = "Data Inicial do Evento é obrigatório")
    public LocalDate dataInicialEvento;

    @NotNull(message = "Data Final do Evento é obrigatório")
    public LocalDate dataFinalEvento;

    private Boolean eventoAtivo;

    @NotNull(message = "Instituição é obrigatório")
    public Long institutionId;

}
