package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.AreaEnvolvidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaEnvolvidaService {
    private final AreaEnvolvidaRepository areaEnvolvidaRepository;

    public AreaEnvolvidaEntity findById(Integer id) throws RegraDeNegocioException {
        return areaEnvolvidaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Responsavel não encontrado!"));
    }

    public AreaEnvolvidaEntity findByNome(String nome) {
        return areaEnvolvidaRepository.findByNome(nome);
    }
}
