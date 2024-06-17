package com.poc.address.book.controller;

import com.poc.address.book.entity.Address;
import com.poc.address.book.service.AddressService;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AddressController.class)
@ExtendWith(MockitoExtension.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private Address address;

    @BeforeEach
    public void setUp() {
        address = new Address();
        address.setId(1L);
        address.setName("Test Name");
        address.setPhonenumber("1234567890");
    }

    @Test
    public void testGetAllAddresses() throws Exception {
        // Arrange
        when(addressService.getAllAddresses()).thenReturn(Arrays.asList(address));

        // Act
        ResultActions result = mockMvc.perform(get("/api/addresses"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Name"))
                .andExpect(jsonPath("$[0].phonenumber").value("1234567890"));
    }

    @Test
    public void testGetAddressById() throws Exception {
        // Arrange
        when(addressService.getAddressById(1L)).thenReturn(Optional.of(address));

        // Act
        ResultActions result = mockMvc.perform(get("/api/addresses/1"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Name"))
                .andExpect(jsonPath("$.phonenumber").value("1234567890"));
    }

    @Test
    public void testGetAddressById_NotFound() throws Exception {
        // Arrange
        when(addressService.getAddressById(1L)).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(get("/api/addresses/1"));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAddress() throws Exception {
        // Arrange
        when(addressService.saveAddress(any(Address.class))).thenReturn(address);

        // Act
        ResultActions result = mockMvc.perform(post("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(address)));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Name"))
                .andExpect(jsonPath("$.phonenumber").value("1234567890"));
    }

    @Test
    public void testUpdateAddress() throws Exception {
        // Arrange
        Address updatedAddress = new Address();
        updatedAddress.setId(1L);
        updatedAddress.setName("Updated Name");
        updatedAddress.setPhonenumber("0987654321");

        when(addressService.getAddressById(1L)).thenReturn(Optional.of(address));
        when(addressService.saveAddress(any(Address.class))).thenReturn(updatedAddress);

        // Act
        ResultActions result = mockMvc.perform(put("/api/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedAddress)));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.phonenumber").value("0987654321"));
    }

    @Test
    public void testUpdateAddress_NotFound() throws Exception {
        // Arrange
        when(addressService.getAddressById(1L)).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(put("/api/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(address)));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAddress() throws Exception {
        // Arrange
        when(addressService.getAddressById(1L)).thenReturn(Optional.of(address));

        // Act
        ResultActions result = mockMvc.perform(delete("/api/addresses/1"));

        // Assert
        result.andExpect(status().isOk());
    }

    @Test
    public void testDeleteAddress_NotFound() throws Exception {
        // Arrange
        when(addressService.getAddressById(1L)).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(delete("/api/addresses/1"));

        // Assert
        result.andExpect(status().isNotFound());
    }
}