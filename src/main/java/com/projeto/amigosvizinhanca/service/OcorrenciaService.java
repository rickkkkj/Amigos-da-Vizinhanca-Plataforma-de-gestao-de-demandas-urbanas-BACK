package com.projeto.amigosvizinhanca.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.amigosvizinhanca.model.Ocorrencia;
import com.projeto.amigosvizinhanca.model.TipoOcorrencia;
import com.projeto.amigosvizinhanca.model.Usuario;
import com.projeto.amigosvizinhanca.repository.OcorrenciaRepository;
import com.projeto.amigosvizinhanca.repository.TipoOcorrenciaRepository;
import com.projeto.amigosvizinhanca.repository.UsuarioRepository;

@Service
public class OcorrenciaService {

	private final OcorrenciaRepository repository;

	private final UsuarioRepository usuarioRepository;

	private final TipoOcorrenciaRepository tipoOcorrenciaRepository;

	public OcorrenciaService(OcorrenciaRepository repository, UsuarioRepository usuarioRepository,
			TipoOcorrenciaRepository tipoOcorrenciaRepository) {
		this.repository = repository;
		this.usuarioRepository = usuarioRepository;
		this.tipoOcorrenciaRepository = tipoOcorrenciaRepository;
	}

	public Ocorrencia salvarComFoto(Ocorrencia ocorrencia, MultipartFile foto) {

		if (ocorrencia.getUsuario() == null || ocorrencia.getUsuario().getId() == null) {
			throw new RuntimeException("É obrigatório informar o usuário.");
		}

		if (ocorrencia.getTipo() == null || ocorrencia.getTipo().getId() == null) {
			throw new RuntimeException("É obrigatório informar o tipo da ocorrência.");
		}

		Usuario usuario = usuarioRepository.findById(ocorrencia.getUsuario().getId())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
		ocorrencia.setUsuario(usuario);

		TipoOcorrencia tipo = tipoOcorrenciaRepository.findById(ocorrencia.getTipo().getId())
				.orElseThrow(() -> new RuntimeException("Tipo de ocorrência não encontrado."));
		ocorrencia.setTipo(tipo);

		if (foto != null && !foto.isEmpty()) {
			try {

				String nomeArquivo = UUID.randomUUID() + "_" + foto.getOriginalFilename();

				String pastaBase = System.getProperty("user.dir") + "/uploads/ocorrencias/";
				Path destino = Paths.get(pastaBase + nomeArquivo);

				Files.createDirectories(destino.getParent());
				foto.transferTo(destino.toFile());

				ocorrencia.setFoto(nomeArquivo);

			} catch (Exception e) {
				throw new RuntimeException("Erro ao salvar foto: " + e.getMessage());
			}
		}

		return repository.save(ocorrencia);
	}

	public List<Ocorrencia> listarPorUsuario(Long usuarioId) {
		return repository.findByUsuarioId(usuarioId);
	}

	public List<Ocorrencia> listar() {
		return repository.findAll();
	}

	public Ocorrencia buscar(Long id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("Ocorrência não encontrada"));
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<Ocorrencia> filtrar(String bairro, Long tipoId, List<String> status) {

		if (bairro != null && bairro.equalsIgnoreCase("TODOS")) {
			bairro = null;
		}

		return repository.filtrar(bairro, tipoId, status);
	}

}
