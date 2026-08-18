package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.AddressRequest;
import com.fooddelivery.foodbackend.dto.response.AddressResponse;
import com.fooddelivery.foodbackend.entity.Address;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.exception.BadRequestException;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.AddressRepository;
import com.fooddelivery.foodbackend.security.SecurityUtils;
import com.fooddelivery.foodbackend.service.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final SecurityUtils securityUtils;

    @Override
    public AddressResponse addAddress(AddressRequest request) {
        User user = securityUtils.getCurrentUser();
        Address address = Address.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .addressType(request.getAddressType())
                .isDefault(request.getIsDefault())
                .user(user)
                .build();
        return mapToResponse(addressRepository.save(address));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAllAddresses() {
        User user = securityUtils.getCurrentUser();
        return addressRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(Long addressId) {
        User user = securityUtils.getCurrentUser();
        Address address = getAddressAndVerifyOwner(addressId, user);
        return mapToResponse(address);
    }

    @Override
    public AddressResponse updateAddress(Long addressId, AddressRequest request) {
        User user = securityUtils.getCurrentUser();
        Address address = getAddressAndVerifyOwner(addressId, user);

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setAddressType(request.getAddressType());
        if (request.getIsDefault() != null) {
            address.setIsDefault(request.getIsDefault());
        }

        return mapToResponse(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long addressId) {
        User user = securityUtils.getCurrentUser();
        Address address = getAddressAndVerifyOwner(addressId, user);
        addressRepository.delete(address);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private Address getAddressAndVerifyOwner(Long addressId, User user) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found with id : " + addressId));
        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("You can only access your own addresses.");
        }
        return address;
    }

    private AddressResponse mapToResponse(Address address) {
        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .addressType(address.getAddressType())
                .isDefault(address.getIsDefault())
                .build();
    }
}