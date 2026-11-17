package school.sptech.cache.cache;

import school.sptech.cache.entity.Conteudo;

public class EntradaCache {

    private Conteudo conteudo;
    private long armazenadoEm;
    private long ultimoAcesso;

    public EntradaCache(Conteudo conteudo) {
        this.conteudo = conteudo;
        this.armazenadoEm = System.currentTimeMillis();
        this.ultimoAcesso = armazenadoEm;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public long getArmazenadoEm() {
        return armazenadoEm;
    }

    public long getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(long ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }
}
