package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.DiaNaoUtilCreateDTO;
import br.com.dbc.chronosapi.dto.DiaNaoUtilDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.entity.classes.DiaNaoUtilEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.DiaNaoUtilRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaNaoUtilService {

    private final ObjectMapper objectMapper;
    private final DiaNaoUtilRepository diaNaoUtilRepository;


    public DiaNaoUtilDTO create(DiaNaoUtilCreateDTO diaNaoUtilCreateDTO, Status repeticaoAnual) {
        DiaNaoUtilEntity diaNaoUtilEntity = objectMapper.convertValue(diaNaoUtilCreateDTO, DiaNaoUtilEntity.class);
        diaNaoUtilEntity.setDescricao(diaNaoUtilCreateDTO.getDescricao());
        if(Status.INATIVO == repeticaoAnual) {
            diaNaoUtilEntity.setRepeticaoAnual(Status.INATIVO);
            diaNaoUtilEntity.setDataFinal(diaNaoUtilCreateDTO.getDataFinal());
        } else {
            diaNaoUtilEntity.setRepeticaoAnual(Status.ATIVO);
            diaNaoUtilEntity.setDataFinal(null);
        }
        DiaNaoUtilEntity diaSaved = diaNaoUtilRepository.save(diaNaoUtilEntity);
        return objectMapper.convertValue(diaSaved, DiaNaoUtilDTO.class);

    }

    public DiaNaoUtilDTO update(Integer idDiaNaoUtil, DiaNaoUtilCreateDTO diaNaoUtilUpdate, Status repeticaoAnual) throws RegraDeNegocioException {
        DiaNaoUtilEntity diaNaoUtilRecover = findById(idDiaNaoUtil);
        diaNaoUtilRecover.setDescricao(diaNaoUtilUpdate.getDescricao());
        diaNaoUtilRecover.setDataInicial(diaNaoUtilUpdate.getDataInicial());
        diaNaoUtilRecover.setDataFinal(diaNaoUtilUpdate.getDataFinal());

        if(Status.INATIVO == repeticaoAnual) {
            diaNaoUtilRecover.setRepeticaoAnual(Status.INATIVO);
        } else {
            diaNaoUtilRecover.setRepeticaoAnual(Status.ATIVO);
            diaNaoUtilRecover.setDataFinal(null);
        }
        DiaNaoUtilDTO diaNaoUtilDTO = objectMapper.convertValue(diaNaoUtilRepository.save(diaNaoUtilRecover), DiaNaoUtilDTO.class);
        return diaNaoUtilDTO;
    }

    public void delete(Integer idDiaNaoUtil) throws RegraDeNegocioException {
        DiaNaoUtilEntity diaNaoUtil = findById(idDiaNaoUtil);
        diaNaoUtilRepository.delete(diaNaoUtil);
    }

    public PageDTO<DiaNaoUtilDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<DiaNaoUtilEntity> paginaDoRepositorio = diaNaoUtilRepository.findAll(pageRequest);
        List<DiaNaoUtilDTO> diaNaoUtilDTOList = paginaDoRepositorio.getContent().stream()
                .map(diaNaoUtil -> objectMapper.convertValue(diaNaoUtil, DiaNaoUtilDTO.class))
                .toList();
        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                diaNaoUtilDTOList);
    }

    public DiaNaoUtilEntity findById(Integer id) throws RegraDeNegocioException {
        DiaNaoUtilEntity diaNaoUtilEntity = diaNaoUtilRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Dia não util não encontrado"));
        return diaNaoUtilEntity;
    }

}