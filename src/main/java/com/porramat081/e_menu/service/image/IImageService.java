package com.porramat081.e_menu.service.image;

import com.porramat081.e_menu.dto.ImageDto;
import com.porramat081.e_menu.model.Image;
import com.porramat081.e_menu.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);

    void clearImageProduct(Long productId);

    List<ImageDto> saveImageProduct(List<MultipartFile> files, Product product);
}
