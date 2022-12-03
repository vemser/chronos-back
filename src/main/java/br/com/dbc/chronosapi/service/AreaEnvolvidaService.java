package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaCreateDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.AreaEnvolvidaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaEnvolvidaService {
    private final AreaEnvolvidaRepository areaEnvolvidaRepository;

    private final ObjectMapper objectMapper;

    public AreaEnvolvidaDTO create(AreaEnvolvidaCreateDTO areaEnvolvidaCreateDTO) {
        AreaEnvolvidaEntity areaEnvolvidaEntity = objectMapper.convertValue(areaEnvolvidaCreateDTO, AreaEnvolvidaEntity.class);
        areaEnvolvidaEntity.setNome(areaEnvolvidaCreateDTO.getNome());
        AreaEnvolvidaDTO areaEnvolvidaDTO = objectMapper.convertValue(areaEnvolvidaRepository.save(areaEnvolvidaEntity), AreaEnvolvidaDTO.class);
        areaEnvolvidaDTO.setNome(areaEnvolvidaCreateDTO.getNome().toUpperCase());
        return areaEnvolvidaDTO;
    }

    public void delete(Integer idAreaEnvolvida) throws RegraDeNegocioException {
        AreaEnvolvidaEntity areaEnvolvidaRecover = findById(idAreaEnvolvida);
        areaEnvolvidaRepository.delete(areaEnvolvidaRecover);
    }


    public AreaEnvolvidaEntity findById(Integer id) throws RegraDeNegocioException {
        return areaEnvolvidaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Área envolvida não encontrada!"));
    }

    public AreaEnvolvidaEntity findByNomeArea(String nome) {
        return areaEnvolvidaRepository.findByNomeArea(nome);
    }

    public List<AreaEnvolvidaDTO> listarAreas(){

        return areaEnvolvidaRepository.findAll().stream()
                .map(area -> objectMapper.convertValue(area, AreaEnvolvidaDTO.class))
                .toList();
    }
}
