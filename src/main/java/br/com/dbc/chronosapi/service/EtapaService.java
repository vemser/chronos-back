package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.EtapaCreateDTO;
import br.com.dbc.chronosapi.dto.EtapaDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.EtapaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EtapaService {

    private final ObjectMapper objectMapper;
    private final EtapaRepository etapaRepository;

    public EtapaDTO create(EtapaCreateDTO etapaCreateDTO) {
        EtapaEntity etapaEntity = objectMapper.convertValue(etapaCreateDTO, EtapaEntity.class);
        EtapaEntity etapaSaved = etapaRepository.save(etapaEntity);
        return objectMapper.convertValue(etapaSaved, EtapaDTO.class);
    }

    public EtapaDTO update(Integer idEtapa, EtapaCreateDTO etapaUpdate) throws RegraDeNegocioException {
        EtapaEntity etapaRecover = findById(idEtapa);
        etapaRecover.setNome(etapaUpdate.getNome());
       return objectMapper.convertValue(etapaRepository.save(etapaRecover), EtapaDTO.class);
    }

    public void delete(Integer idEtapa) throws RegraDeNegocioException {
        EtapaEntity etapaRecover = findById(idEtapa);
        etapaRepository.delete(etapaRecover);
    }

    public PageDTO<EtapaDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EtapaEntity> paginaDoRepositorio = etapaRepository.findAll(pageRequest);
        List<EtapaDTO> etapasDaPagina = paginaDoRepositorio.getContent().stream()
                .map(etapaEntity -> objectMapper.convertValue(etapaEntity, EtapaDTO.class))
                .toList();
        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                etapasDaPagina
        );
    }

    public EtapaEntity findById(Integer id) throws RegraDeNegocioException {
        EtapaEntity etapaEntity = etapaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Etapa não encontrada"));
        return etapaEntity;
    }

}
