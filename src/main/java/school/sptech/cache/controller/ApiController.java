package school.sptech.cache.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiController {

    @GetMapping("/")
    public Map<String, String> inicio() {
        return Map.of(
                "mensagem", "API de cache de conteúdo",
                "conteudos", "POST /conteudos; GET, PUT e DELETE /conteudos/{id}",
                "estatisticas", "GET /cache/estatisticas",
                "limparCache", "DELETE /cache",
                "consoleH2", "/h2-console"
        );
    }
}
