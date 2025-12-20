package com.porramat081.e_menu.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class CreatePromotionRequest {
    private String startDateTime;
    private String stopDateTime;
    private MultipartFile file;
}
