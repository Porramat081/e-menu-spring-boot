package com.porramat081.e_menu.service.product;

import com.porramat081.e_menu.dto.ProductDto;
import com.porramat081.e_menu.model.Product;
import com.porramat081.e_menu.request.AddProductRequest;
import com.porramat081.e_menu.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    Product getProductById(Long id);
    void deletedProductById(Long id);

    Product updateProduct(AddProductRequest request, Long productId);

    List<Product> getAllProducts();
    List<Product> getProductByCategory(String category);

    List<Product> getProductByName(String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
