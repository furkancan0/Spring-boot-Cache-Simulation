package com.cache;

import com.cache.Entity.Product;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cache")
public class CacheController {
    private final ProductService productService;

    public CacheController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping("/product")
    public void addProduct(@RequestBody String name) {
        productService.createProduct(name);
    }

    @GetMapping("/product")
    public void getProduct() {
        productService.fillCache();
    }

    @GetMapping("/product/clear")
    public void clear() {
        productService.clear();
    }
}
