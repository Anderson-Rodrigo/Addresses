package com.example.addresses.application;

import com.example.addresses.domain.Address;
import com.example.addresses.domain.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

	private final AddressService addressService;
	private final AddressMapper addressMapper;

	@PostMapping
	public ResponseEntity<?> createAddress(@Valid @RequestBody AddressRequestDto content){
		Address address = addressMapper.createAddress(content);
		address = addressService.createAddress(address);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(address.getId())
				.toUri();

		return ResponseEntity.ok(address.getId());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAddressById(@PathVariable UUID id){
		Optional<Address> address = addressService.findAddressById(id);
		if(!address.isPresent()){
			return ResponseEntity.notFound().build();
		}
		addressService.deleteAddress(address.get());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateAddress(@PathVariable UUID id, @Valid @RequestBody AddressRequestDto content){
		Optional<Address> address = addressService.findAddressById(id);
		if(!address.isPresent()){
			return ResponseEntity.notFound().build();
		}
		addressMapper.updateAddress(content, address.get());
		addressService.updateAddress(address.get());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<AddressResponseDto> findAddressById(@PathVariable UUID id){
		return addressService
					.findAddressById(id)
					.map(address -> ResponseEntity.ok(addressMapper.createAddressResponseDto(address)))
					.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<AddressResponseDto>> findAllAddress(){
		List<AddressResponseDto> responseDtoList = addressService
				.findAllAddress()
				.stream()
				.map(addressMapper::createAddressResponseDto)
				.collect(Collectors.toList());
		if(responseDtoList.isEmpty()){
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(responseDtoList);
	}

	@GetMapping("/search")
	public ResponseEntity<List<AddressResponseDto>> findByStreetNameIgnoreCase(@RequestParam(value = "streetName") String streetName){
		List<AddressResponseDto> responseDtoList = addressService
				.findByStreetNameIgnoreCase(streetName)
				.stream()
				.map(addressMapper::createAddressResponseDto)
				.collect(Collectors.toList());
		if(responseDtoList.isEmpty()){
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(responseDtoList);
	}

}
