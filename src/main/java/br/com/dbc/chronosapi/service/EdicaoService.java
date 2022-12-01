package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.EdicaoRepository;
import br.com.dbc.chronosapi.repository.EtapaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EdicaoService {

    private final EdicaoRepository edicaoRepository;
    private final ObjectMapper objectMapper;

    private EtapaRepository etapaRepository;

    public EdicaoDTO create(EdicaoCreateDTO edicaoCreateDTO) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = objectMapper.convertValue(edicaoCreateDTO, EdicaoEntity.class);

        if(edicaoCreateDTO.getDataInicial().isBefore(edicaoCreateDTO.getDataFinal())) {
            edicaoEntity.setStatus(Status.ATIVO);
            edicaoEntity.setEtapas(new HashSet<>());
            EdicaoEntity edicaoSaved = edicaoRepository.save(edicaoEntity);
            return objectMapper.convertValue(edicaoSaved, EdicaoDTO.class);
        } else {
            throw new RegraDeNegocioException("A data final antecede a data inicial.");
        }
    }

    public EdicaoDTO update(Integer idEdicao, EdicaoCreateDTO edicaoUpdate) throws RegraDeNegocioException {
        EdicaoEntity edicaoRecover = findById(idEdicao);

        if(edicaoUpdate.getDataInicial().isBefore(edicaoUpdate.getDataFinal())) {
            edicaoRecover.setNome(edicaoUpdate.getNome());
            edicaoRecover.setDataInicial(edicaoUpdate.getDataInicial());
            edicaoRecover.setDataFinal(edicaoUpdate.getDataFinal());
            edicaoRepository.save(edicaoRecover);
            return objectMapper.convertValue(edicaoRecover, EdicaoDTO.class);
        } else {
            throw new RegraDeNegocioException("A data final antecede a data inicial.");
        }
    }

    public void delete(Integer idEdicao) throws RegraDeNegocioException {
        EdicaoEntity edicaoRecover = findById(idEdicao);
        edicaoRepository.delete(edicaoRecover);
    }

    public EdicaoDTO enableOrDisable(Integer idEdicao) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = this.findById(idEdicao);
        if(edicaoEntity.getStatus() == Status.ATIVO) {
            edicaoEntity.setStatus(Status.INATIVO);
            edicaoRepository.save(edicaoEntity);
        }else {
            edicaoEntity.setStatus(Status.ATIVO);
            edicaoRepository.save(edicaoEntity);
        }
        return objectMapper.convertValue(edicaoEntity, EdicaoDTO.class);
    }


    // falta terminar
    public EdicaoDTO clone(Integer idEdicao) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = findById(idEdicao);
        Set<EtapaEntity> etapaEntities = etapaRepository.findAllByEdicao(edicaoEntity);
        EdicaoEntity edicaoEntityClone = new EdicaoEntity();
        Set<EtapaEntity> etapaEntities = new HashSet<>(edicaoEntity.getEtapas());
        edicaoEntityClone.setEtapas(etapaEntities);
        edicaoEntityClone.setNome(edicaoEntity.getNome() + " - Clone" );
        edicaoEntityClone.setStatus(Status.INATIVO);
        edicaoEntityClone.setDataInicial(edicaoEntity.getDataInicial());
        edicaoEntityClone.setDataFinal(edicaoEntity.getDataFinal());
        return objectMapper.convertValue(edicaoRepository.save(edicaoEntityClone), EdicaoDTO.class);
    }

    public PageDTO<EdicaoDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EdicaoEntity> paginaDoRepositorio = edicaoRepository.findAll(pageRequest);
        List<EdicaoDTO> edicaoDTOList = paginaDoRepositorio.getContent().stream()
                .map(edicao -> {
                    EdicaoDTO edicaoDTO = objectMapper.convertValue(edicao, EdicaoDTO.class);
                    edicaoDTO.setEtapas(getEtapasDTO(edicao.getEtapas()));
                    return edicaoDTO;
                }).toList();

        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                edicaoDTOList);
    }

    public EdicaoEntity findById(Integer id) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = edicaoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Edição não encontrada!"));
        return edicaoEntity;
    }

    public EdicaoDTO save(EdicaoEntity edicaoEntity) {
        edicaoRepository.save(edicaoEntity);
        return objectMapper.convertValue(edicaoEntity, EdicaoDTO.class);
    }

    public Set<EtapaDTO> getEtapasDTO(Set<EtapaEntity> etapas) {
        return etapas.stream()
                .map(etapaEntity -> objectMapper.convertValue(etapaEntity, EtapaDTO.class))
                .collect(Collectors.toSet());
    }
}
