package school.sptech.cache.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.cache.dto.ConteudoRequest;
import school.sptech.cache.dto.ConteudoResponse;
import school.sptech.cache.entity.Conteudo;
import school.sptech.cache.mapper.ConteudoMapper;
import school.sptech.cache.service.ConteudoService;

@RestController
@RequestMapping("/conteudos")
public class ConteudoController {

    private final ConteudoService conteudoService;

    public ConteudoController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConteudoResponse> buscarPorId(@PathVariable Long id) {
        boolean hit = conteudoService.estaNoCache(id);
        Conteudo conteudo = conteudoService.buscarPorId(id);
        return ResponseEntity.ok()
                .header("X-Cache", hit ? "HIT" : "MISS")
                .body(ConteudoMapper.toResponse(conteudo));
    }

    @PostMapping
    public ResponseEntity<ConteudoResponse> criar(@RequestBody @Valid ConteudoRequest request) {
        Conteudo conteudo = ConteudoMapper.toEntity(request);
        Conteudo criado = conteudoService.criar(conteudo);
        return ResponseEntity.status(201).body(ConteudoMapper.toResponse(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConteudoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ConteudoRequest request) {
        Conteudo conteudo = ConteudoMapper.toEntity(request);
        Conteudo atualizado = conteudoService.atualizar(id, conteudo);
        return ResponseEntity.ok(ConteudoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        conteudoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
