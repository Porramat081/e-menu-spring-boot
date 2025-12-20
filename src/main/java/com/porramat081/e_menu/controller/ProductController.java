package com.porramat081.e_menu.controller;

import com.porramat081.e_menu.dto.ImageDto;
import com.porramat081.e_menu.dto.ProductDto;
import com.porramat081.e_menu.exception.AlreadyExistException;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.Product;
import com.porramat081.e_menu.request.AddProductRequest;
import com.porramat081.e_menu.request.UpdateProductRequest;
import com.porramat081.e_menu.response.ApiResponse;
import com.porramat081.e_menu.service.image.IImageService;
import com.porramat081.e_menu.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api_prefix}/products")
public class ProductController {
    @Autowired
    private final IProductService productService;
    @Autowired
    private final IImageService imageService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct(){
        List<Product> products = this.productService.getAllProducts();
        List<ProductDto> convertedProducts = this.productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Get all product successfully",convertedProducts));
    }

    @GetMapping("/product/{id}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try{
            Product theProduct = this.productService.getProductById(id);
            ProductDto productDto = this.productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Get Product Successfully",productDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage() , null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/add" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> addProduct(@RequestParam(value = "files" , required = false) List<MultipartFile> files , @RequestParam("request") String request){
        try{
            JSONObject jsonObject = new JSONObject(request);
            String productName = jsonObject.getString("name");
            String productDes = jsonObject.getString("description");
            BigDecimal productPrice = jsonObject.getBigDecimal("price");
            int productStock = jsonObject.getInt("stock");
            String productCat = jsonObject.getString("categoryName");
            AddProductRequest addProductRequest = new AddProductRequest(
                    productName,
                    productDes,
                    productPrice,
                    productStock,
                    productCat
            );
            Product theProduct = this.productService.addProduct(addProductRequest);
            ProductDto productDto = this.productService.convertToDto(theProduct);
            if(files != null){
                List<ImageDto> imageDtos = this.imageService.saveImageProduct(files, theProduct);
                productDto.setImages(imageDtos);
            }
            return ResponseEntity.ok(new ApiResponse("add product successfully",productDto));
        }catch (AlreadyExistException e){
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse(e.getMessage(),null));
        }catch (Exception  e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        try{
            this.productService.deletedProductById(id);
            return ResponseEntity.ok(new ApiResponse("Delete Product Successfully",null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/product/{id}/update" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id , @RequestParam(value = "files" , required = false) List<MultipartFile> files , @RequestParam("request") String request){
        try{
            JSONObject jsonObject = new JSONObject(request);
            String productName = jsonObject.getString("name");
            String productDes = jsonObject.getString("description");
            BigDecimal productPrice = jsonObject.getBigDecimal("price");
            int productStock = jsonObject.getInt("stock");
            String productCat = jsonObject.getString("categoryName");
            AddProductRequest updateProductRequest = new AddProductRequest(
                    productName,
                    productDes,
                    productPrice,
                    productStock,
                    productCat
            );
            Product theProduct = this.productService.updateProduct(updateProductRequest,id);
            ProductDto productDto = this.productService.convertToDto(theProduct);
            this.imageService.clearImageProduct(productDto.getId());
            if(files != null){
                List<ImageDto> imageDtos = this.imageService.saveImageProduct(files, theProduct);
                productDto.setImages(imageDtos);
            }
            return ResponseEntity.ok(new ApiResponse("Update Successfully",productDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }
}
