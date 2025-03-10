package br.com.fiap.task_manager_security.controller.dto;

import br.com.fiap.task_manager_security.entity.Task;
import br.com.fiap.task_manager_security.entity.TaskPriority;
import br.com.fiap.task_manager_security.entity.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Título é obrigatório")
    private String title;

    @NotBlank(message = "Descrição é obrigatória")
    private String description;

    @NotNull(message = "Data de vencimento é obrigatória")
    @FutureOrPresent(message = "A data de vencimento não pode ser anterior à data atual")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.MEDIUM;

    private Long assigneeId;

}
