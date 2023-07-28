package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/create")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "user-create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.create(user);
        return "redirect:/home";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('USER') and authentication.principal.id == #userId")
    @GetMapping("/{userId}/read")
    public String readUser(@PathVariable("userId") Long userId, Model model){
        User user = userService.findByID(userId);
        model.addAttribute("user", user);
        return "user-info";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('USER') and authentication.principal.id == #userId")
    @GetMapping("/{userId}/update")
    public String updateUser(@PathVariable("userId") Long userId, Model model){
        User user = userService.findByID(userId);
        model.addAttribute("user", user);
        return "user-update";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('USER') and authentication.principal.id == #userId")
    @PostMapping("/{userId}/update")
    public String updateUser(@PathVariable("userId") Long userId,
                            @ModelAttribute("user") User user){
        User oldUser = userService.findByID(userId);
        user.setId(oldUser.getId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.update(user);
        return "redirect:/users/" + user.getId() + "/read";
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('USER') and authentication.principal.id == #userId")
    @GetMapping("/{userId}/delete")
    public String deleteUser(@PathVariable("userId") Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(((User) authentication.getPrincipal()).getId() == userId){
            userService.delete(userId);
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        }
        userService.delete(userId);
        return "redirect:/login";
    }
}