package com.poc.address.book.controller;

import com.poc.address.book.entity.Address;
import com.poc.address.book.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        Optional<Address> address = addressService.getAddressById(id);
        return address.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        return addressService.saveAddress(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address addressDetails) {
        Optional<Address> address = addressService.getAddressById(id);
        if (address.isPresent()) {
            Address updatedAddress = address.get();
            updatedAddress.setName(addressDetails.getName());
            updatedAddress.setPhonenumber(addressDetails.getPhonenumber());
            return ResponseEntity.ok(addressService.saveAddress(updatedAddress));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        if (addressService.getAddressById(id).isPresent()) {
            addressService.deleteAddress(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

