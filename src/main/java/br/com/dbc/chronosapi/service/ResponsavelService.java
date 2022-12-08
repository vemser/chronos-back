package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.processo.ResponsavelCreateDTO;
import br.com.dbc.chronosapi.dto.processo.ResponsavelDTO;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.ResponsavelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponsavelService {
    private final ResponsavelRepository responsavelRepository;
    private final ObjectMapper objectMapper;

    public ResponsavelDTO create(ResponsavelCreateDTO responsavelCreateDTO) {
        ResponsavelEntity responsavelEntity = objectMapper.convertValue(responsavelCreateDTO, ResponsavelEntity.class);
        ResponsavelDTO responsavelDTO = convertResponsavelToDTO(responsavelRepository.save(responsavelEntity));
        responsavelDTO.setNome(responsavelCreateDTO.getNome().toUpperCase());
        return responsavelDTO;
    }

    public void delete(Integer idResponsavel) throws RegraDeNegocioException {
        ResponsavelEntity responsavelRecover = findById(idResponsavel);
        responsavelRepository.delete(responsavelRecover);
    }

    public ResponsavelEntity findById(Integer id) throws RegraDeNegocioException {
        return responsavelRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Responsavel não encontrado!"));
    }

    public ResponsavelEntity findByNomeResponsavel(String nome) {
        return responsavelRepository.findByNomeResponsavel(nome);
    }
    public List<ResponsavelDTO> listarResponsaveis(){

        return responsavelRepository.findAll().stream()
                .map(this::convertResponsavelToDTO)
                .toList();
    }

    public ResponsavelDTO convertResponsavelToDTO(ResponsavelEntity responsavelEntity) {
        return objectMapper.convertValue(responsavelEntity, ResponsavelDTO.class);
    }
}
