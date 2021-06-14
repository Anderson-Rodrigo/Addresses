package com.example.addresses.application;

import com.example.addresses.domain.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface AddressMapper {

	AddressResponseDto createAddressResponseDto(Address address);

	Address createAddress(AddressRequestDto addressRequestDto);

	void updateAddress(AddressRequestDto addressRequestDto, @MappingTarget Address address);

}
