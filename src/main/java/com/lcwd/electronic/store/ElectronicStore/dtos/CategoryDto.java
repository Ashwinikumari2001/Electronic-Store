package com.lcwd.electronic.store.ElectronicStore.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @NotBlank(message = "title required!!")
    @Size(min=4,message = "Title must be of 4 character or above!!")
    private String title;

    @NotBlank(message = "Description required!!")
    private String description;

    //@NotBlank(message = "Cover image required!! ")
    private String  coverImage;
}
