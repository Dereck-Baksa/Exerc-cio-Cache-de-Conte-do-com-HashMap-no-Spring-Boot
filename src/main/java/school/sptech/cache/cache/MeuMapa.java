package school.sptech.cache.cache;

import java.util.ArrayList;
import java.util.List;

public class MeuMapa<K, V> {

    private Node<K, V>[] bucket;
    private int capacidade;
    private int tamanho;

    public MeuMapa(int capacidade) {
        this.capacidade = capacidade;
        this.bucket = new Node[capacidade];
    }

    public void put(K chave, V valor) {
        int hash = chave.hashCode();
        int indice = Math.floorMod(hash, capacidade);

        Node<K, V> atual = bucket[indice];

        if (atual == null) {
            bucket[indice] = new Node<>(hash, chave, valor);
            tamanho++;
            return;
        }

        while (atual != null) {

            if (atual.hash == hash && atual.key.equals(chave)) {
                atual.value = valor;
                return;
            }

            if (atual.next == null) {
                atual.next = new Node<>(hash, chave, valor);
                tamanho++;
                return;
            }

            atual = atual.next;
        }
    }

    public V get(K chave) {
        int hash = chave.hashCode();
        int indice = Math.floorMod(hash, capacidade);

        Node<K, V> atual = bucket[indice];

        while (atual != null) {

            if (atual.hash == hash && atual.key.equals(chave)) {
                return atual.value;
            }

            atual = atual.next;
        }

        return null;
    }

    public void remove(K chave) {
        int hash = chave.hashCode();
        int indice = Math.floorMod(hash, capacidade);

        Node<K, V> atual = bucket[indice];
        Node<K, V> anterior = null;

        while (atual != null) {

            if (atual.hash == hash && atual.key.equals(chave)) {
                if (anterior == null) {
                    bucket[indice] = atual.next;
                } else {
                    anterior.next = atual.next;
                }
                tamanho--;
                return;
            }

            anterior = atual;
            atual = atual.next;
        }
    }

    public List<K> chaves() {
        List<K> chaves = new ArrayList<>();

        for (int i = 0; i < capacidade; i++) {
            Node<K, V> atual = bucket[i];

            while (atual != null) {
                chaves.add(atual.key);
                atual = atual.next;
            }
        }

        return chaves;
    }

    public void limpar() {
        this.bucket = new Node[capacidade];
        this.tamanho = 0;
    }

    public int getTamanho() {
        return tamanho;
    }
}
