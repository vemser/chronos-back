package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaCreateDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.EtapaRepository;
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

@RequiredArgsConstructor
@Service
public class EtapaService {

    private final ObjectMapper objectMapper;
    private final EtapaRepository etapaRepository;
    private final EdicaoService edicaoService;
    private final ProcessoRepository processoRepository;

    public PageDTO<EtapaDTO> list(Integer pagina, Integer tamanho) {
        Sort ordenacao = Sort.by("ordemExecucao", "nome");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<EtapaEntity> paginaDoRepositorio = etapaRepository.findAll(pageRequest);
        List<EtapaDTO> etapasDaPagina = paginaDoRepositorio.getContent().stream()
                .map(etapaEntity -> {
                    EtapaDTO etapaDTO = objectMapper.convertValue(etapaEntity, EtapaDTO.class);
                    etapaDTO.setProcessos(etapaEntity.getProcessos().stream()
                            .map(processoEntity -> {
                                processoRepository.findAll(Sort.by("ordemExecucao").ascending().and(Sort.by("nome")).ascending());
                                return objectMapper.convertValue(processoEntity, ProcessoDTO.class);
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

    public EtapaDTO create(Integer idEdicao, EtapaCreateDTO etapaCreateDTO) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = edicaoService.findById(idEdicao);
        EtapaEntity etapaEntity = objectMapper.convertValue(etapaCreateDTO, EtapaEntity.class);
        etapaEntity.setEdicao(edicaoEntity);
        EtapaEntity etapaSaved = etapaRepository.save(etapaEntity);
        edicaoEntity.getEtapas().add(etapaSaved);
        edicaoService.save(edicaoEntity);
        return objectMapper.convertValue(etapaSaved, EtapaDTO.class);
    }

    public EtapaDTO update(Integer idEtapa, EtapaCreateDTO etapaUpdate) throws RegraDeNegocioException {
        EtapaEntity etapaRecover = findById(idEtapa);
        etapaRecover.setNome(etapaUpdate.getNome());
        etapaRecover.setOrdemExecucao(etapaUpdate.getOrdemExecucao());
       return objectMapper.convertValue(etapaRepository.save(etapaRecover), EtapaDTO.class);
    }

    public void delete(Integer idEtapa) throws RegraDeNegocioException {
        EtapaEntity etapaRecover = findById(idEtapa);
        etapaRepository.delete(etapaRecover);
    }

    public EtapaEntity findById(Integer id) throws RegraDeNegocioException {
        EtapaEntity etapaEntity = etapaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Etapa não encontrada"));
        return etapaEntity;
    }

    public EtapaDTO save(EtapaEntity etapaEntity) {
        etapaRepository.save(etapaEntity);
        return objectMapper.convertValue(etapaEntity, EtapaDTO.class);
    }

    private Set<ProcessoDTO> getProcessosDTO(Set<ProcessoEntity> processos) {
        return processos.stream()
                .map(processoEntity -> objectMapper.convertValue(processoEntity, ProcessoDTO.class))
                .collect(Collectors.toSet());
    }
}
