package com.porramat081.e_menu.service.category;

import com.porramat081.e_menu.model.Category;
import com.porramat081.e_menu.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategory() {
        return this.categoryRepository.findAllNonEmptyCategories();
        //return this.categoryRepository.findByNameNot("uncategory" , Sort.by(Sort.Order.asc("name").ignoreCase()));
    }
}
