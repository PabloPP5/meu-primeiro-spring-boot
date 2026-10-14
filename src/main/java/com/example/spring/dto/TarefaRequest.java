package com.example.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TarefaRequest(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
        String titulo,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String descricao,

        Boolean concluida
) {
}
