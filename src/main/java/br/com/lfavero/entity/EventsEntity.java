package br.com.lfavero.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "evento")
@AllArgsConstructor
@NoArgsConstructor
public class EventsEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idEvento")
    @Column(name = "id")
    public Long id;

    @JsonProperty("nomeEvento")
    @Column(name = "nomeEvento")
    public String name;

    @JsonProperty("dataInicialEvento")
    @Column(name = "dataInicial")
    public LocalDate initialDate;

    @JsonProperty("dataFinalEvento")
    @Column(name = "dataFinal")
    public LocalDate finalDate;

    @JsonProperty("eventoAtivo")
    @Column(name = "ativo")
    public Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    public InstitutionEntity institution;
}
