package com.porramat081.e_menu.service.product;

import com.porramat081.e_menu.dto.ImageDto;
import com.porramat081.e_menu.dto.ProductDto;
import com.porramat081.e_menu.exception.AlreadyExistException;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.Category;
import com.porramat081.e_menu.model.Image;
import com.porramat081.e_menu.model.Product;
import com.porramat081.e_menu.repository.CategoryRepository;
import com.porramat081.e_menu.repository.ImageRepository;
import com.porramat081.e_menu.repository.ProductRepository;
import com.porramat081.e_menu.request.AddProductRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    private boolean productExists(String name, String category){
        System.out.println(name);
        System.out.println(category);
        boolean result = this.productRepository.existsByNameAndCategoryName(name,category);
        System.out.println(result);
        return this.productRepository.existsByNameAndCategoryName(name,category);
    }

    private Product createProduct(AddProductRequest request , Category category){
        LocalDateTime date = LocalDateTime.now();
        return new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                category,
                date
        );
    }

    private Product updateExisingProduct(Product existingProduct , AddProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setDescription(request.getDescription());

        LocalDateTime updatedDate = LocalDateTime.now();
        existingProduct.setUpdatedAt(updatedDate);
        if(request.getCategoryName() == null || request.getCategoryName().isEmpty()){
            request.setCategoryName("uncategory");
        }
        Category category = this.categoryRepository.findByName(request.getCategoryName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        if(request.getCategoryName() == null || request.getCategoryName().isEmpty()){
            request.setCategoryName("uncategory");
        }

        if(this.productExists(request.getName() , request.getCategoryName())){
            throw new AlreadyExistException(request.getName() + " already exists in "+request.getCategoryName());
        }

        Category category = Optional.ofNullable(this.categoryRepository.findByName(request.getCategoryName()))
                .orElseGet(()->{
                    Category newCategory = new Category(request.getCategoryName());
                    return this.categoryRepository.save(newCategory);
                });

        return this.productRepository.save(createProduct(request , category));

    }

    @Override
    public Product getProductById(Long id) {
        return this.productRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Product not found")
        );
    }

    @Override
    public void deletedProductById(Long id) {
        this.productRepository.findById(id).ifPresentOrElse(this.productRepository::delete,
                ()->{
                    throw new ResourceNotFoundException("Product not found");
                });
    }

    @Override
    public Product updateProduct(AddProductRequest request, Long productId) {
        return this.productRepository.findById(productId)
                .map(existingProduct -> updateExisingProduct(existingProduct,request))
                .map(this.productRepository::save)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return this.productRepository.findByCategoryNameIgnoreCase(category);
    }

    @Override
    public List<Product> getProductByName(String name){
        return this.productRepository.findByNameIgnoreCase(name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products
                .stream()
                .sorted(Comparator.comparing(Product::getCreatedAt).reversed())
                .map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        List<Image> images = this.imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = new ArrayList<>();

        for(Image image : images){
            ImageDto imageDto = new ImageDto();
            imageDto.setId(image.getId());
            imageDto.setFileName(image.getFileName());
            imageDto.setDownloadUrl(image.getDownloadUrl());

            imageDtos.add(imageDto);
        }

        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setStock(product.getStock());
        productDto.setCategory(product.getCategory());
        productDto.setImages(imageDtos);

        return productDto;
    }
}
