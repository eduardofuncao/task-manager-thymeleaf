package br.com.fiap.task_manager_security.repository;

import br.com.fiap.task_manager_security.entity.Task;
import br.com.fiap.task_manager_security.entity.TaskStatus;
import br.com.fiap.task_manager_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignee(User assignee);
    List<Task> findByAssigneeId(Long id);

    @Query("SELECT t.status as status, COUNT(t) as count FROM Task t WHERE t.assignee.id = :userId GROUP BY t.status")
    List<Map<String, Object>> countTasksByStatusForUser(Long userId);

    // Get task stats grouped by user ID and status
    @Query("SELECT t.assignee.id as userId, t.status as status, COUNT(t) as count " +
            "FROM Task t WHERE t.assignee IS NOT NULL GROUP BY t.assignee.id, t.status")
    List<Map<String, Object>> getTaskStatsByUserAndStatus();
}
