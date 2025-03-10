package br.com.fiap.task_manager_security.controller;

import br.com.fiap.task_manager_security.controller.dto.TaskDTO;
import br.com.fiap.task_manager_security.controller.dto.UserDTO;
import br.com.fiap.task_manager_security.entity.*;
import br.com.fiap.task_manager_security.service.TaskService;
import br.com.fiap.task_manager_security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public String listTasks(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) userService.loadUserByUsername(auth.getName());

        List<TaskDTO> tasks;
        if (currentUser.getRole() == UserRole.ROLE_COLLABORATOR) {
            tasks = taskService.findByAssignee(currentUser);
        } else {
            tasks = taskService.getAll();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("currentUser", currentUser);
        return "tasks";
    }

    @GetMapping("/new")
    public String newTaskForm(Model model) {
        model.addAttribute("task", new Task());

        List<UserDTO> collaborators = userService.getAll()
                .stream()
                .filter(user -> user.getRole() == UserRole.ROLE_COLLABORATOR)
                .toList();

        model.addAttribute("collaborators", collaborators);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());
        return "new-task";
    }

    @PostMapping
    public String createTask(@Valid @ModelAttribute("task") TaskDTO task,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            List<UserDTO> collaborators = userService.getAll()
                    .stream()
                    .filter(user -> user.getRole() == UserRole.ROLE_COLLABORATOR)
                    .toList();

            model.addAttribute("collaborators", collaborators);
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());
            return "new-task";
        }

        taskService.save(task);
        redirectAttributes.addFlashAttribute("success", "Tarefa criada com sucesso!");
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editTaskForm(@PathVariable Long id, Model model) {
        TaskDTO taskDTO = taskService.getById(id);

        model.addAttribute("task", taskDTO);

        List<UserDTO> collaborators = userService.getAll()
                .stream()
                .filter(user -> user.getRole() == UserRole.ROLE_COLLABORATOR)
                .collect(Collectors.toList());

        model.addAttribute("collaborators", collaborators);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());
        return "edit-task";
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id,
                             @Valid @ModelAttribute("task") Task task,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            List<UserDTO> collaborators = userService.getAll()
                    .stream()
                    .filter(user -> user.getRole() == UserRole.ROLE_COLLABORATOR)
                    .toList();

            model.addAttribute("collaborators", collaborators);
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());
            return "edit-task";
        }

        TaskDTO existingTask = taskService.getById(id);

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setAssigneeId(task.getAssignee().getId());

        taskService.save(existingTask);
        redirectAttributes.addFlashAttribute("success", "Tarefa atualizada com sucesso!");
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Tarefa excluída com sucesso!");
        return "redirect:/tasks";
    }

    @PostMapping("/update-status/{id}")
    public String updateTaskStatus(@PathVariable Long id,
                                   @RequestParam TaskStatus status,
                                   RedirectAttributes redirectAttributes) {

        TaskDTO taskDTO = taskService.getById(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) userService.loadUserByUsername(auth.getName());

        if (taskDTO.getAssigneeId().equals(currentUser.getId())) {
            taskDTO.setStatus(status);
            taskService.save(taskDTO);
            redirectAttributes.addFlashAttribute("success", "Status da tarefa atualizado com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Você não tem permissão para atualizar esta tarefa.");
        }

        return "redirect:/tasks";
    }
}
