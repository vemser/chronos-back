package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.ProcessoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessoService {
    private final ProcessoRepository processoRepository;
    private final ObjectMapper objectMapper;

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

    public ProcessoDTO create(ProcessoCreateDTO processoCreateDTO) {
        ProcessoEntity processoEntity = objectMapper.convertValue(processoCreateDTO, ProcessoEntity.class);
        return objectMapper.convertValue(processoRepository.save(processoEntity), ProcessoDTO.class);
    }

    public ProcessoDTO update(Integer idProcesso, ProcessoCreateDTO processoUpdate) throws RegraDeNegocioException {
        ProcessoDTO processoRecoverDTO = objectMapper.convertValue(this.findById(idProcesso), ProcessoDTO.class);
        processoRecoverDTO.setNome(processoUpdate.getNome());
        processoRecoverDTO.setEtapa(processoUpdate.getEtapa());
        processoRecoverDTO.setAreaEnvolvida(processoUpdate.getAreaEnvolvida());
        processoRecoverDTO.setResponsavel(processoRecoverDTO.getResponsavel());
        processoRecoverDTO.setDiasUteis(processoUpdate.getDiasUteis());
        processoRecoverDTO.setOrdemExecucao(processoUpdate.getOrdemExecucao());
        processoRecoverDTO.setDuracaoProcesso(processoUpdate.getDuracaoProcesso());
        processoRecoverDTO.setEdicoes(processoUpdate.getEdicoes());
        ProcessoEntity processoEntity = objectMapper.convertValue(processoRecoverDTO, ProcessoEntity.class);
        return objectMapper.convertValue(processoRepository.save(processoEntity), ProcessoDTO.class);
    }

    public void delete(Integer isProcesso) throws RegraDeNegocioException {
        ProcessoEntity processoRecover = findById(isProcesso);
        processoRepository.delete(processoRecover);
    }

    public ProcessoEntity findById(Integer idProcesso) throws RegraDeNegocioException {
        return processoRepository.findById(idProcesso)
                .orElseThrow(() -> new RegraDeNegocioException("Processo não encontrado!"));
    }
}
