package com.khodataev.my.app.controller;

import com.khodataev.my.app.model.Role;
import com.khodataev.my.app.model.User;
import com.khodataev.my.app.service.RoleService;
import com.khodataev.my.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.Set;


@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin")
    public String welcome() {
        return "redirect:/admin/all";
    }

    @GetMapping(value = "admin/all")
    public String allUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "allUsersPage";
    }

    @GetMapping(value = "admin/add")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "addUser";
    }

    @PostMapping(value = "admin/add")
    public String postAddUser(@ModelAttribute("user") User user,
                              @RequestParam(required=false) String roleAdmin,
                              @RequestParam(required=false) String roleVIP) {
        Set<Role> roles = new HashSet<>();
        //roles.add(roleService.getRoleByName("ROLE_USER"));
        roles.add(roleService.getRoleByName("ROLE_USER"));
        if (roleAdmin != null && roleAdmin.equals("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        if (roleVIP != null && roleVIP.equals("ROLE_VIP")) {
            roles.add(roleService.getRoleByName("ROLE_VIP"));
        }
        user.setRoles(roles);
        userService.addUser(user);

        return "redirect:/admin";
    }


    @GetMapping(value = "admin/edit/{id}")
    public String editUser(ModelMap model, @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        Set<Role> roles = user.getRoles();
        for (Role role: roles) {
            if (role.equals(roleService.getRoleByName("ROLE_ADMIN"))) {
                model.addAttribute("roleAdmin", true);
            }
            if (role.equals(roleService.getRoleByName("ROLE_VIP"))) {
                model.addAttribute("roleVIP", true);
            }
        }
        model.addAttribute("user", user);
        return "editUser";
    }
    @PostMapping(value = "admin/edit")
    public String postEditUser(@ModelAttribute("user") User user,
                               @RequestParam(required=false) String roleAdmin,
                               @RequestParam(required=false) String roleVIP) {

        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ROLE_USER"));
        if (roleAdmin != null && roleAdmin .equals("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        if (roleVIP != null && roleVIP.equals("ROLE_VIP")) {
            roles.add(roleService.getRoleByName("ROLE_VIP"));
        }
        user.setRoles(roles);
        userService.editUser(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
