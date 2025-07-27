package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;

import java.util.List;

public interface ProductService {
    //create
    ProductDto create(ProductDto productDto);

    //update
    ProductDto update(ProductDto productDto,String productId);

    //delete
    void delete(String productId);

    //get Single
    ProductDto getById(String productId);

    //getAll
    PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    //getAll Live
    PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);

    //Search Title
    PageableResponse<ProductDto> searchByTitle(String title,int pageNumber,int pageSize,String sortBy,String sortDir);




}
