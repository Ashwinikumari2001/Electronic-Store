package com.lcwd.electronic.store.ElectronicStore.controller;

import com.lcwd.electronic.store.ElectronicStore.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.dtos.ImageResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private Logger logger= LoggerFactory.getLogger(UserController.class);
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
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException {
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
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage")MultipartFile image,@PathVariable String userId) throws IOException {
     String imageName=fileService.uploadFile(image,imageUploadPath);
     UserDto user=userService.getUserById(userId);
     user.setImageName(imageName);
     UserDto userDto=userService.updateUser(user,userId);
         ImageResponse imageResponse=ImageResponse.builder()
                                     .imageName(imageName)
                                     .success(true)
                                     .httpStatus(HttpStatus.CREATED)
                                     .build();
         return  new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }
    //serve user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user=userService.getUserById(userId);
        logger.info("User image name: ",user.getImageName());
        InputStream resource=fileService.getResource(imageUploadPath,user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
}
