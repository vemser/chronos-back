package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.ResponsavelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponsavelService {
    private final ResponsavelRepository responsavelRepository;

    public ResponsavelEntity findById(Integer id) throws RegraDeNegocioException {
        return responsavelRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Responsavel não encontrado!"));
    }

    public ResponsavelEntity findByNome(String nome) {
        return responsavelRepository.findByNome(nome);
    }
}
