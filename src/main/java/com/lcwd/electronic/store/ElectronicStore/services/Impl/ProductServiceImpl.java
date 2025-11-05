package com.lcwd.electronic.store.ElectronicStore.services.Impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.exception.ResourceNotFound;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public ProductDto create(ProductDto productDto) {
        String productId= UUID.randomUUID().toString();
        productDto.setId(productId);
        productDto.setAddedDate(new Date());
        Product product=mapper.map(productDto, Product.class);
        Product saveProduct=productRepository.save(product);

        return mapper.map(saveProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product=productRepository.findById(productId).orElseThrow(()-> new ResourceNotFound("Product not found of given Id!!"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setAddedDate(productDto.getAddedDate());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());
        Product updatedProduct=productRepository.save(product);
        return mapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product=productRepository.findById(productId).orElseThrow(()-> new ResourceNotFound("Product not found of given Id!!"));
        productRepository.delete(product);
    }

    @Override
    public ProductDto getById(String productId) {
        Product product=productRepository.findById(productId).orElseThrow(()-> new ResourceNotFound("Product not found of given Id!!"));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
       Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> page=productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse= Helper.getPageableResponse(page,ProductDto.class);
        return pageableResponse;

    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String title,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByTitleContaining(title,pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFound("category not found!!"));
        String productId= UUID.randomUUID().toString();
        productDto.setId(productId);
        productDto.setAddedDate(new Date());
        Product product=mapper.map(productDto, Product.class);
        product.setCategory(category);
        Product saveProduct=productRepository.save(product);

        return mapper.map(saveProduct,ProductDto.class);
    }

    @Override
    public ProductDto updateCategoryOfProduct(String productId, String categoryId) {
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFound("product not found!!"));
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFound("category not found!!"));
        product.setCategory(category);
        Product savedProduct=productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductsOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy, String sortDir) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFound("not found!!"));
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByCategory(category,pageable);

        return Helper.getPageableResponse(page,ProductDto.class);
    }
}
