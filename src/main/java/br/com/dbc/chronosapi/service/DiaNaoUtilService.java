package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.diaNaoUtil.DiaNaoUtilCreateDTO;
import br.com.dbc.chronosapi.dto.diaNaoUtil.DiaNaoUtilDTO;
import br.com.dbc.chronosapi.entity.classes.DiaNaoUtilEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.DiaNaoUtilRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaNaoUtilService {

    private final ObjectMapper objectMapper;
    private final DiaNaoUtilRepository diaNaoUtilRepository;

    public DiaNaoUtilDTO create(DiaNaoUtilCreateDTO diaNaoUtilCreateDTO) throws RegraDeNegocioException {
        DiaNaoUtilEntity diaNaoUtilEntity = objectMapper.convertValue(diaNaoUtilCreateDTO, DiaNaoUtilEntity.class);
        diaNaoUtilEntity.setDescricao(diaNaoUtilCreateDTO.getDescricao());
        verify(diaNaoUtilCreateDTO, diaNaoUtilEntity);
        DiaNaoUtilEntity diaSaved = diaNaoUtilRepository.save(diaNaoUtilEntity);
        return objectMapper.convertValue(diaSaved, DiaNaoUtilDTO.class);

    }

    public DiaNaoUtilDTO update(Integer idDiaNaoUtil, DiaNaoUtilCreateDTO diaNaoUtilUpdate) throws
            RegraDeNegocioException {
        DiaNaoUtilEntity diaNaoUtilRecover = findById(idDiaNaoUtil);
        diaNaoUtilRecover.setDescricao(diaNaoUtilUpdate.getDescricao());
        diaNaoUtilRecover.setDataInicial(diaNaoUtilUpdate.getDataInicial());
        diaNaoUtilRecover.setDataFinal(diaNaoUtilUpdate.getDataFinal());

        verify(diaNaoUtilUpdate, diaNaoUtilRecover);
        return objectMapper.convertValue(diaNaoUtilRepository.save(diaNaoUtilRecover), DiaNaoUtilDTO.class);

    }

    private void verify(DiaNaoUtilCreateDTO diaNaoUtilCreateDTO, DiaNaoUtilEntity diaNaoUtilEntity) throws RegraDeNegocioException {
        if (diaNaoUtilCreateDTO.getRepeticaoAnual() == Status.INATIVO) {
            diaNaoUtilEntity.setRepeticaoAnual(Status.INATIVO);

            if (diaNaoUtilCreateDTO.getDataFinal().isAfter(diaNaoUtilCreateDTO.getDataInicial())) {
                diaNaoUtilEntity.setDataFinal(diaNaoUtilCreateDTO.getDataFinal());
            } else if (diaNaoUtilCreateDTO.getDataFinal().isBefore(diaNaoUtilCreateDTO.getDataInicial())) {
                throw new RegraDeNegocioException("A data final antecede a data inicial.");
            }

        } else {
            diaNaoUtilEntity.setRepeticaoAnual(Status.ATIVO);
            diaNaoUtilEntity.setDataFinal(null);
        }
    }

    public void delete(Integer idDiaNaoUtil) throws RegraDeNegocioException {
        DiaNaoUtilEntity diaNaoUtil = findById(idDiaNaoUtil);
        diaNaoUtilRepository.delete(diaNaoUtil);
    }

    public PageDTO<DiaNaoUtilDTO> list(Integer pagina, Integer tamanho) {
        Sort orderBy = Sort.by("dataInicial");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, orderBy);
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
        return diaNaoUtilRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Dia não util não encontrado"));
    }

    public PageDTO<DiaNaoUtilDTO> filtrar(Integer pagina, Integer tamanho, LocalDate dataInicial, LocalDate dataFinal, String descricao) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);

        Page<DiaNaoUtilEntity> diaNaoUtilEntitiesPage = diaNaoUtilRepository
                .findAllByFiltro(pageRequest, descricao, dataFinal, dataInicial);

        List<DiaNaoUtilDTO> diaNaoUtilDTOList = diaNaoUtilEntitiesPage.stream()
                .map(dia -> objectMapper.convertValue(dia, DiaNaoUtilDTO.class))
                .toList();

        return new PageDTO<>(diaNaoUtilEntitiesPage.getTotalElements(),
                diaNaoUtilEntitiesPage.getTotalPages(),
                pagina,
                tamanho,
                diaNaoUtilDTOList);
    }
}
