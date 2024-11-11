package com.zpi.amoz.services;

import com.zpi.amoz.dtos.CategoryDTO;
import com.zpi.amoz.dtos.CategoryTreeDTO;
import com.zpi.amoz.models.Category;
import com.zpi.amoz.repository.CategoryRepository;
import com.zpi.amoz.requests.CategoryCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public boolean deleteById(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<CategoryTreeDTO> getCategoryTree(UUID companyId) {
        List<Category> companyCategories = categoryRepository.getCategoriesWithParentHierarchy(companyId.toString());
        List<CategoryDTO> categoryDTOs = companyCategories
                .stream()
                .map(CategoryDTO::toCategoryDTO)
                .collect(Collectors.toList());

        return CategoryTreeDTO.buildCategoryTree(categoryDTOs);
    }


    @Transactional
    public Category createCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = new Category();
        category.setName(categoryCreateRequest.name());

        categoryCreateRequest.parentCategoryId()
                .map(parentId -> categoryRepository.findById(parentId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent category with given id not found: " + parentId)))
                .ifPresent(parent -> {
                    if (categoryRepository.isDirectProductCategory(parent.getCategoryId())) {
                        throw new IllegalArgumentException("Parent category cannot be direct category of any product");
                    }
                    category.setParentCategory(parent);
                    category.setCategoryLevel((short) (parent.getCategoryLevel() + 1));
                });

        return categoryRepository.save(category);
    }


    @Transactional
    public Category updateCategory(UUID categoryId, CategoryCreateRequest categoryCreateRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with given id not found: " + categoryId));

        category.setName(categoryCreateRequest.name());
        categoryCreateRequest.parentCategoryId()
                .map(parentId -> categoryRepository.findById(parentId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent category with given id not found: " + parentId)))
                .ifPresent(parent -> {
                    if (categoryRepository.isDirectProductCategory(parent.getCategoryId())) {
                        throw new IllegalArgumentException("Parent category cannot be direct category of any product");
                    }
                    category.setParentCategory(parent);
                    category.setCategoryLevel((short) (parent.getCategoryLevel() + 1));
                });


        return categoryRepository.save(category);
    }


    public void deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }
}
