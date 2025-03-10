package br.com.fiap.task_manager_security.service;


import br.com.fiap.task_manager_security.controller.dto.TaskDTO;
import br.com.fiap.task_manager_security.entity.Task;
import br.com.fiap.task_manager_security.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public TaskService(TaskMapper taskMapper, TaskRepository taskRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }

    public TaskDTO saveTask(TaskDTO taskDTO) {
        Task taskToSave = taskMapper.toEntity(taskDTO);
        Task savedTask = taskRepository.save(taskToSave);
        taskDTO.setId(savedTask.getId());
        return taskDTO;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        Task foundTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.toDTO(foundTask);
    }

    public void deleteTaskById(Long id) {
        Task taskToDelete = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(taskToDelete);
    }

    public List<Long> getAllTaskIds() {
        return taskRepository.findAll().stream().map(Task::getId).collect(Collectors.toList());
    }
}
