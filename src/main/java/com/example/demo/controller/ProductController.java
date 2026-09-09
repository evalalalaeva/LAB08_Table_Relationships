package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.service.ProductService;
import com.example.demo.strategy.DiscountContext;

/**
 * SRP: this class's only job is translating HTTP requests/responses to
 * and from ProductService calls and Thymeleaf model attributes. It
 * contains no persistence code and no business rules - both live one
 * layer down, in ProductService.
 *
 * DIP: depends on ProductService (concrete, but itself built on
 * repository abstractions) via constructor injection.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** Options shown in the discountType <select> on add/edit forms. */
    private Map<String, String> discountOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put(DiscountContext.NONE, "No Discount");
        options.put(DiscountContext.MEMBER, "Member Discount (10%)");
        options.put(DiscountContext.SEASONAL, "Seasonal Sale (20%)");
        return options;
    }

    // GET /products - list
    @GetMapping
    public String list(Model model) {
        var products = productService.findAll();
        Map<Long, Double> finalPrices = new LinkedHashMap<>();
        for (Product p : products) {
            finalPrices.put(p.getId(), productService.calculateFinalPrice(p));
        }
        model.addAttribute("products", products);
        model.addAttribute("finalPrices", finalPrices);
        return "products/list";
    }

    // GET /products/add - show add form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        Product product = new Product();
        product.setDetail(new ProductDetail());
        model.addAttribute("product", product);
        model.addAttribute("discountOptions", discountOptions());
        return "products/add";
    }

    // POST /products/save - create
    @PostMapping("/save")
    public String save(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
        productService.save(product);
        redirectAttributes.addFlashAttribute("message", "Product created successfully.");
        return "redirect:/products";
    }

    // GET /products/edit/{id} - show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.findById(id)
                .map(product -> {
                    if (product.getDetail() == null) {
                        product.setDetail(new ProductDetail());
                    }
                    model.addAttribute("product", product);
                    model.addAttribute("discountOptions", discountOptions());
                    return "products/edit";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Product not found.");
                    return "redirect:/products";
                });
    }

    // POST /products/update/{id} - update
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("product") Product product,
                          RedirectAttributes redirectAttributes) {
        product.setId(id);
        productService.save(product);
        redirectAttributes.addFlashAttribute("message", "Product updated successfully.");
        return "redirect:/products";
    }

    // GET /products/delete/{id} - confirm delete
    @GetMapping("/delete/{id}")
    public String showDeleteConfirm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.findById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "products/delete";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Product not found.");
                    return "redirect:/products";
                });
    }

    // POST /products/delete/{id} - perform delete
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully.");
        return "redirect:/products";
    }
}
