package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaCreateDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.dto.processo.ResponsavelDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.EtapaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EtapaService {

    private final ObjectMapper objectMapper;
    private final EtapaRepository etapaRepository;
    private final EdicaoService edicaoService;

    public PageDTO<EtapaDTO> list(Integer pagina, Integer tamanho) {
        Sort ordenacao = Sort.by("ordemExecucao", "nome");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<EtapaEntity> paginaDoRepositorio = etapaRepository.findAll(pageRequest);
        List<EtapaDTO> etapasDaPagina = paginaDoRepositorio.getContent().stream()
                .map(etapaEntity -> {
                    EtapaDTO etapaDTO = convertEtapaToDTO(etapaEntity);
                    etapaDTO.setProcessos(etapaEntity.getProcessos().stream()
                            .map(processoEntity -> {
                                ProcessoDTO processoDTO = objectMapper.convertValue(processoEntity, ProcessoDTO.class);
                                processoDTO.setAreasEnvolvidas(this.getAreaEnvolvidaDTO(processoEntity.getAreasEnvolvidas()));
                                processoDTO.setResponsaveis(this.getResponsavelDTO(processoEntity.getResponsaveis()));
                                return processoDTO;
                            }).collect(Collectors.toList()));
                    return etapaDTO;
                }).toList();
        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                etapasDaPagina
        );
    }

    public List<EtapaDTO> listEtapasDaEdicao(Integer idEdicao) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = edicaoService.findById(idEdicao);
        return edicaoEntity.getEtapas().stream()
                .map(etapaEntity -> {
                    EtapaDTO etapaDTO = convertEtapaToDTO(etapaEntity);
                    etapaDTO.setProcessos(etapaEntity.getProcessos().stream()
                            .map(processoEntity -> {
                                ProcessoDTO processoDTO = objectMapper.convertValue(processoEntity, ProcessoDTO.class);
                                processoDTO.setAreasEnvolvidas(this.getAreaEnvolvidaDTO(processoEntity.getAreasEnvolvidas()));
                                processoDTO.setResponsaveis(this.getResponsavelDTO(processoEntity.getResponsaveis()));
                                return processoDTO;
                            }).collect(Collectors.toList()));
                    return etapaDTO;
                }).collect(Collectors.toList());
    }

    public EtapaDTO create(Integer idEdicao, EtapaCreateDTO etapaCreateDTO) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = edicaoService.findById(idEdicao);
        EtapaEntity etapaEntity = objectMapper.convertValue(etapaCreateDTO, EtapaEntity.class);
        etapaEntity.setEdicao(edicaoEntity);
        EtapaEntity etapaSaved = etapaRepository.save(etapaEntity);
        edicaoEntity.getEtapas().add(etapaSaved);
        edicaoService.save(edicaoEntity);
        return convertEtapaToDTO(etapaSaved);
    }

    public EtapaDTO update(Integer idEtapa, EtapaCreateDTO etapaUpdate) throws RegraDeNegocioException {
        EtapaEntity etapaRecover = findById(idEtapa);
        etapaRecover.setNome(etapaUpdate.getNome());
        etapaRecover.setOrdemExecucao(etapaUpdate.getOrdemExecucao());
        return convertEtapaToDTO(etapaRepository.save(etapaRecover));
    }

    public void delete(Integer idEtapa) throws RegraDeNegocioException {
        EtapaEntity etapaRecover = findById(idEtapa);
        etapaRepository.delete(etapaRecover);
    }

    public EtapaEntity findById(Integer id) throws RegraDeNegocioException {
        return etapaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Etapa não encontrada"));
    }

    public EtapaDTO save(EtapaEntity etapaEntity) {
        etapaRepository.save(etapaEntity);
        return convertEtapaToDTO(etapaEntity);
    }

    public List<ProcessoDTO> getProcessosDTO(Set<ProcessoEntity> processos) {
        return processos.stream()
                .map(processoEntity -> objectMapper.convertValue(processoEntity, ProcessoDTO.class))
                .collect(Collectors.toList());
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

    public EtapaDTO convertEtapaToDTO(EtapaEntity etapaEntity) {
        return objectMapper.convertValue(etapaEntity, EtapaDTO.class);
    }
}
