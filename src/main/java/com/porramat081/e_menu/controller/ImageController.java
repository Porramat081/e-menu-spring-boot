package com.porramat081.e_menu.controller;

import com.porramat081.e_menu.dto.ImageDto;
import com.porramat081.e_menu.model.Image;
import com.porramat081.e_menu.model.Product;
import com.porramat081.e_menu.response.ApiResponse;
import com.porramat081.e_menu.service.image.IImageService;
import com.porramat081.e_menu.service.product.IProductService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api_prefix}/images")
public class ImageController {
    private final IImageService imageService;
    private final IProductService productService;

    @PostMapping(value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> saveProductImages(@RequestParam("files") List<MultipartFile> files , @RequestParam("productId") Long productId){
        try {
            Product theProduct = this.productService.getProductById(productId);
            List<ImageDto> imageDtos = this.imageService.saveImageProduct(files, theProduct);
            return ResponseEntity.ok(new ApiResponse("upload successfully", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("upload fail", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = this.imageService.getImageById(imageId);
        ByteArrayResource resouce = new ByteArrayResource(image.getImage());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+image.getFileName()+"\"")
                .body(resouce);
    }
}
