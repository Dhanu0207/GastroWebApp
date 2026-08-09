package com.fooddelivery.foodbackend.service.services;

import com.fooddelivery.foodbackend.dto.request.AddressRequest;
import com.fooddelivery.foodbackend.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(AddressRequest request);

    List<AddressResponse> getAllAddresses();

    AddressResponse getAddressById(Long addressId);

    AddressResponse updateAddress(Long addressId,
                                  AddressRequest request);

    void deleteAddress(Long addressId);

}