package com.stackroute.keepnote.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Service classes are used here to implement additional business logic/validation
 * This class has to be annotated with @Service annotation.
 * @Service - It is a specialization of the component annotation. It doesn't currently
 * provide any additional behavior over the @Component annotation, but it's a good idea
 * to use @Service over @Component in service-layer classes because it specifies intent
 * better. Additionally, tool support and additional behavior might rely on it in the
 * future.
 * */
@Service
public class CategoryServiceImpl implements CategoryService {

    /*
     * Autowiring should be implemented for the CategoryRepository. (Use
     * Constructor-based autowiring) Please note that we should not create any
     * object using the new keyword.
     */
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /*
     * This method should be used to save a new category.Call the corresponding
     * method of Respository interface.
     */
    public Category createCategory(Category category) throws CategoryNotCreatedException {
        Category createdCategory = null;
        category.setCategoryCreationDate(new Date());
        Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
        if (optionalCategory.isPresent()) {
            throw new CategoryNotCreatedException("Category already exists");
        } else {
            createdCategory = categoryRepository.insert(category);
            if (createdCategory == null) {
                throw new CategoryNotCreatedException("Category already exists");
            }
            return category;
        }
    }

    /*
     * This method should be used to delete an existing category.Call the
     * corresponding method of Respository interface.
     */
    public boolean deleteCategory(String categoryId) throws CategoryDoesNoteExistsException {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            categoryRepository.deleteById(categoryId);
            return true;
        } else {
            throw new CategoryDoesNoteExistsException("Category does not exist");
        }
    }

    /*
     * This method should be used to update a existing category.Call the
     * corresponding method of Respository interface.
     */
    public Category updateCategory(Category category, String categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            category.setCategoryCreationDate(optionalCategory.get().getCategoryCreationDate());
            categoryRepository.save(category);
            return category;
        } else {
            return null;
        }
    }

    /*
     * This method should be used to get a category by categoryId.Call the
     * corresponding method of Respository interface.
     */
    public Category getCategoryById(String categoryId) throws CategoryNotFoundException {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (optionalCategory.isPresent()) {
                return optionalCategory.get();

            } else {
                throw new CategoryNotFoundException("Category not found");
            }
        } catch (NoSuchElementException e) {
            throw new CategoryNotFoundException("Category not found");
        }
    }

    /*
     * This method should be used to get a category by userId.Call the corresponding
     * method of Respository interface.
     */
    public List<Category> getAllCategoryByUserId(String userId) {
        return categoryRepository.findAllCategoryByCategoryCreatedBy(userId);
    }

}
