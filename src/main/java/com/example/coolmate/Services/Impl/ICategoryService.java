package com.example.coolmate.Services.Impl;

import com.example.coolmate.Dtos.CategoryDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO) throws DataNotFoundException;

    Category getCategoryById(int id);

    List<Category> getAllCategories(int page, int limit);

    void deleteCategory(int id) throws DataNotFoundException;

    Category updateCategory(int categoryId, CategoryDTO categoryDTO) throws DataNotFoundException;
}
