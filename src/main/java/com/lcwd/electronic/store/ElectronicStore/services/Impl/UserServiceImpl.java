package com.lcwd.electronic.store.ElectronicStore.services.Impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exception.ResourceNotFound;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public UserDto createUser(UserDto userDto) {
        String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user=dtoToEntity(userDto);
        User savedUser=userRepository.save(user);
        UserDto newDto=entityToDto(savedUser);
        return newDto;
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("user not found with given id!!"));
        user.setName(userDto.getName());
        user.setPassward(userDto.getPassward());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());

        //savedata
        User updatedUser=userRepository.save(user);
        UserDto updatedDto=entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
      User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("user not found by given Id!!"));
      userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBY, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBY).descending()):(Sort.by(sortBY).ascending());
        //pageNumber default start from 0
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<User> pages=userRepository.findAll(pageable);
        PageableResponse<UserDto> response=Helper.getPageableResponse(pages,UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("user not found by given userId!!"));
        UserDto userDto=entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
     User user=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFound("Email not found!!"));
     UserDto userDto=entityToDto(user);
     return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keywords) {
        List<User> users=userRepository.findByNameContaining(keywords);
        List<UserDto> dtoList= new ArrayList<>(users.stream().map(user -> entityToDto(user)).collect(Collectors.toList()));
        return dtoList;
    }
    private UserDto entityToDto(User savedUser) {
//        return UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .passward(savedUser.getPassward())
//                .gender(savedUser.getGender())
//                .about(savedUser.getAbout())
//                .imangeName(savedUser.getImangeName())
//                .build();
        return mapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        return User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .gender(userDto.getGender())
//                .passward(userDto.getPassward())
//                .about(userDto.getAbout())
//                .imangeName(userDto.getImangeName())
//                .build();
        return mapper.map(userDto,User.class);
    }
}
