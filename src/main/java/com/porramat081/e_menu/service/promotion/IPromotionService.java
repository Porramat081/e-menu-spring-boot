package com.porramat081.e_menu.service.promotion;

import com.porramat081.e_menu.dto.ImageDto;
import com.porramat081.e_menu.model.Promotion;
import com.porramat081.e_menu.request.CreatePromotionRequest;

import java.util.List;

public interface IPromotionService {
    Promotion createPromotion(CreatePromotionRequest createPromotionRequest);
    List<Promotion> getAvaliablePromotion();
    Promotion getPromotionById(Long promotionId);
}
