package com.lcwd.electronic.store.ElectronicStore.services.Impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.exception.ResourceNotFound;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;
    @Value("${category.profile.image.path}")
    private String imagePath;
    Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        String categoryId= UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category=mapper.map(categoryDto,Category.class);
        Category savedCategory=categoryRepository.save(category);
        return  mapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        //get category by given id
        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category not found Exception!!"));

        //update details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory=categoryRepository.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);

    }

    @Override
    public void delete(String categoryId) {

        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category not found Exception!!"));
        String fullPath=imagePath+category.getCoverImage();
        try{
            Path path= Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("category image not found");
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page=categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse=Helper.getPageableResponse(page,CategoryDto.class);
        return  pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFound("Category not found Exception!!"));
        return mapper.map(category,CategoryDto.class);
    }
}
