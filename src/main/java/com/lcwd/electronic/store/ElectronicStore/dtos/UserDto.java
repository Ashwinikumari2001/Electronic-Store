package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;
    @Size(min = 3,max=15,message = "Invalid Name!!")
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",message = "Invalid User Email!!")
    @Email(message = "Invalid Email!!")
    private String email;

    @NotBlank(message = "Passward is required!!")
    private String passward;

    @Size(min=3,max=6,message = "Invalid Gender!!")
    private String gender;

    @NotBlank(message = "write something about yourself!! ")
    private String about;

    @ImageNameValid(message = "Invalid ImageName!!")
    private String imageName;

    private List<RoleDto> roleDtoList;
}
