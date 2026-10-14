package com.example.spring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.spring.dto.TarefaResponse;
import com.example.spring.exception.RecursoNaoEncontradoException;
import com.example.spring.service.TarefaService;

@WebMvcTest(TarefaController.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TarefaService tarefaService;

    @Test
    void deveRetornar201AoCriar() throws Exception {
        when(tarefaService.criar(any())).thenReturn(new TarefaResponse(1L, "Estudar", null, false));

        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Estudar\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Estudar"));
    }

    @Test
    void deveRetornar400QuandoTituloVazio() throws Exception {
        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").isArray());
    }

    @Test
    void deveListar() throws Exception {
        when(tarefaService.listar()).thenReturn(List.of(new TarefaResponse(1L, "A", null, false)));

        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("A"));
    }

    @Test
    void deveBuscarPorId() throws Exception {
        when(tarefaService.buscarPorId(1L)).thenReturn(new TarefaResponse(1L, "A", null, false));

        mockMvc.perform(get("/api/tarefas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornar404QuandoNaoEncontrar() throws Exception {
        when(tarefaService.buscarPorId(99L))
                .thenThrow(new RecursoNaoEncontradoException("Tarefa não encontrada com id 99"));

        mockMvc.perform(get("/api/tarefas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"));
    }

    @Test
    void deveAtualizar() throws Exception {
        when(tarefaService.atualizar(any(), any())).thenReturn(new TarefaResponse(1L, "Novo", null, true));

        mockMvc.perform(put("/api/tarefas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Novo\",\"concluida\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Novo"))
                .andExpect(jsonPath("$.concluida").value(true));
    }

    @Test
    void deveRemoverRetornando204() throws Exception {
        mockMvc.perform(delete("/api/tarefas/1"))
                .andExpect(status().isNoContent());
    }
}
