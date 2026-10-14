package com.example.spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.spring.dto.TarefaRequest;
import com.example.spring.dto.TarefaResponse;
import com.example.spring.entity.Tarefa;
import com.example.spring.exception.RecursoNaoEncontradoException;
import com.example.spring.repository.TarefaRepository;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    void deveCriarTarefa() {
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(invocation -> {
            Tarefa tarefa = invocation.getArgument(0);
            tarefa.setId(1L);
            return tarefa;
        });

        TarefaResponse response = tarefaService.criar(new TarefaRequest("Estudar", "Spring Boot", false));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.titulo()).isEqualTo("Estudar");
        assertThat(response.descricao()).isEqualTo("Spring Boot");
        assertThat(response.concluida()).isFalse();
        verify(tarefaRepository).save(any(Tarefa.class));
    }

    @Test
    void deveListarTarefas() {
        when(tarefaRepository.findAll()).thenReturn(List.of(
                new Tarefa("A", null, false),
                new Tarefa("B", null, true)));

        List<TarefaResponse> response = tarefaService.listar();

        assertThat(response).hasSize(2);
        assertThat(response.get(0).titulo()).isEqualTo("A");
        assertThat(response.get(1).concluida()).isTrue();
    }

    @Test
    void deveBuscarPorId() {
        Tarefa tarefa = new Tarefa("A", null, false);
        tarefa.setId(5L);
        when(tarefaRepository.findById(5L)).thenReturn(Optional.of(tarefa));

        TarefaResponse response = tarefaService.buscarPorId(5L);

        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.titulo()).isEqualTo("A");
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrar() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tarefaService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deveAtualizarTarefa() {
        Tarefa tarefa = new Tarefa("Antigo", null, false);
        tarefa.setId(7L);
        when(tarefaRepository.findById(7L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TarefaResponse response = tarefaService.atualizar(7L, new TarefaRequest("Novo", "desc", true));

        assertThat(response.titulo()).isEqualTo("Novo");
        assertThat(response.concluida()).isTrue();
    }

    @Test
    void deveRemoverTarefa() {
        Tarefa tarefa = new Tarefa("A", null, false);
        tarefa.setId(7L);
        when(tarefaRepository.findById(7L)).thenReturn(Optional.of(tarefa));

        tarefaService.remover(7L);

        verify(tarefaRepository).delete(tarefa);
    }
}
