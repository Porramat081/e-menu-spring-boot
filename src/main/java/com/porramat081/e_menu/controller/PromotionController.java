package com.porramat081.e_menu.controller;

import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.Promotion;
import com.porramat081.e_menu.request.CreatePromotionRequest;
import com.porramat081.e_menu.response.ApiResponse;
import com.porramat081.e_menu.service.promotion.IPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api_prefix}/promotion")
public class PromotionController {
    private final IPromotionService promotionService;

    @GetMapping("/avalible")
    public ResponseEntity<ApiResponse> getAllPromotion(){
        List<Promotion> promotions = this.promotionService.getAvaliablePromotion();
        return ResponseEntity.ok(new ApiResponse("Get Promotion Successfully",promotions));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createPromotion(@RequestBody CreatePromotionRequest createPromotionRequest){
        try {
            Promotion newPromotion =this.promotionService.createPromotion(createPromotionRequest);
            return ResponseEntity.ok(new ApiResponse("Create Promotion Successfully",newPromotion));
        } catch (RuntimeException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/{promotionId}/download")
    public ResponseEntity<Resource> getPromotionPic(@PathVariable Long promotionId) throws SQLException {
        Promotion promotion = this.promotionService.getPromotionById(promotionId);
        ByteArrayResource resource = new ByteArrayResource(promotion.getImage());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(promotion.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+promotion.getFileName()+"\"")
                    .body(resource);

    }
}
