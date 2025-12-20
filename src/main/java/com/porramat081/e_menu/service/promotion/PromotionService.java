package com.porramat081.e_menu.service.promotion;

import com.porramat081.e_menu.dto.ImageDto;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.Promotion;
import com.porramat081.e_menu.repository.PromotionRepository;
import com.porramat081.e_menu.request.CreatePromotionRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class PromotionService implements IPromotionService{
    private final PromotionRepository promotionRepository;
    @Override
    public Promotion createPromotion(CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = new Promotion();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        LocalDateTime startDate = LocalDateTime.parse(createPromotionRequest.getStartDateTime(), formatter);
        LocalDateTime stopDate = LocalDateTime.parse(createPromotionRequest.getStopDateTime(),formatter);

        Promotion inputPromotion = this.promotionRepository.save(promotion);

        String downloadUrl = "/api/v1/promotion/"+inputPromotion.getId()+"/download";

        try{
            promotion.setImage(createPromotionRequest.getFile().getBytes());
            promotion.setStartDate(startDate);
            promotion.setStopDate(stopDate);
            promotion.setDownloadUrl(downloadUrl);
            promotion.setFileName(createPromotionRequest.getFile().getOriginalFilename());
            promotion.setFileType(createPromotionRequest.getFile().getContentType());
            return this.promotionRepository.save(promotion);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Promotion> getAvaliablePromotion() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return this.promotionRepository.findByStartDateLessThanEqualAndStopDateGreaterThanEqual(today,today);
    }

    @Override
    public Promotion getPromotionById(Long promotionId){
        return this.promotionRepository.findById(promotionId)
                .orElseThrow(()->new ResourceNotFoundException("Promotion not found"));

    }
}
