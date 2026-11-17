package school.sptech.cache.cache;

public class EstatisticasCache {

    private int tamanho;
    private int limite;
    private int hits;
    private int misses;
    private int evictions;
    private double hitRatio;

    public EstatisticasCache(int tamanho, int limite, int hits, int misses, int evictions, double hitRatio) {
        this.tamanho = tamanho;
        this.limite = limite;
        this.hits = hits;
        this.misses = misses;
        this.evictions = evictions;
        this.hitRatio = hitRatio;
    }

    public int getTamanho() {
        return tamanho;
    }

    public int getLimite() {
        return limite;
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public int getEvictions() {
        return evictions;
    }

    public double getHitRatio() {
        return hitRatio;
    }
}
