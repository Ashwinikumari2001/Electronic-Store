package com.lcwd.electronic.store.ElectronicStore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotBlank(message = "cartId is required!!")
    private String cartId;

    @NotBlank(message = "userId is required!!")
    private String userID;


    private String orderStatus="PENDING";

    private String paymentStatus="NOTPAID";

    @NotBlank(message = "Address is required!!")
    private String billingAddress;

    @NotBlank(message = "Phone number is required!!")
    private String billingPhone;

    @NotBlank(message = "Name is Required!!")
    private String billingName;

}
