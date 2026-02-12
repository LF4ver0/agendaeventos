package br.com.lfavero.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "instituicao")
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idInstituicao")
    @Column(name = "id")
    public Long id;

    @JsonProperty("nomeInstituicao")
    @Column(name = "nome")
    public String name;

    @JsonProperty("tipoInstituicao")
    @Column(name = "tipo")
    public String type;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventsEntity> events;
}