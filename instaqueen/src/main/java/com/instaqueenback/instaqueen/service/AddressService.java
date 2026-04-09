package com.instaqueenback.instaqueen.service;

import com.instaqueenback.instaqueen.dto.request.AddressRequest;
import com.instaqueenback.instaqueen.dto.response.AddressResponse;
import com.instaqueenback.instaqueen.entity.Address;
import com.instaqueenback.instaqueen.entity.User;
import com.instaqueenback.instaqueen.exception.ResourceNotFoundException;
import com.instaqueenback.instaqueen.repository.AddressRepository;
import com.instaqueenback.instaqueen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<AddressResponse> getUserAddresses(UUID userId) {
        return addressRepository.findByUserId(userId).stream().map(AddressResponse::from).toList();
    }

    @Transactional
    public AddressResponse addAddress(UUID userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (request.isDefault()) {
            addressRepository.findByUserIdAndIsDefaultTrue(userId)
                    .ifPresent(a -> { a.setDefault(false); addressRepository.save(a); });
        }
        Address address = Address.builder()
                .user(user).label(request.getLabel()).street(request.getStreet())
                .city(request.getCity()).state(request.getState()).zipCode(request.getZipCode())
                .latitude(request.getLatitude()).longitude(request.getLongitude())
                .isDefault(request.isDefault())
                .build();
        return AddressResponse.from(addressRepository.save(address));
    }

    public AddressResponse updateAddress(UUID userId, UUID addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        address.setLabel(request.getLabel());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        return AddressResponse.from(addressRepository.save(address));
    }

    public void deleteAddress(UUID userId, UUID addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        addressRepository.delete(address);
    }

    @Transactional
    public AddressResponse setDefault(UUID userId, UUID addressId) {
        addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(a -> { a.setDefault(false); addressRepository.save(a); });
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        address.setDefault(true);
        return AddressResponse.from(addressRepository.save(address));
    }
}
