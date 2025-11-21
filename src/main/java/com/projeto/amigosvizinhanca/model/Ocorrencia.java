package com.projeto.amigosvizinhanca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ocorrencias")
public class Ocorrencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String titulo;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;

	@Embedded
	private Endereco endereco;

	@Column(nullable = true)
	private String foto;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NivelUrgencia urgencia;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusOcorrencia status;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "tipo_id", nullable = false)
	private TipoOcorrencia tipo;


}
