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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        Sort ordeancao = Sort.by("ordemExecucao", "nome");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordeancao);
        Page<ProcessoEntity> paginaDoRepositorio = processoRepository.findAll(pageRequest);
        List<ProcessoDTO> processoDTOList = paginaDoRepositorio.getContent().stream()
                .map(processo -> {
                    ProcessoDTO processoDTO = objectMapper.convertValue(processo, ProcessoDTO.class);
                    processoDTO.setAreasEnvolvidas(getAreaEnvolvidaDTO(processo.getAreasEnvolvidas()));
                    processoDTO.setResponsaveis(getResponsavelDTO(processo.getResponsaveis()));
                    return processoDTO;
                }).toList();
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
        etapaService.save(etapaEntity);
        Set<AreaEnvolvidaEntity> areas = processoCreateDTO.getAreasEnvolvidas().stream()
                .map(area -> areaEnvolvidaService.findByNomeContainingIgnoreCase(area))
                .collect(Collectors.toSet());
        processoEntity.setAreasEnvolvidas(areas);
        Set<ResponsavelEntity> responsaveis = processoCreateDTO.getAreasEnvolvidas().stream()
                .map(responsavel -> responsavelService.findByNomeContainingIgnoreCase(responsavel))
                .collect(Collectors.toSet());
        processoEntity.setResponsaveis(responsaveis);
        ProcessoEntity processoSaved = processoRepository.save(processoEntity);
        ProcessoDTO processoDTO = objectMapper.convertValue(processoSaved, ProcessoDTO.class);
        processoDTO.setAreasEnvolvidas(getAreaEnvolvidaDTO(processoSaved.getAreasEnvolvidas()));
        processoDTO.setResponsaveis(getResponsavelDTO(processoSaved.getResponsaveis()));
        return processoDTO;
    }

    public ProcessoDTO update(Integer idProcesso, ProcessoCreateDTO processoUpdate) throws RegraDeNegocioException {
        ProcessoEntity processoRecover = findById(idProcesso);
        processoRecover.setNome(processoUpdate.getNome());
        processoRecover.setOrdemExecucao(processoUpdate.getOrdemExecucao());
        processoRecover.setDuracaoProcesso(processoUpdate.getDuracaoProcesso());
        processoRecover.setDiasUteis(processoUpdate.getDiasUteis());
        Set<AreaEnvolvidaEntity> areaEnvolvidasEntities = processoUpdate.getAreasEnvolvidas().stream()
                .map(area -> areaEnvolvidaService.findByNomeContainingIgnoreCase(area))
                .collect(Collectors.toSet());
        processoRecover.setAreasEnvolvidas(areaEnvolvidasEntities);
        Set<ResponsavelEntity> responsaveisEntities = processoUpdate.getAreasEnvolvidas().stream()
                .map(responsavel -> responsavelService.findByNomeContainingIgnoreCase(responsavel))
                .collect(Collectors.toSet());
        processoRecover.setResponsaveis(responsaveisEntities);
        ProcessoDTO processoDTO = objectMapper.convertValue(processoRepository.save(processoRecover), ProcessoDTO.class);
        processoDTO.setResponsaveis(getResponsavelDTO(responsaveisEntities));
        processoDTO.setAreasEnvolvidas(getAreaEnvolvidaDTO(areaEnvolvidasEntities));
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

