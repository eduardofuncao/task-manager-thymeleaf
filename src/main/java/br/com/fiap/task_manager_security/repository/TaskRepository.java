package br.com.fiap.task_manager_security.repository;

import br.com.fiap.task_manager_security.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
