package com.example.spring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.spring.entity.Tarefa;

@DataJpaTest
class TarefaRepositoryTest {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Test
    void deveSalvarEBuscarTarefa() {
        Tarefa salva = tarefaRepository.save(new Tarefa("Estudar", "Spring Boot", false));

        assertThat(salva.getId()).isNotNull();

        Optional<Tarefa> encontrada = tarefaRepository.findById(salva.getId());
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getTitulo()).isEqualTo("Estudar");
        assertThat(encontrada.get().isConcluida()).isFalse();
    }

    @Test
    void deveListarTodasAsTarefas() {
        tarefaRepository.save(new Tarefa("A", null, false));
        tarefaRepository.save(new Tarefa("B", null, true));

        assertThat(tarefaRepository.findAll()).hasSize(2);
    }

    @Test
    void deveRemoverTarefa() {
        Tarefa salva = tarefaRepository.save(new Tarefa("A", null, false));

        tarefaRepository.deleteById(salva.getId());

        assertThat(tarefaRepository.findById(salva.getId())).isEmpty();
    }
}
