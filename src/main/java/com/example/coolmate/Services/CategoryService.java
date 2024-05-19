package com.example.coolmate.Services;

import com.example.coolmate.Dtos.CategoryDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Category;
import com.example.coolmate.Repositories.CategoryRepository;
import com.example.coolmate.Services.Impl.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) throws DataNotFoundException {

        Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.getName());

        if (existingCategory.isPresent()) {
            throw new DataNotFoundException("Category đã tồn tại: " + categoryDTO.getName());
        } else {
            // Create a new Category entity
            Category newCategory = Category.builder()
                    .name(categoryDTO.getName())
                    .build();
            return categoryRepository.save(newCategory);
        }
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(int id) throws DataNotFoundException {
        if (!categoryRepository.existsById(id)) {
            throw new DataNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category updateCategory(int categoryId, CategoryDTO categoryDTO) throws DataNotFoundException {
        Category existingCategory = getCategoryById(categoryId);

      
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory); //lưu data mới
        return existingCategory;
    }


}
