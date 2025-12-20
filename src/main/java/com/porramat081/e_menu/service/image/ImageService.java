package com.porramat081.e_menu.service.image;

import com.porramat081.e_menu.dto.ImageDto;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.Image;
import com.porramat081.e_menu.model.Product;
import com.porramat081.e_menu.repository.ImageRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;

    @Override
    public Image getImageById(Long id){
        return this.imageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Image not found"));
    }

    @Transactional
    @Override
    public void clearImageProduct(Long productId){
        System.out.println(productId);
        this.imageRepository.deleteByProductId(productId);
    }

    @Override
    public List<ImageDto> saveImageProduct(List<MultipartFile> files, Product product){

        List<ImageDto> savedImageDto = new ArrayList<>();
        for(MultipartFile file:files){
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(file.getBytes());
                image.setProduct(product);

                Image savedImage = this.imageRepository.save(image);
                String buildDownLoadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownLoadUrl + image.getId();

                image.setDownloadUrl(downloadUrl);

                this.imageRepository.save(image);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                imageDto.setFileName(savedImage.getFileName());
                savedImageDto.add(imageDto);
            }catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }
}
