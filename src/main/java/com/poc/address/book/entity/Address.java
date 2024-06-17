package com.poc.address.book.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phonenumber;
}

