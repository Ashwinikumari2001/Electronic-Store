package com.lcwd.electronic.store.ElectronicStore.controller;

import com.lcwd.electronic.store.ElectronicStore.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.dtos.ImageResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user=userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable String userId){
        UserDto user=userService.updateUser(userDto,userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
        ApiResponseMessage message=ApiResponseMessage.builder()
                .message("User is deleted successfully.")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
    //getAll
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber
            ,@RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize
    ,@RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy
    ,@RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir){
        PageableResponse<UserDto> dtoList=userService.getAllUser(pageNumber,pageSize,sortBy,sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }
    //getById
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getById(@PathVariable String userId){
        UserDto user=userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    //getByEmail
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getByEmail(@PathVariable String email){
        UserDto user=userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    //searchUser
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){
        List<UserDto> userDtoList=userService.searchUser(keywords);
        return ResponseEntity.status(HttpStatus.OK).body(userDtoList);
    }

    //upload user image
    public ResponseEntity<ImageResponse>
}
