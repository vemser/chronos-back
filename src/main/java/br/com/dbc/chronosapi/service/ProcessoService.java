package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.ProcessoDTO;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.ProcessoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessoService {
    private final ProcessoRepository processoRepository;
    private final ObjectMapper objectMapper;

    public List<ProcessoDTO> list() {
        return processoRepository.findAll().stream()
                .map(processoEntity -> objectMapper.convertValue(processoEntity, ProcessoDTO.class))
                .toList();
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
