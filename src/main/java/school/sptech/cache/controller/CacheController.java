package school.sptech.cache.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.cache.cache.EstatisticasCache;
import school.sptech.cache.service.ConteudoService;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final ConteudoService conteudoService;

    public CacheController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasCache> estatisticas() {
        return ResponseEntity.ok(conteudoService.estatisticasCache());
    }

    @DeleteMapping
    public ResponseEntity<Void> limpar() {
        conteudoService.limparCache();
        return ResponseEntity.noContent().build();
    }
}
