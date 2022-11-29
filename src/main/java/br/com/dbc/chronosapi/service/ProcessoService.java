package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.dto.processo.ResponsavelDTO;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.ProcessoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessoService {
    private final ProcessoRepository processoRepository;
    private final ObjectMapper objectMapper;
    private final ResponsavelService responsavelService;
    private final AreaEnvolvidaService areaEnvolvidaService;

    private final EtapaService etapaService;

    public PageDTO<ProcessoDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<ProcessoEntity> paginaDoRepositorio = processoRepository.findAll(pageRequest);
        List<ProcessoDTO> processoDTOList = paginaDoRepositorio.getContent().stream()
                .map(processo -> objectMapper.convertValue(processo, ProcessoDTO.class))
                .toList();
        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                processoDTOList);
    }

    public ProcessoDTO create(Integer idEtapa, ProcessoCreateDTO processoCreateDTO) throws RegraDeNegocioException {
        EtapaEntity etapaEntity = etapaService.findById(idEtapa);
        ProcessoEntity processoEntity = objectMapper.convertValue(processoCreateDTO, ProcessoEntity.class);
        processoEntity.setEtapa(etapaEntity);
        ProcessoEntity processoSaved = processoRepository.save(processoEntity);
        etapaEntity.getProcessos().add(processoSaved);
        etapaService.save(etapaEntity);
        return objectMapper.convertValue(processoEntity, ProcessoDTO.class);
    }

    public ProcessoDTO update(Integer idProcesso, ProcessoCreateDTO processoUpdate) throws RegraDeNegocioException {
        ProcessoEntity processoRecover = findById(idProcesso);
        processoRecover.setOrdemExecucao(processoUpdate.getOrdemExecucao());
        processoRecover.setDuracaoProcesso(processoUpdate.getDuracaoProcesso());
        processoRecover.setDiasUteis(processoUpdate.getDiasUteis());
        Set<ResponsavelEntity> responsaveisEntities = processoUpdate.getResponsavel().stream()
                        .map(responsavel -> (responsavelService.findByNome(responsavel))).collect(Collectors.toSet());
        processoRecover.setResponsaveis(new HashSet<>(responsaveisEntities));
        Set<AreaEnvolvidaEntity> areaEnvolvidasEntities = processoUpdate.getAreaEnvolvida().stream()
                .map(areasEnvolvidas -> (areaEnvolvidaService.findByNome(areasEnvolvidas))).collect(Collectors.toSet());
        processoRecover.setAreasEnvolvidas(new HashSet<>(areaEnvolvidasEntities));
        ProcessoDTO processoDTO = objectMapper.convertValue(processoRepository.save(processoRecover), ProcessoDTO.class);
        processoDTO.setResponsavel(getResponsavelDTO(responsaveisEntities));
        processoDTO.setAreaEnvolvida(getAreaEnvolvidaDTO(areaEnvolvidasEntities));
        return processoDTO;
    }

    public void delete(Integer isProcesso) throws RegraDeNegocioException {
        ProcessoEntity processoRecover = findById(isProcesso);
        processoRepository.delete(processoRecover);
    }

    public ProcessoEntity findById(Integer idProcesso) throws RegraDeNegocioException {
        return processoRepository.findById(idProcesso)
                .orElseThrow(() -> new RegraDeNegocioException("Processo não encontrado!"));
    }

    public Set<ResponsavelDTO> getResponsavelDTO(Set<ResponsavelEntity> responsaveis) {
        return responsaveis.stream()
                .map(responsavelEntity -> objectMapper.convertValue(responsavelEntity, ResponsavelDTO.class))
                .collect(Collectors.toSet());
    }
    public Set<AreaEnvolvidaDTO> getAreaEnvolvidaDTO(Set<AreaEnvolvidaEntity> AreasEnvolvidas) {
        return AreasEnvolvidas.stream()
                .map(areaEnvolvidaEntity -> objectMapper.convertValue(areaEnvolvidaEntity, AreaEnvolvidaDTO.class))
                .collect(Collectors.toSet());
    }

}

