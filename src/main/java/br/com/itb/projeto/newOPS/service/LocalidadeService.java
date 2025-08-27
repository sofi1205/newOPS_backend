package br.com.itb.projeto.newOPS.service;

import br.com.itb.projeto.newOPS.model.entity.Localidade;
import br.com.itb.projeto.newOPS.model.repository.LocalidadeRepository;
import br.com.itb.projeto.newOPS.rest.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalidadeService {

    private final LocalidadeRepository localidadeRepository;

    public LocalidadeService(LocalidadeRepository localidadeRepository) {
        this.localidadeRepository = localidadeRepository;
    }

    public Localidade findById(long id) {
        Optional<Localidade> localidade = localidadeRepository.findById(id);
        return localidade.orElse(null);
    }

    public List<Localidade> findAll() {
        return localidadeRepository.findAll();
    }

    @Transactional
    public Localidade save(Localidade localidade) {
        return localidadeRepository.save(localidade);
    }

    public void deleteById(long id) {
        if (localidadeRepository.existsById(id)) {
            localidadeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Localidade não encontrada com o ID: " + id);
        }
    }

    @Transactional
    public Localidade alterarLocalidade(long id, Localidade localidadeAtualizada) {
        return localidadeRepository.findById(id)
                .map(localidadeExistente -> {
                    localidadeExistente.setNome(localidadeAtualizada.getNome());
                    localidadeExistente.setStatusLocal(localidadeAtualizada.getStatusLocal());
                    return localidadeRepository.save(localidadeExistente);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Localidade com ID: " + id + " não encontrada."));
    }

    public boolean existsByName(String nome) {
        return localidadeRepository.existsByNome(nome);
    }
}
