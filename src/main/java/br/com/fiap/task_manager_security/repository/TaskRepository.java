package br.com.fiap.task_manager_security.repository;

import br.com.fiap.task_manager_security.entity.Task;
import br.com.fiap.task_manager_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignee(User assignee);
}
