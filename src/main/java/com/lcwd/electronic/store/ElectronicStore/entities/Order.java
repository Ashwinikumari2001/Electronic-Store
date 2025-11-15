package com.lcwd.electronic.store.ElectronicStore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
  @Id
  private String orderId;
  //pending,dispatch,delivered
    //can use enum
  private String orderStatus;
// not-paid,paid
    //can use enum
    //can use boolean false=not-paid,true=paid
  private String paymentStatus;

  private int orderAmount;

  @Column(length = 1000)
  private String billingAddress;
  private String billingPhone;
  private String billingName;
  private Date orderDate;
  private Date deliveredDate;
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="user_id")
  private User user;
   @OneToMany(mappedBy = "order", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
  private List<OrderItem> orderItem=new ArrayList<>();





}
