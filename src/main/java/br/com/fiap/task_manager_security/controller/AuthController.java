package br.com.fiap.task_manager_security.controller;

import br.com.fiap.task_manager_security.controller.dto.UserDTO;
import br.com.fiap.task_manager_security.entity.User;
import br.com.fiap.task_manager_security.entity.UserRole;
import br.com.fiap.task_manager_security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", UserRole.values());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO userDTO,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        System.out.println(userDTO);
        if (userService.existsByEmail(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Este email já está em uso");
        }

        if (userService.existsByCpf(userDTO.getCpf())) {
            bindingResult.rejectValue("cpf", "error.user", "Este CPF já está em uso");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.save(userDTO);
        redirectAttributes.addFlashAttribute("success", "Usuário cadastrado com sucesso");
        return "redirect:/home";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }


}
