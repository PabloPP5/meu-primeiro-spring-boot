package com.example.spring.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring.dto.TarefaRequest;
import com.example.spring.dto.TarefaResponse;
import com.example.spring.entity.Tarefa;
import com.example.spring.exception.RecursoNaoEncontradoException;
import com.example.spring.mapper.TarefaMapper;
import com.example.spring.repository.TarefaRepository;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    @Transactional(readOnly = true)
    public List<TarefaResponse> listar() {
        return tarefaRepository.findAll().stream()
                .map(TarefaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TarefaResponse buscarPorId(Long id) {
        return TarefaMapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public TarefaResponse criar(TarefaRequest request) {
        Tarefa salva = tarefaRepository.save(TarefaMapper.toEntity(request));
        return TarefaMapper.toResponse(salva);
    }

    @Transactional
    public TarefaResponse atualizar(Long id, TarefaRequest request) {
        Tarefa tarefa = buscarEntidade(id);
        TarefaMapper.updateEntity(tarefa, request);
        return TarefaMapper.toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional
    public void remover(Long id) {
        tarefaRepository.delete(buscarEntidade(id));
    }

    private Tarefa buscarEntidade(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada com id " + id));
    }
}
