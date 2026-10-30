
package org.example.coffeeshopclient.controller;

import jakarta.servlet.http.HttpSession;
import org.example.coffeeshopclient.dto.CreateOrderRequest;
import org.example.coffeeshopclient.dto.OrderListItem;
import org.example.coffeeshopclient.dto.OrderResponse;
import org.example.coffeeshopclient.dto.OrderSummaryResponse;
import org.example.coffeeshopclient.service.CategoryApiClient;
import org.example.coffeeshopclient.service.OrderApiClient;
import org.example.coffeeshopclient.service.ProductApiClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderApiClient orderApiClient;
    private final ProductApiClient productApiClient;
    private final CategoryApiClient categoryApiClient;

    public OrderController(OrderApiClient orderApiClient, ProductApiClient productApiClient, CategoryApiClient categoryApiClient) {
        this.orderApiClient = orderApiClient;
        this.productApiClient = productApiClient;
        this.categoryApiClient = categoryApiClient;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createOrder(
            @RequestBody CreateOrderRequest request,
            HttpSession session) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        orderApiClient.createOrder(token, request);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public String getOrder(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/login";
        }

        var order = orderApiClient.getOrder(id, token);

        long totalWithoutDiscount = order.items().stream()
                .mapToLong(item -> (long) item.cost() * item.quantity())
                .sum();
        long totalWithDiscount = order.items().stream()
                .mapToLong(item ->
                        item.discountedPrice() * item.quantity())
                .sum();
/*
        long totalWithDiscount = order.items().stream()
                .mapToLong(item -> {
                    long discountedPrice = java.math.BigDecimal.valueOf(item.cost())
                            .multiply(java.math.BigDecimal.valueOf(100 - item.discount()))
                            .divide(
                                    java.math.BigDecimal.valueOf(100),
                                    0,
                                    java.math.RoundingMode.HALF_UP
                            )
                            .longValueExact();

                    return discountedPrice * item.quantity();
                })
                .sum();
*/
        //model.addAttribute("totalWithoutDiscount", totalWithoutDiscount);
        //model.addAttribute("totalWithDiscount", totalWithDiscount);
        model.addAttribute("order", order);
        model.addAttribute("item_name", order);
        model.addAttribute("totalWithoutDiscount", totalWithoutDiscount);
        model.addAttribute("totalWithDiscount", totalWithDiscount);

        System.out.println("_".repeat(80));
        System.out.println("Total without discount: " + model.getAttribute("totalWithoutDiscount"));
        System.out.println("Total with discount: " + model.getAttribute("totalWithDiscount"));
        System.out.println("_".repeat(80));


        return "order";
    }
    private long calculateElapsedSeconds(OrderSummaryResponse order) {
        if ("COMPLETED".equals(order.status())) {
            return order.durationSeconds() != null
                    ? order.durationSeconds()
                    : 0;
        }

        return Math.max(
                0,
                java.time.Duration.between(
                        order.createdAt(),
                        java.time.LocalDateTime.now()
                ).getSeconds()
        );
    }

    private List<OrderListItem> buildOrderList(
            List<OrderSummaryResponse> orders,
            String token
    ) {
        return orders.stream()
                .filter(order -> !order.deleted())
                .filter(order -> List.of(
                        "OPEN", "INPROGRESS", "COMPLETED"
                ).contains(order.status()))
                .map(order -> {
                    OrderResponse details =
                            orderApiClient.getOrder(order.id(), token);

                    long total = details.items().stream()
                            .mapToLong(item ->
                                    item.discountedPrice() * item.quantity())
                            .sum();

                    return new OrderListItem(
                            order.id(),
                            order.status(),
                            total,
                            calculateElapsedSeconds(order)
                    );
                })
                .toList();
    }

    @GetMapping
    public String getOrders(HttpSession session, Model model) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/login";
        }

        List<OrderSummaryResponse> orders = orderApiClient.getOrders(token);

        model.addAttribute("orders", buildOrderList(orders, token));
        model.addAttribute("username", session.getAttribute("username"));

        return "orders";
    }
    @GetMapping("/new")
    public String newOrder(
            HttpSession session,
            Model model) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return "redirect:/login";
        }
        model.addAttribute("products",productApiClient.getProducts(token));
        model.addAttribute("categories",categoryApiClient.getCategories(token));
        System.out.println("Products: " + model.getAttribute("products"));
        System.out.println("Categories: " + model.getAttribute("categories"));
        return "order-form";
    }

}
