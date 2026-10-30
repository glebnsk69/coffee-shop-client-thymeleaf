
package org.example.coffeeshopclient.controller;

import jakarta.servlet.http.HttpSession;
import org.example.coffeeshopclient.service.AuthApiClient;
import org.example.coffeeshopclient.util.JwtUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientResponseException;

@Controller
public class AuthController {

    private final AuthApiClient authApiClient;

    public AuthController(AuthApiClient authApiClient) {
        this.authApiClient = authApiClient;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        try {
            var response = authApiClient.login(username, password);

            session.setAttribute("accessToken", response.token());
            session.setAttribute("username", username);

            System.out.println("username:"+username+" Token:"+response.token());
            System.out.println("Login session ID: " + session.getId());
            System.out.println("Token saved: " +
                    (session.getAttribute("accessToken") != null));
            System.out.println("_".repeat(80)+"\nLogin response is null: " + (response == null));
            System.out.println("Token is null: " +
                    (response == null || response.token() == null));
            System.out.println(JwtUtils.getRole(response.token()));
            session.setAttribute("roleName", JwtUtils.getRole(response.token()));
            return "redirect:/";
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                model.addAttribute("error", "Неверный логин или пароль");
            } else {
                model.addAttribute("error", "Ошибка при обращении к backend");
            }
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
