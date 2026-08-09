package com.fooddelivery.foodbackend.controller;

import com.fooddelivery.foodbackend.dto.request.AddressRequest;
import com.fooddelivery.foodbackend.dto.response.AddressResponse;
import com.fooddelivery.foodbackend.service.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            @Valid @RequestBody AddressRequest request) {

        return new ResponseEntity<>(
                addressService.addAddress(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {

        return ResponseEntity.ok(
                addressService.getAllAddresses()
        );
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(
            @PathVariable Long addressId) {

        return ResponseEntity.ok(
                addressService.getAddressById(addressId)
        );
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
                addressService.updateAddress(addressId, request)
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId) {

        addressService.deleteAddress(addressId);

        return ResponseEntity.noContent().build();
    }
}