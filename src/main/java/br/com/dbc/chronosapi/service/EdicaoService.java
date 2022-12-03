package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.*;
import br.com.dbc.chronosapi.dto.calendario.*;
import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.dto.processo.ResponsavelDTO;
import br.com.dbc.chronosapi.entity.classes.DiaNaoUtilEntity;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.DiaNaoUtilRepository;
import br.com.dbc.chronosapi.repository.EdicaoRepository;
import br.com.dbc.chronosapi.repository.EtapaRepository;
import br.com.dbc.chronosapi.repository.ProcessoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EdicaoService {

    public final int UM_DIA = 1;
    public static LocalDate dia;
    private final EdicaoRepository edicaoRepository;
    private final ObjectMapper objectMapper;
    private final EtapaRepository etapaRepository;
    private final ProcessoRepository processoRepository;
    private final DiaNaoUtilRepository diaNaoUtilRepository;

    public EdicaoDTO create(EdicaoCreateDTO edicaoCreateDTO) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = objectMapper.convertValue(edicaoCreateDTO, EdicaoEntity.class);

        if (edicaoCreateDTO.getDataInicial().isBefore(edicaoCreateDTO.getDataFinal())) {
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

        if (edicaoUpdate.getDataInicial().isBefore(edicaoUpdate.getDataFinal())) {
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
        if (edicaoEntity.getStatus() == Status.ATIVO) {
            edicaoEntity.setStatus(Status.INATIVO);
            edicaoRepository.save(edicaoEntity);
        } else {
            edicaoEntity.setStatus(Status.ATIVO);
            edicaoRepository.save(edicaoEntity);
        }
        return objectMapper.convertValue(edicaoEntity, EdicaoDTO.class);
    }


    public EdicaoDTO clone(Integer idEdicao) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = findById(idEdicao);
        EdicaoEntity edicaoEntityClone = new EdicaoEntity();
        edicaoEntityClone.setNome(edicaoEntity.getNome() + " - Clone");
        edicaoEntityClone.setStatus(Status.INATIVO);
        edicaoEntityClone.setDataInicial(edicaoEntity.getDataInicial());
        edicaoEntityClone.setDataFinal(edicaoEntity.getDataFinal());
        EdicaoEntity edicaoEntityCloneSaved = edicaoRepository.save(edicaoEntityClone);
        Set<EtapaEntity> etapaEntities = edicaoEntity.getEtapas().stream()
                .map(etapaEntity -> {
                    EtapaEntity etapaEntityClone = new EtapaEntity();
                    etapaEntityClone.setEdicao(edicaoEntityCloneSaved);
                    etapaEntityClone.setNome(etapaEntity.getNome());
                    etapaEntityClone.setOrdemExecucao(etapaEntity.getOrdemExecucao());
                    EtapaEntity etapaEntityCloneSaved = etapaRepository.save(etapaEntityClone);
                    Set<ProcessoEntity> processoEntities = etapaEntity.getProcessos().stream()
                            .map(processoEntity -> {
                                ProcessoEntity processoEntityClone = new ProcessoEntity();
                                Set<AreaEnvolvidaEntity> areasEnvolvidasEntities = new HashSet<>(processoEntity.getAreasEnvolvidas());
                                Set<ResponsavelEntity> responsaveisEntities = new HashSet<>(processoEntity.getResponsaveis());
                                processoEntityClone.setOrdemExecucao(processoEntity.getOrdemExecucao());
                                processoEntityClone.setEtapa(etapaEntityCloneSaved);
                                processoEntityClone.setDiasUteis(processoEntity.getDiasUteis());
                                processoEntityClone.setNome(processoEntity.getNome());
                                processoEntityClone.setDuracaoProcesso(processoEntity.getDuracaoProcesso());
                                processoEntityClone.setAreasEnvolvidas(areasEnvolvidasEntities);
                                processoEntityClone.setResponsaveis(responsaveisEntities);
                                return processoRepository.save(processoEntityClone);
                            }).collect(Collectors.toSet());
                    etapaEntityClone.setProcessos(processoEntities);
                    return etapaEntityCloneSaved;
                }).collect(Collectors.toSet());
        edicaoEntityClone.setEtapas(etapaEntities);
        return objectMapper.convertValue(edicaoEntityCloneSaved, EdicaoDTO.class);
    }

    public List<DiaCalendarioGeralDTO> gerarCalendarioGeral() throws RegraDeNegocioException{
        List<EdicaoEntity> edicoes = edicaoRepository.findByEdicoesAtivasOrderByDataInicial();
        if (edicoes.isEmpty()) {
            throw new RegraDeNegocioException("Não existem edições cadastradas!");
        }
        List<DiaCalendarioGeralDTO> dias = new ArrayList<>();
        List<DiaCalendarioEdicaoDTO> diaCalendarioEdicaoDTOS;

        diaCalendarioEdicaoDTOS = gerarCalendarioEdicao(edicoes.get(0).getIdEdicao());
        EdicaoEntity edicaoEntity = edicoes.get(0);
        edicoes.remove(edicaoEntity);

        for(var diaEdicaoBase : diaCalendarioEdicaoDTOS) {
            DiaCalendarioGeralDTO diaCalendarioGeralDTO = new DiaCalendarioGeralDTO();
            diaCalendarioGeralDTO.setEdicoes(new ArrayList<>());
            JuncaoEdicoesDTO juncaoEdicoesDTO = new JuncaoEdicoesDTO();
            diaCalendarioGeralDTO.setDia(diaEdicaoBase.getDia());
            diaCalendarioGeralDTO.setDiaUtil(diaEdicaoBase.getDiaUtil());
            if(diaEdicaoBase.getDiaUtil().isEhDiaUtil()){
                juncaoEdicoesDTO.setEdicao(edicaoEntity.getNome());
                juncaoEdicoesDTO.setEtapa(diaEdicaoBase.getEtapa());
                juncaoEdicoesDTO.setProcesso(diaEdicaoBase.getProcesso());
                diaCalendarioGeralDTO.getEdicoes().add(juncaoEdicoesDTO);
            }
            dias.add(diaCalendarioGeralDTO);
        }

        for(var edicao : edicoes) {
            List<DiaCalendarioEdicaoDTO> diasCalendarioEdicao = gerarCalendarioEdicao(edicao.getIdEdicao());
            for(var diaEdicaoJuncao : diasCalendarioEdicao) {
                DiaCalendarioGeralDTO diaCalendarioGeralDTO = new DiaCalendarioGeralDTO();
                diaCalendarioGeralDTO.setEdicoes(new ArrayList<>());
                JuncaoEdicoesDTO juncaoEdicoesDTO = new JuncaoEdicoesDTO();
                diaCalendarioGeralDTO.setDia(diaEdicaoJuncao.getDia());
                diaCalendarioGeralDTO.setDiaUtil(diaEdicaoJuncao.getDiaUtil());
                if(diaEdicaoJuncao.getDiaUtil().isEhDiaUtil()){
                    juncaoEdicoesDTO.setEdicao(edicao.getNome());
                    juncaoEdicoesDTO.setEtapa(diaEdicaoJuncao.getEtapa());
                    juncaoEdicoesDTO.setProcesso(diaEdicaoJuncao.getProcesso());
                    diaCalendarioGeralDTO.getEdicoes().add(juncaoEdicoesDTO);
                }
                for(var diaEdicaoBase : dias) {
                    if(diaEdicaoBase.getDia().equals(diaEdicaoJuncao.getDia()) && diaEdicaoBase.getDiaUtil().isEhDiaUtil()) {
                        diaEdicaoBase.getEdicoes().add(juncaoEdicoesDTO);
                    }
                }
            }
        }
        return dias;
    }

    public List<DiaCalendarioEdicaoDTO> gerarCalendarioEdicao(Integer idEdicao) throws RegraDeNegocioException {
        List<DiaNaoUtilEntity> diasNaoUteis = diaNaoUtilRepository.findAll(Sort.by("dataInicial").ascending());
        EdicaoEntity edicaoEntity = findById(idEdicao);
        Set<EtapaEntity> etapas = edicaoEntity.getEtapas();
        LocalDate dataInicial = edicaoEntity.getDataInicial();
        List<DiaCalendarioEdicaoDTO> dias = new ArrayList<>();
        dia = dataInicial;

        etapas.stream()
                .map(etapaEntity -> {
                    return etapaEntity.getProcessos().stream()
                            .map(processoEntity -> {
                                Integer diasUteisProcesso = processoEntity.getDiasUteis();
                                int contDiasUteis = UM_DIA;
                                int diasCorridos;
                                while(contDiasUteis <= diasUteisProcesso) {
                                    DayOfWeek diaDaSemana = dia.getDayOfWeek();

                                    FeriadoDTO feriadoDTO = verificarDiasNaoUteis(dia, diasNaoUteis);

                                    if(diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY && feriadoDTO.getQtdDias() < UM_DIA) {
                                        DiaCalendarioEdicaoDTO diaCalendarioEdicaoDTO = new DiaCalendarioEdicaoDTO();
                                        DiaUtilDTO diaUtilDTO = new DiaUtilDTO();
                                        diaUtilDTO.setEhDiaUtil(false);
                                        diaUtilDTO.setEhDiaNaoUtil(true);
                                        diaUtilDTO.setDescricao(null);
                                        diaCalendarioEdicaoDTO.setDiaUtil(diaUtilDTO);
                                        diaCalendarioEdicaoDTO.setDia(dia);
                                        diaCalendarioEdicaoDTO.setEtapa(null);
                                        diaCalendarioEdicaoDTO.setProcesso(null);
                                        dias.add(diaCalendarioEdicaoDTO);
                                        dia = dia.plusDays(UM_DIA);
                                    }else if(feriadoDTO.getQtdDias() > 0){
                                        diasCorridos = UM_DIA;
                                        while (diasCorridos <= feriadoDTO.getQtdDias()){
                                            DiaCalendarioEdicaoDTO diaCalendarioEdicaoDTO = new DiaCalendarioEdicaoDTO();
                                            DiaUtilDTO diaUtilDTO = new DiaUtilDTO();
                                            diaUtilDTO.setEhDiaUtil(false);
                                            diaUtilDTO.setEhDiaNaoUtil(true);
                                            diaUtilDTO.setDescricao(feriadoDTO.getDescricao());
                                            diaCalendarioEdicaoDTO.setDiaUtil(diaUtilDTO);
                                            diaCalendarioEdicaoDTO.setDia(dia);
                                            diaCalendarioEdicaoDTO.setEtapa(null);
                                            diaCalendarioEdicaoDTO.setProcesso(null);
                                            dias.add(diaCalendarioEdicaoDTO);
                                            dia = dia.plusDays(UM_DIA);
                                            diasCorridos++;
                                        }
                                    }else {
                                        DiaCalendarioEdicaoDTO diaCalendarioEdicaoDTO = new DiaCalendarioEdicaoDTO();
                                        DiaUtilDTO diaUtilDTO = new DiaUtilDTO();
                                        diaUtilDTO.setEhDiaUtil(true);
                                        diaUtilDTO.setEhDiaNaoUtil(false);
                                        diaUtilDTO.setDescricao(null);
                                        diaCalendarioEdicaoDTO.setDiaUtil(diaUtilDTO);
                                        diaCalendarioEdicaoDTO.setDia(dia);
                                        diaCalendarioEdicaoDTO.setEtapa(objectMapper.convertValue(etapaEntity, EtapaDTO.class));
                                        ProcessoDTO processoDTO = objectMapper.convertValue(processoEntity, ProcessoDTO.class);
                                        processoDTO.setAreasEnvolvidas(this.getAreaEnvolvidaDTO(processoEntity.getAreasEnvolvidas()));
                                        processoDTO.setResponsaveis((this.getResponsavelDTO(processoEntity.getResponsaveis())));
                                        diaCalendarioEdicaoDTO.setProcesso(processoDTO);
                                        dias.add(diaCalendarioEdicaoDTO);
                                        dia = dia.plusDays(UM_DIA);
                                        contDiasUteis++;
                                    }
                                }
                                return processoEntity;
                            }).toList();
                }).toList();
        return dias;
    }

    public FeriadoDTO verificarDiasNaoUteis(LocalDate dia, List<DiaNaoUtilEntity> diasNaoUteis) {
        FeriadoDTO feriado = new FeriadoDTO();
        diasNaoUteis.stream()
                .forEach(diaNaoUtilEntity -> {
                    diaNaoUtilEntity.setDataInicial(LocalDate.of(dia.getYear(), diaNaoUtilEntity.getDataInicial().getMonthValue(), diaNaoUtilEntity.getDataInicial().getDayOfMonth()));
                    if(diaNaoUtilEntity.getRepeticaoAnual() == Status.ATIVO && diaNaoUtilEntity.getDataInicial().equals(dia)) {
                        feriado.setQtdDias(1);
                        feriado.setDescricao(diaNaoUtilEntity.getDescricao());
                    }else if(diaNaoUtilEntity.getDataInicial().equals(dia)){
                        Period between = Period.between(diaNaoUtilEntity.getDataInicial(), diaNaoUtilEntity.getDataFinal());
                        int qtdDias = between.getDays()+1;
                        feriado.setQtdDias(qtdDias);
                        feriado.setDescricao(diaNaoUtilEntity.getDescricao());
                    }
                });
        return feriado;
    }

    public PageDTO<EdicaoDTO> listComEtapa(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EdicaoEntity> paginaDoRepositorio = edicaoRepository.findAll(pageRequest);
        List<EdicaoDTO> edicaoDTOList = paginaDoRepositorio.getContent().stream()
                .map(edicao -> {
                    EdicaoDTO edicaoDTO = objectMapper.convertValue(edicao, EdicaoDTO.class);
                    edicaoDTO.setEtapas(edicao.getEtapas().stream()
                            .map(etapaEntity -> objectMapper.convertValue(etapaEntity, EtapaDTO.class)
                            ).collect(Collectors.toList()));
                    return edicaoDTO;
                }).toList();

        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                edicaoDTOList);
    }

    public PageDTO<EdicaoDTO> list(Integer pagina, Integer tamanho) {

        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EdicaoEntity> paginaDoRepositorio = edicaoRepository.findAll(pageRequest);

        List<EdicaoDTO> edicaoDTOList = paginaDoRepositorio.getContent().stream()
                .map(edicao -> objectMapper.convertValue(edicao, EdicaoDTO.class)).toList();

        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                edicaoDTOList);
    }

    public EdicaoEntity findById(Integer id) throws RegraDeNegocioException {
        return edicaoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Edição não encontrada!"));
    }

    public EdicaoDTO save(EdicaoEntity edicaoEntity) {
        edicaoRepository.save(edicaoEntity);
        return objectMapper.convertValue(edicaoEntity, EdicaoDTO.class);
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
