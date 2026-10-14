package com.example.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring.entity.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
