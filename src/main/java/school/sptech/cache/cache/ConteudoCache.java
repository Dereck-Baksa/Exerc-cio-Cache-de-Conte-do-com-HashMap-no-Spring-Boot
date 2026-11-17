package school.sptech.cache.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import school.sptech.cache.entity.Conteudo;

@Component
public class ConteudoCache {

    private static final Logger log = LoggerFactory.getLogger(ConteudoCache.class);

    private final MeuMapa<Long, EntradaCache> itens = new MeuMapa<>(16);

    @Value("${app.cache.max-itens}")
    private int maxItens;

    @Value("${app.cache.ttl-segundos}")
    private long ttlSegundos;

    private int hits;
    private int misses;
    private int evictions;

    public Conteudo buscar(Long id) {
        EntradaCache entrada = itens.get(id);

        if (entrada == null) {
            misses++;
            return null;
        }

        if (expirou(entrada)) {
            log.info("Cache EXPIRADO - id={}", id);
            itens.remove(id);
            misses++;
            return null;
        }

        entrada.setUltimoAcesso(System.currentTimeMillis());
        hits++;
        return entrada.getConteudo();
    }

    public void guardar(Long id, Conteudo conteudo) {
        if (itens.get(id) == null && itens.getTamanho() >= maxItens) {
            removerMenosUsado();
        }
        itens.put(id, new EntradaCache(conteudo));
    }

    public boolean contem(Long id) {
        EntradaCache entrada = itens.get(id);
        return entrada != null && !expirou(entrada);
    }

    public void invalidar(Long id) {
        itens.remove(id);
        log.info("Cache INVALIDADO - id={}", id);
    }

    public void limpar() {
        itens.limpar();
        log.info("Cache LIMPO");
    }

    public void logHit(Long id, long tempo) {
        log.info("Cache HIT  - id={} - {} ms", id, tempo);
    }

    public void logMiss(Long id, long tempo) {
        log.info("Cache MISS - id={} - {} ms", id, tempo);
    }

    public EstatisticasCache estatisticas() {
        int total = hits + misses;
        double hitRatio = total == 0 ? 0.0 : (double) hits / total * 100;
        return new EstatisticasCache(itens.getTamanho(), maxItens, hits, misses, evictions, hitRatio);
    }

    private boolean expirou(EntradaCache entrada) {
        return System.currentTimeMillis() - entrada.getArmazenadoEm() > ttlSegundos * 1000;
    }

    private void removerMenosUsado() {
        Long idRemover = null;
        long menorAcesso = Long.MAX_VALUE;

        for (Long id : itens.chaves()) {
            EntradaCache entrada = itens.get(id);
            if (entrada.getUltimoAcesso() < menorAcesso) {
                menorAcesso = entrada.getUltimoAcesso();
                idRemover = id;
            }
        }

        itens.remove(idRemover);
        evictions++;
        log.info("Cache CHEIO - removido id={} (LRU)", idRemover);
    }
}
