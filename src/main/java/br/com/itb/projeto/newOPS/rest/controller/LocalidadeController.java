package br.com.itb.projeto.newOPS.rest.controller;

import br.com.itb.projeto.newOPS.model.entity.Localidade;
import br.com.itb.projeto.newOPS.rest.exception.ResourceNotFoundException;
import br.com.itb.projeto.newOPS.service.LocalidadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/localidade")
public class LocalidadeController {

    private final LocalidadeService localidadeService;

    public LocalidadeController(LocalidadeService localidadeService) {
        this.localidadeService = localidadeService;
    }

    // Método para buscar todas as localidades
    @GetMapping
    public ResponseEntity<List<Localidade>> findAll() {
        List<Localidade> localidades = localidadeService.findAll();
        return ResponseEntity.ok(localidades);
    }

    // Método para buscar uma localidade por ID
    @GetMapping("/{id}")
    public ResponseEntity<Localidade> findById(@PathVariable long id) {
        Localidade localidade = localidadeService.findById(id);
        if (localidade != null) {
            return ResponseEntity.ok(localidade);
        }
        throw new ResourceNotFoundException("Localidade não encontrada com ID: " + id);
    }

    // Método para verificar se a localidade já existe (usado para evitar duplicidade)
    @GetMapping("/verificar-duplicidade")
    public ResponseEntity<Boolean> verificarDuplicidade(@RequestParam String nome) {
        boolean exists = localidadeService.existsByName(nome);
        return ResponseEntity.ok(exists);
    }

    // Método para salvar uma nova localidade
    @PostMapping
    public ResponseEntity<Localidade> save(@RequestBody Localidade localidade) {
        Localidade savedLocalidade = localidadeService.save(localidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocalidade);
    }

    // Método para atualizar uma localidade existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocalidade(@PathVariable long id, @RequestBody Localidade localidadeAtualizada) {
        try {
            Localidade updated = localidadeService.alterarLocalidade(id, localidadeAtualizada);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar localidade.");
        }
    }

    // Método para deletar uma localidade
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocalidade(@PathVariable long id) {
        try {
            localidadeService.deleteById(id);
            return ResponseEntity.ok("Localidade deletada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        }
    }
}
