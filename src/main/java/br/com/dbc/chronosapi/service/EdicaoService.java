package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.EdicaoDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.EdicaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EdicaoService {

    private final EdicaoRepository edicaoRepository;
    private final ObjectMapper objectMapper;


    public EdicaoDTO create(EdicaoCreateDTO edicaoCreateDTO) {
        EdicaoEntity edicaoEntity = objectMapper.convertValue(edicaoCreateDTO, EdicaoEntity.class);
        EdicaoEntity edicaoSaved = edicaoRepository.save(edicaoEntity);
        return objectMapper.convertValue(edicaoSaved, EdicaoDTO.class);
    }

    public EdicaoDTO update(Integer idEdicao, EdicaoCreateDTO edicaoUpdate) throws RegraDeNegocioException {
        EdicaoEntity edicaoRecover = findById(idEdicao);
        edicaoRecover.setNome(edicaoUpdate.getNome());
        edicaoRecover.setDataInicial(edicaoUpdate.getDataInicial());
        edicaoRecover.setDataFinal(edicaoUpdate.getDataFinal());
        edicaoRepository.save(edicaoRecover);
        return objectMapper.convertValue(edicaoRecover, EdicaoDTO.class);
    }

    public void delete(Integer idEdicao) throws RegraDeNegocioException {
        EdicaoEntity edicaoRecover = findById(idEdicao);
        edicaoRepository.delete(edicaoRecover);
    }

    public List<EdicaoDTO> list() {
        return edicaoRepository.findAll().stream()
                .map(edicaoEntity -> objectMapper.convertValue(edicaoEntity, EdicaoDTO.class))
                .toList();
    }

    public EdicaoEntity findById(Integer id) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = edicaoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Edição não encontrada"));
        return edicaoEntity;
    }



}
