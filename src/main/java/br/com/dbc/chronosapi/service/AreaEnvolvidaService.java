package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaCreateDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.AreaEnvolvidaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaEnvolvidaService {
    private final AreaEnvolvidaRepository areaEnvolvidaRepository;

    private final ObjectMapper objectMapper;

    public AreaEnvolvidaDTO create(AreaEnvolvidaCreateDTO areaEnvolvidaCreateDTO) {
        AreaEnvolvidaEntity areaEnvolvidaEntity = objectMapper.convertValue(areaEnvolvidaCreateDTO, AreaEnvolvidaEntity.class);
        AreaEnvolvidaDTO areaEnvolvidaDTO = objectMapper.convertValue(areaEnvolvidaRepository.save(areaEnvolvidaEntity), AreaEnvolvidaDTO.class);
        areaEnvolvidaDTO.setNome(areaEnvolvidaCreateDTO.getNome());
        return areaEnvolvidaDTO;
    }

    public void delete(Integer idAreaEnvolvida) throws RegraDeNegocioException {
        AreaEnvolvidaEntity areaEnvolvidaRecover = findById(idAreaEnvolvida);
        areaEnvolvidaRepository.delete(areaEnvolvidaRecover);
    }


    public AreaEnvolvidaEntity findById(Integer id) throws RegraDeNegocioException {
        return areaEnvolvidaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Responsavel não encontrado!"));
    }

    public AreaEnvolvidaEntity findByNome(String nome) {
        return areaEnvolvidaRepository.findByNome(nome);
    }
}
