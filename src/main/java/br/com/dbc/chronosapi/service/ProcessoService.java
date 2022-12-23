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

    public List<ProcessoDTO> listProcessosDaEtapa(Integer idEtapa) throws RegraDeNegocioException {

        EtapaEntity etapaEntity = etapaService.findById(idEtapa);
        return etapaEntity.getProcessos().stream()
                .map(processoEntity -> {
                    ProcessoDTO processoDTO = objectMapper.convertValue(processoEntity, ProcessoDTO.class);
                    processoDTO.setAreasEnvolvidas(getAreaEnvolvidaDTO(processoEntity.getAreasEnvolvidas()));
                    processoDTO.setResponsaveis(getResponsavelDTO(processoEntity.getResponsaveis()));
                    return processoDTO;
                }).toList();
    }

    public ProcessoDTO create(Integer idEtapa, ProcessoCreateDTO processoCreateDTO) throws RegraDeNegocioException {
        if (processoCreateDTO.getAreasEnvolvidas().isEmpty() || processoCreateDTO.getResponsaveis().isEmpty()) {
            throw new RegraDeNegocioException("Dados invalidos");
        }
        EtapaEntity etapaEntity = etapaService.findById(idEtapa);
        ProcessoEntity processoEntity = objectMapper.convertValue(processoCreateDTO, ProcessoEntity.class);
        processoEntity.setEtapa(etapaEntity);
        etapaService.save(etapaEntity);
        Set<AreaEnvolvidaEntity> areas = processoCreateDTO.getAreasEnvolvidas().stream()
                .map(area -> {
                    String nomeArea = area.getNome().trim().toUpperCase();
                    AreaEnvolvidaEntity areaEnvolvidaEntity = areaEnvolvidaService.findByNomeArea(nomeArea);
                    if (areaEnvolvidaEntity == null) {
                        String nomeAreaEntity = area.getNome().trim();
                        area.setNome(nomeAreaEntity);
                        return objectMapper.convertValue(areaEnvolvidaService.create(area), AreaEnvolvidaEntity.class);
                    }
                    return areaEnvolvidaEntity;
                }).collect(Collectors.toSet());
        processoEntity.setAreasEnvolvidas(areas);
        Set<ResponsavelEntity> responsaveis = processoCreateDTO.getResponsaveis().stream()
                .map(responsavel -> {
                    String nomeResponsavel = responsavel.getNome().trim().toUpperCase();
                    ResponsavelEntity responsavelEntity = responsavelService.findByNomeResponsavel(nomeResponsavel);
                    if (responsavelEntity == null) {
                        String nomeResponsavelEntity = responsavel.getNome().trim();
                        responsavel.setNome(nomeResponsavelEntity);
                        return objectMapper.convertValue(responsavelService.create(responsavel), ResponsavelEntity.class);
                    }
                    return responsavelEntity;
                }).collect(Collectors.toSet());
        processoEntity.setResponsaveis(responsaveis);
        ProcessoEntity processoSaved = processoRepository.save(processoEntity);
        ProcessoDTO processoDTO = objectMapper.convertValue(processoSaved, ProcessoDTO.class);
        processoDTO.setAreasEnvolvidas(getAreaEnvolvidaDTO(processoSaved.getAreasEnvolvidas()));
        processoDTO.setResponsaveis(getResponsavelDTO(processoSaved.getResponsaveis()));
        return processoDTO;
    }

    public ProcessoDTO update(Integer idProcesso, ProcessoCreateDTO processoUpdate) throws RegraDeNegocioException {
        if (processoUpdate.getAreasEnvolvidas().isEmpty() || processoUpdate.getResponsaveis().isEmpty()) {
            throw new RegraDeNegocioException("Dados invalidos");
        }
        ProcessoEntity processoRecover = findById(idProcesso);
        processoRecover.setNome(processoUpdate.getNome());
        processoRecover.setOrdemExecucao(processoUpdate.getOrdemExecucao());
        processoRecover.setDuracaoProcesso(processoUpdate.getDuracaoProcesso());
        processoRecover.setDiasUteis(processoUpdate.getDiasUteis());
        Set<AreaEnvolvidaEntity> areas = processoUpdate.getAreasEnvolvidas().stream()
                .map(area -> {
                    String nomeArea = area.getNome().trim().toUpperCase();
                    AreaEnvolvidaEntity areaEnvolvidaEntity = areaEnvolvidaService.findByNomeArea(nomeArea);
                    if (areaEnvolvidaEntity == null) {
                        String nomeAreaEntity = area.getNome().trim();
                        area.setNome(nomeAreaEntity);
                        return objectMapper.convertValue(areaEnvolvidaService.create(area), AreaEnvolvidaEntity.class);
                    }
                    return areaEnvolvidaEntity;
                }).collect(Collectors.toSet());
        processoRecover.setAreasEnvolvidas(areas);
        Set<ResponsavelEntity> responsaveis = processoUpdate.getResponsaveis().stream()
                .map(responsavel -> {
                    String nomeResponsavel = responsavel.getNome().trim().toUpperCase();
                    ResponsavelEntity responsavelEntity = responsavelService.findByNomeResponsavel(nomeResponsavel);
                    if (responsavelEntity == null) {
                        String nomeResponsavelEntity = responsavel.getNome().trim();
                        responsavel.setNome(nomeResponsavelEntity);
                        return objectMapper.convertValue(responsavelService.create(responsavel), ResponsavelEntity.class);
                    }
                    return responsavelEntity;
                }).collect(Collectors.toSet());
        processoRecover.setResponsaveis(responsaveis);
        ProcessoDTO processoDTO = objectMapper.convertValue(processoRepository.save(processoRecover), ProcessoDTO.class);
        processoDTO.setResponsaveis(getResponsavelDTO(responsaveis));
        processoDTO.setAreasEnvolvidas(getAreaEnvolvidaDTO(areas));
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

