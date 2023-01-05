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
            if(diaNaoUtilEntity.getDataInicial().getYear() != LocalDate.now().getYear()) {
                diaNaoUtilEntity.setDataInicial(LocalDate.of(LocalDate.now().getYear(),
                        diaNaoUtilEntity.getDataInicial().getMonthValue(),
                        diaNaoUtilEntity.getDataInicial().getDayOfMonth()));
            }
        }
    }

    public void delete(Integer idDiaNaoUtil) throws RegraDeNegocioException {
        DiaNaoUtilEntity diaNaoUtil = findById(idDiaNaoUtil);

        if(diaNaoUtil.getRepeticaoAnual() == Status.ATIVO) {
            List<DiaNaoUtilEntity> byDescricao = diaNaoUtilRepository.findByDescricao(diaNaoUtil.getDescricao());
            diaNaoUtilRepository.deleteAll(byDescricao);
        }else {
            diaNaoUtilRepository.delete(diaNaoUtil);
        }
    }

    public PageDTO<DiaNaoUtilDTO> list(Integer pagina, Integer tamanho) {
        Sort orderBy = Sort.by("dataInicial");
        int ano = LocalDate.now().getYear();
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, orderBy);
        Page<DiaNaoUtilEntity> paginaDoRepositorio = diaNaoUtilRepository.findAllByAno(pageRequest, ano);
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
        Page<DiaNaoUtilEntity> diaNaoUtilEntitiesPage;

        if(dataInicial == null && dataFinal == null && descricao == null) {
            return this.list(pagina, tamanho);
        }else if(dataInicial == null && dataFinal == null && descricao != null) {
            int ano = LocalDate.now().getYear();
            diaNaoUtilEntitiesPage = diaNaoUtilRepository.findByDescricaoAndAno(pageRequest, descricao, ano);
        }else if(dataInicial != null && dataFinal != null && descricao == null){
            verificarDiasComRepeticaoAnual(dataInicial, dataFinal);
            diaNaoUtilEntitiesPage = diaNaoUtilRepository.findAllByFiltro(pageRequest, descricao, dataFinal, dataInicial);
        }else if(dataInicial != null && dataFinal != null && descricao != null){
            verificarDiasComRepeticaoAnual(dataInicial, dataFinal);
            diaNaoUtilEntitiesPage = diaNaoUtilRepository.findAllByFiltro(pageRequest, descricao, dataFinal, dataInicial);
        }else {
            int ano = LocalDate.now().getYear();
            diaNaoUtilEntitiesPage = diaNaoUtilRepository.findByDescricaoAndAno(pageRequest, descricao, ano);
        }

        List<DiaNaoUtilDTO> diaNaoUtilDTOList = diaNaoUtilEntitiesPage.stream()
                .map(dia -> objectMapper.convertValue(dia, DiaNaoUtilDTO.class)).toList();

        return new PageDTO<>(diaNaoUtilEntitiesPage.getTotalElements(),
                diaNaoUtilEntitiesPage.getTotalPages(),
                pagina,
                tamanho,
                diaNaoUtilDTOList);
    }

    private void verificarDiasComRepeticaoAnual(LocalDate dataInicial, LocalDate dataFinal) {
        List<DiaNaoUtilEntity> diasComRepeticaoAnual = diaNaoUtilRepository.findByRepeticaoAnual(Status.ATIVO);
        LocalDate dataCompare = dataInicial;

        while (dataCompare.isBefore(dataFinal)) {
            for (DiaNaoUtilEntity dia : diasComRepeticaoAnual) {
                if (dataCompare.getDayOfMonth() == dia.getDataInicial().getDayOfMonth() && dataCompare.getMonthValue() == dia.getDataInicial().getMonthValue()) {
                    DiaNaoUtilEntity byDataInicial = diaNaoUtilRepository.findByDataInicial(dataCompare);
                    if (byDataInicial == null) {
                        DiaNaoUtilEntity novoDiaComRepeticaoAnual = new DiaNaoUtilEntity();
                        novoDiaComRepeticaoAnual.setDataInicial(LocalDate.of(dataCompare.getYear(), dia.getDataInicial().getMonthValue(), dia.getDataInicial().getDayOfMonth()));
                        novoDiaComRepeticaoAnual.setDataFinal(null);
                        novoDiaComRepeticaoAnual.setRepeticaoAnual(Status.ATIVO);
                        novoDiaComRepeticaoAnual.setDescricao(dia.getDescricao());
                        diaNaoUtilRepository.save(novoDiaComRepeticaoAnual);
                    }
                }
            }
            dataCompare = dataCompare.plusDays(1);
        }
    }
}
