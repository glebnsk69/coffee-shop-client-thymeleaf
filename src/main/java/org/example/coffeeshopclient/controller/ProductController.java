package org.example.coffeeshopclient.controller;

import jakarta.servlet.http.HttpSession;
import org.example.coffeeshopclient.service.CategoryApiClient;
import org.example.coffeeshopclient.service.ProductApiClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.coffeeshopclient.dto.ProductResponse;
import java.util.List;

@Controller
public class ProductController {

    private final ProductApiClient productApiClient;
    private final CategoryApiClient categoryApiClient;

    public ProductController(ProductApiClient productApiClient,CategoryApiClient categoryApiClient) {
        this.productApiClient = productApiClient;
        this.categoryApiClient = categoryApiClient;
    }

    @GetMapping("/products")
    public String products(HttpSession session, Model model) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/login";
        }
        var products = productApiClient.getProducts(token);
//        String productsJson = productApiClient.getProductsJson(token);

//        model.addAttribute("productsJson", productsJson);
//        model.addAttribute("username", session.getAttribute("username"));

 //       var categories = categoryApiClient.getCategories(token);
 //       System.out.println("Categories: " + categories);

        model.addAttribute(
                "categories",
                categoryApiClient.getCategories(token)
        );

        model.addAttribute("products", products);
        model.addAttribute("username", session.getAttribute("username"));

        return "products";
    }
}