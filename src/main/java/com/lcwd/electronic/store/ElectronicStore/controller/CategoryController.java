package com.lcwd.electronic.store.ElectronicStore.controller;

import com.lcwd.electronic.store.ElectronicStore.dtos.*;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @Value("${category.profile.image.path}")
     String imagePath;
    //create
    @PostMapping
     public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
       CategoryDto categoryDto1=categoryService.create(categoryDto);
       return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
   }
    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable String categoryId){
        CategoryDto categoryDto1=categoryService.update(categoryDto,categoryId);
        return new ResponseEntity<>(categoryDto1,HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.delete(categoryId);
        ApiResponseMessage apiResponseMessage=ApiResponseMessage.builder().message("Category is deleted successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }
    //getAll
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll
    (@RequestParam(value = "pageNumber",defaultValue = "0",required = false)int pageNumber,
     @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
     @RequestParam(value = "sortBy",defaultValue = "title",required = false )String sortBy,
    @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir) {
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
    //getSingle
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> get(@PathVariable String categoryId){
        CategoryDto categoryDto=categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }
    //upload category image
   @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam("categoryImage")MultipartFile image, @PathVariable String categoryId) throws IOException {
        String imageName=fileService.uploadFile(image,imagePath);
        CategoryDto categoryDto=categoryService.get(categoryId);
        categoryDto.setCoverImage(imageName);
        categoryService.update(categoryDto,categoryId);
       ImageResponse imageResponse=ImageResponse.builder()
               .imageName(imageName)
               .success(true)
               .httpStatus(HttpStatus.CREATED)
               .build();
       return  new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
   }
   //serve category image
    @GetMapping("/image/{categoryId}")
    public void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto categoryDto=categoryService.get(categoryId);
        InputStream resource=fileService.getResource(imagePath,categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
    @PostMapping("/{categoryId}/product")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId,
                                                               @RequestBody ProductDto productDto){
        ProductDto productDto1=productService.createWithCategory(productDto,categoryId);
        return new ResponseEntity<>(productDto1,HttpStatus.CREATED);
    }
    //update category of product
    @PutMapping("/{productId}/products/{categoryId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId,@PathVariable String categoryId){
        ProductDto productDto=productService.updateCategoryOfProduct(productId,categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
    //get product of categories
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductOfCategory(@PathVariable String categoryId,@RequestParam(value = "pageNumber",defaultValue = "0",required = false)int pageNumber,
                                                                             @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
                                                                             @RequestParam(value = "sortBy",defaultValue = "title",required = false )String sortBy,
                                                                             @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir){
        PageableResponse<ProductDto> response=productService.getAllProductsOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
