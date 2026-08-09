package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.AddressRequest;
import com.fooddelivery.foodbackend.dto.response.AddressResponse;
import com.fooddelivery.foodbackend.entity.Address;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.AddressRepository;
import com.fooddelivery.foodbackend.repository.UserRepository;
import com.fooddelivery.foodbackend.security.SecurityUtils;
import com.fooddelivery.foodbackend.service.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;
private final SecurityUtils securityUtils;
    @Override
    public AddressResponse addAddress(AddressRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "User not found with email : " + email));
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

        Address savedAddress = addressRepository.save(address);

        return AddressResponse.builder()
                .addressId(savedAddress.getAddressId())
                .fullName(savedAddress.getFullName())
                .phoneNumber(savedAddress.getPhoneNumber())
                .addressLine1(savedAddress.getAddressLine1())
                .addressLine2(savedAddress.getAddressLine2())
                .landmark(savedAddress.getLandmark())
                .city(savedAddress.getCity())
                .state(savedAddress.getState())
                .postalCode(savedAddress.getPostalCode())
                .addressType(savedAddress.getAddressType())
                .isDefault(savedAddress.getIsDefault())
                .build();
    }
    @Override
    public List<AddressResponse> getAllAddresses() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "User not found with email : " + email));
        User user = securityUtils.getCurrentUser();
        List<Address> addresses = addressRepository.findByUser(user);

        return addresses.stream()
                .map(address -> AddressResponse.builder()
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
                        .build())
                .toList();
    }

    @Override
    public AddressResponse getAddressById(Long addressId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "User not found with email : " + email));
        User user = securityUtils.getCurrentUser();
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id : " + addressId));

        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new ResourceNotFoundException("Address not found.");
        }

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

    @Override
    public AddressResponse updateAddress(Long addressId,
                                         AddressRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "User not found with email : " + email));
        User user = securityUtils.getCurrentUser();
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id : " + addressId));

        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new ResourceNotFoundException("Address not found.");
        }

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

        Address updatedAddress = addressRepository.save(address);

        return AddressResponse.builder()
                .addressId(updatedAddress.getAddressId())
                .fullName(updatedAddress.getFullName())
                .phoneNumber(updatedAddress.getPhoneNumber())
                .addressLine1(updatedAddress.getAddressLine1())
                .addressLine2(updatedAddress.getAddressLine2())
                .landmark(updatedAddress.getLandmark())
                .city(updatedAddress.getCity())
                .state(updatedAddress.getState())
                .postalCode(updatedAddress.getPostalCode())
                .addressType(updatedAddress.getAddressType())
                .isDefault(updatedAddress.getIsDefault())
                .build();
    }

    @Override
    public void deleteAddress(Long addressId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "User not found with email : " + email));
        User user = securityUtils.getCurrentUser();
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id : " + addressId));

        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new ResourceNotFoundException("Address not found.");
        }

        addressRepository.delete(address);
    }
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email : " + email));
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