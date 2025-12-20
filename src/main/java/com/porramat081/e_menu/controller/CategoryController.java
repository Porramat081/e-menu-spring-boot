package com.porramat081.e_menu.controller;

import com.porramat081.e_menu.model.Category;
import com.porramat081.e_menu.response.ApiResponse;
import com.porramat081.e_menu.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api_prefix}/category")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategory(){
        List<Category> categories = this.categoryService.getAllCategory();

        return ResponseEntity.ok(new ApiResponse("get all categories successfully",categories));
    }
}
