package school.sptech.cache.mapper;

import school.sptech.cache.dto.ConteudoRequest;
import school.sptech.cache.dto.ConteudoResponse;
import school.sptech.cache.entity.Conteudo;

public class ConteudoMapper {

    public static ConteudoResponse toResponse(Conteudo conteudo) {
        ConteudoResponse response = new ConteudoResponse();
        response.setId(conteudo.getId());
        response.setTitulo(conteudo.getTitulo());
        response.setCorpo(conteudo.getCorpo());
        response.setAutor(conteudo.getAutor());
        response.setAtualizadoEm(conteudo.getAtualizadoEm());
        return response;
    }

    public static Conteudo toEntity(ConteudoRequest request) {
        Conteudo conteudo = new Conteudo();
        conteudo.setTitulo(request.getTitulo());
        conteudo.setCorpo(request.getCorpo());
        conteudo.setAutor(request.getAutor());
        return conteudo;
    }
}
