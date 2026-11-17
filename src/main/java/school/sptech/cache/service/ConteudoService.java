package school.sptech.cache.service;

import org.springframework.stereotype.Service;
import school.sptech.cache.cache.ConteudoCache;
import school.sptech.cache.cache.EstatisticasCache;
import school.sptech.cache.entity.Conteudo;
import school.sptech.cache.exception.ConteudoNaoEncontradoException;
import school.sptech.cache.repository.ConteudoRepository;

import java.time.LocalDateTime;

@Service
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;
    private final ConteudoCache conteudoCache;

    public ConteudoService(ConteudoRepository conteudoRepository, ConteudoCache conteudoCache) {
        this.conteudoRepository = conteudoRepository;
        this.conteudoCache = conteudoCache;
    }

    public boolean estaNoCache(Long id) {
        return conteudoCache.contem(id);
    }

    public Conteudo buscarPorId(Long id) {
        long inicio = System.currentTimeMillis();

        Conteudo conteudo = conteudoCache.buscar(id);

        if (conteudo != null) {
            conteudoCache.logHit(id, System.currentTimeMillis() - inicio);
            return conteudo;
        }

        conteudo = buscarNaFonte(id);
        conteudoCache.guardar(id, conteudo);
        conteudoCache.logMiss(id, System.currentTimeMillis() - inicio);
        return conteudo;
    }

    public Conteudo criar(Conteudo conteudo) {
        conteudo.setAtualizadoEm(LocalDateTime.now());
        return conteudoRepository.save(conteudo);
    }

    public Conteudo atualizar(Long id, Conteudo conteudo) {
        if (!conteudoRepository.existsById(id)) {
            throw new ConteudoNaoEncontradoException(id);
        }
        conteudo.setId(id);
        conteudo.setAtualizadoEm(LocalDateTime.now());
        Conteudo atualizado = conteudoRepository.save(conteudo);
        conteudoCache.invalidar(id);
        return atualizado;
    }

    public void deletar(Long id) {
        if (!conteudoRepository.existsById(id)) {
            throw new ConteudoNaoEncontradoException(id);
        }
        conteudoRepository.deleteById(id);
        conteudoCache.invalidar(id);
    }

    public EstatisticasCache estatisticasCache() {
        return conteudoCache.estatisticas();
    }

    public void limparCache() {
        conteudoCache.limpar();
    }

    // Simula uma fonte lenta
    private Conteudo buscarNaFonte(Long id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return conteudoRepository.findById(id)
                .orElseThrow(() -> new ConteudoNaoEncontradoException(id));
    }
}
