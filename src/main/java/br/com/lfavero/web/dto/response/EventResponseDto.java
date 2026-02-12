package br.com.lfavero.web.dto.response;

import java.time.LocalDate;

public class EventResponseDto {

    public Long idEvento;
    public String nomeEvento;
    public LocalDate dataInicialEvento;
    public LocalDate dataFinalEvento;
    public Boolean eventoAtivo;

    public Long idInstituicao;
    public String institutionName;

}
