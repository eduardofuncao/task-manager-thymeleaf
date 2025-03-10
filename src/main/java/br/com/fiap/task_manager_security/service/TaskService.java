package br.com.fiap.task_manager_security.service;


import br.com.fiap.task_manager_security.controller.dto.TaskDTO;
import br.com.fiap.task_manager_security.controller.dto.UserDTO;
import br.com.fiap.task_manager_security.controller.mapper.TaskMapper;
import br.com.fiap.task_manager_security.controller.mapper.UserMapper;
import br.com.fiap.task_manager_security.entity.Task;
import br.com.fiap.task_manager_security.entity.TaskStatus;
import br.com.fiap.task_manager_security.entity.User;
import br.com.fiap.task_manager_security.repository.TaskRepository;
import br.com.fiap.task_manager_security.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserMapper userMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
    }

    public List<TaskDTO> getAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    public List<TaskDTO> getByAsignee(UserDTO asignee) {
        User user = userMapper.toEntity(asignee);
        List<Task> foundTasks= taskRepository.findByAssignee(user);
        return foundTasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getById(Long id) {
        Task foundTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        return taskMapper.toDTO(foundTask);
    }

    public TaskDTO save(TaskDTO taskDTO) {
        Task taskToSave = taskMapper.toEntity(taskDTO);
        User foundUser = userRepository.findById(taskDTO.getAssigneeId()).orElseThrow(()-> new RuntimeException("Usuário designado não encontrado"));
        taskToSave.setAssignee(foundUser);
        Task savedTask = taskRepository.save(taskToSave);
        savedTask.setAssignee(foundUser);
        taskDTO.setId(savedTask.getId());
        return taskDTO;
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public List<TaskDTO> findByAssignee(User currentUser) {
        return taskRepository.findByAssignee(currentUser).stream()
                .map(taskMapper::toDTO)
                .toList();
    }


}
