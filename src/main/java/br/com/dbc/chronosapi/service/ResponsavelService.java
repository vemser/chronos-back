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
        ResponsavelDTO responsavelDTO = objectMapper.convertValue(responsavelRepository.save(responsavelEntity), ResponsavelDTO.class);
        responsavelDTO.setNome(responsavelCreateDTO.getNome());
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

    public ResponsavelEntity findByNome(String nome) {
        return responsavelRepository.findByNome(nome);
    }

    public ResponsavelEntity findByNomeContainingIgnoreCase(String nome) {
        return responsavelRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<ResponsavelDTO> listarResponsaveis(){

        return responsavelRepository.findAll().stream()
                .map(responsavel -> objectMapper.convertValue(responsavel, ResponsavelDTO.class))
                .toList();
    }
}
