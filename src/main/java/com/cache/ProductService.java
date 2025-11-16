package com.cache;

import com.cache.CacheUtil.TTL_LRUCache;
import com.cache.Entity.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final TTL_LRUCache<String, Product> productCache;

    public ProductService(ProductRepository productRepository, TTL_LRUCache<String, Product> productCache) {
        this.productRepository = productRepository;
        this.productCache = productCache;
    }

    public ProductDto getProduct(Long id) {
        logHeapUsage("Before processing request");
        String key = "product-" + id;

        Product cached = productCache.get(key);
        if (cached != null){
            log.info("Cached hit {}", toDto(cached).getName());
            return toDto(cached);
        }
        log.info("Cached miss");

        Product dbProduct = productRepository.findById(id).orElseThrow();
        ProductDto productDto = toDto(dbProduct);

        productCache.put(key, dbProduct, 60_000); // TTL = 60 seconds

        log.info("Product {} is cached ", productDto.getName());

        //clear();
        //System.gc();
        //logHeapUsage("After GC suggestion");

        return productDto;
    }

    public void createProduct(String name) {
        Product product = new Product();
        product.setName(name);
        product.setData(new byte[40 * 1024 * 1024]); //40mb
        productRepository.save(product);
    }

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        return productDto;
    }

    public void fillCache() {
        for(long i=1; i<10; i++){
            String key = "product-" + i;
            Product dbProduct = productRepository.findById(i).orElseThrow();
            productCache.put(key, dbProduct, 60_000); // TTL = 60 seconds
        }
    }

    public void clear() {
        productCache.cleanUp();
    }

    private void logHeapUsage(String message) {
        long total = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long free = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long used = total - free;

        log.info("{} -> Heap Usage: {} MB used / {} MB total", message, used, total);
    }
}
