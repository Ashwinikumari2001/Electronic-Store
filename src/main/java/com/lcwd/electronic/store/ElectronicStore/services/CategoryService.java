package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;

public interface CategoryService {
    //create
    CategoryDto create(CategoryDto categoryDto);

    //update
    CategoryDto update(CategoryDto categoryDto,String categoryId);

    //delete
    void delete(String categoryId);

    //getAll
    PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    //get single detail
    CategoryDto get(String categoryId);

    //search
}
