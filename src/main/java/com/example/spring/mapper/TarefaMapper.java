package com.example.spring.mapper;

import com.example.spring.dto.TarefaRequest;
import com.example.spring.dto.TarefaResponse;
import com.example.spring.entity.Tarefa;

public final class TarefaMapper {

    private TarefaMapper() {
    }

    public static Tarefa toEntity(TarefaRequest request) {
        return new Tarefa(
                request.titulo(),
                request.descricao(),
                Boolean.TRUE.equals(request.concluida()));
    }

    public static void updateEntity(Tarefa tarefa, TarefaRequest request) {
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        if (request.concluida() != null) {
            tarefa.setConcluida(request.concluida());
        }
    }

    public static TarefaResponse toResponse(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.isConcluida());
    }
}
