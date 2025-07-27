package com.lcwd.electronic.store.ElectronicStore.controller;

import com.lcwd.electronic.store.ElectronicStore.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        ProductDto productDto1=productService.create(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/productId")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,@PathVariable String productId){
        ProductDto productDto1=productService.update(productDto,productId);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/productId")
    public  ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
        productService.delete(productId);
        ApiResponseMessage apiResponseMessage=ApiResponseMessage.builder().message("deleted successfully!!")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }
    //getSingleProduct
    @GetMapping("/productId")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId){
        ProductDto productDto=productService.getById(productId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
    //getAll
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                               @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
                                                               @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                               @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir){

        PageableResponse pageableResponse=productService.getAll(pageNumber,pageSize,sortBy,sortDir );
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //getAllLive
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                               @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
                                                               @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                               @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir){

        PageableResponse pageableResponse=productService.getAllLive(pageNumber,pageSize,sortBy,sortDir );
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    //Search
    @GetMapping("/search/{title}")
    public ResponseEntity<PageableResponse<ProductDto>> searchTitleContaining(@PathVariable String title,@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                   @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
                                                                   @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                                   @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir){

        PageableResponse pageableResponse=productService.searchByTitle(title,pageNumber,pageSize,sortBy,sortDir );
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
}
