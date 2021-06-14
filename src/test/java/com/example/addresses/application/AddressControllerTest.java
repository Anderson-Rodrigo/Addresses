package com.example.addresses.application;

import com.example.addresses.domain.Address;
import com.example.addresses.domain.AddressService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.addresses.application.AddressControllerTestFixture.newMockedAddress;
import static com.example.addresses.application.AddressControllerTestFixture.newMockedAddressRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AddressControllerTest {

	private AddressController subject;
	private AddressService addressService;
	private final AddressMapper addressMapper = new AddressMapperImpl();

	@BeforeEach
	public void setUp(){
		addressService = Mockito.mock(AddressService.class);
		subject = new AddressController(addressService, addressMapper);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
	}

	@Test
	public void shouldCreateOneAddress(){
		Address address = newMockedAddress();

		given(addressService.createAddress(any(Address.class))).willReturn(address);

		ResponseEntity<?> createdAddressResponse = subject.createAddress(newMockedAddressRequest());
		Assertions.assertNotNull(createdAddressResponse);
		Assertions.assertNull(createdAddressResponse.getBody());
		Assertions.assertNotNull(createdAddressResponse.getHeaders());
		Assertions.assertEquals(HttpStatus.CREATED, createdAddressResponse.getStatusCode());

		verify(addressService).createAddress(any(Address.class));
		verifyNoMoreInteractions(addressService);
	}

	@Test
	public void shouldCreatedOneAddressWithoutLatitudeAndLongitude(){
		Address address = newMockedAddress();

		given(addressService.createAddress(any(Address.class))).willReturn(address);

		AddressRequestDto addressRequestDto = newMockedAddressRequest();
		addressRequestDto.setLatitude(null);
		addressRequestDto.setLongitude(null);

		ResponseEntity<?> createdAddressResponse = subject.createAddress(addressRequestDto);

		Assertions.assertNotNull(createdAddressResponse);
		Assertions.assertNull(createdAddressResponse.getBody());
		Assertions.assertNotNull(createdAddressResponse.getHeaders());
		Assertions.assertEquals(HttpStatus.CREATED, createdAddressResponse.getStatusCode());

		verify(addressService).createAddress(any(Address.class));
		verifyNoMoreInteractions(addressService);

	}

	@Test
	public void shouldUpdateOneAddress(){
		Optional<Address> address = Optional.of(newMockedAddress());

		given(addressService.findAddressById(any(UUID.class))).willReturn(address);
		given(addressService.updateAddress(any(Address.class))).willReturn(address.get());

		ResponseEntity<?> updateAddressResponse = subject.updateAddress(UUID.randomUUID(), newMockedAddressRequest());
		Assertions.assertNotNull(updateAddressResponse);
		Assertions.assertNull(updateAddressResponse.getBody());
		Assertions.assertEquals(HttpStatus.OK, updateAddressResponse.getStatusCode());

		verify(addressService).findAddressById(any(UUID.class));
		verify(addressService).updateAddress(address.get());
		verifyNoMoreInteractions(addressService);

	}

	@Test
	public void shouldDeleteOneAddress(){
		Optional<Address> address = Optional.of(newMockedAddress());

		given(addressService.findAddressById(any(UUID.class))).willReturn(address);
		doNothing().when(addressService).deleteAddress(address.get());

		ResponseEntity<?> deleteAddressResponse = subject.deleteAddressById(UUID.randomUUID());
		Assertions.assertNotNull(deleteAddressResponse);
		Assertions.assertNull(deleteAddressResponse.getBody());
		Assertions.assertEquals(HttpStatus.OK, deleteAddressResponse.getStatusCode());

		verify(addressService).findAddressById(any(UUID.class));
		verify(addressService).deleteAddress(address.get());
		verifyNoMoreInteractions(addressService);
	}

	@Test
	public void shouldReturnOneAddress(){
		Optional<Address> address = Optional.of(newMockedAddress());

		given(addressService.findAddressById(any(UUID.class))).willReturn(address);

		ResponseEntity<AddressResponseDto> findByIdAddressResponse = subject.findAddressById(UUID.randomUUID());
		Assertions.assertNotNull(findByIdAddressResponse);
		Assertions.assertNotNull(findByIdAddressResponse.getBody());
		Assertions.assertEquals(HttpStatus.OK, findByIdAddressResponse.getStatusCode());
		assertEqualsProperties(address.get(), findByIdAddressResponse.getBody());

		verify(addressService).findAddressById(any(UUID.class));
		verifyNoMoreInteractions(addressService);

	}

	@Test
	public void shouldReturnAllAddress(){
		List<Address> addressList = new ArrayList<>();
		addressList.add(newMockedAddress());
		addressList.add(newMockedAddress());

		given(addressService.findAllAddress()).willReturn(addressList);

		ResponseEntity<List<AddressResponseDto>> findAllAddressResponse = subject.findAllAddress();
		Assertions.assertNotNull(findAllAddressResponse);
		Assertions.assertNotNull(findAllAddressResponse.getBody());
		Assertions.assertEquals(HttpStatus.OK, findAllAddressResponse.getStatusCode());
		Assertions.assertEquals(2, findAllAddressResponse.getBody().size());

		assertEqualsProperties(addressList.get(0), findAllAddressResponse.getBody().get(0));
		assertEqualsProperties(addressList.get(1), findAllAddressResponse.getBody().get(1));

		verify(addressService).findAllAddress();
		verifyNoMoreInteractions(addressService);

	}

	@Test
	public void shouldReturnAddresssByStreetName(){
		List<Address> addressList = new ArrayList<>();
		addressList.add(newMockedAddress());
		addressList.add(newMockedAddress());

		given(addressService.findByStreetNameIgnoreCase(anyString())).willReturn(addressList);

		ResponseEntity<List<AddressResponseDto>> findAddressByStreetName = subject.findByStreetNameIgnoreCase(anyString());
		Assertions.assertNotNull(findAddressByStreetName);
		Assertions.assertNotNull(findAddressByStreetName.getBody());
		Assertions.assertEquals(HttpStatus.OK, findAddressByStreetName.getStatusCode());
		Assertions.assertEquals(2, findAddressByStreetName.getBody().size());

		assertEqualsProperties(addressList.get(0), findAddressByStreetName.getBody().get(0));
		assertEqualsProperties(addressList.get(1), findAddressByStreetName.getBody().get(1));

		verify(addressService).findByStreetNameIgnoreCase(anyString());
		verifyNoMoreInteractions(addressService);

	}

	private void assertEqualsProperties(Address expected, AddressResponseDto actual) {
		Assertions.assertEquals(expected.getStreetName(), actual.getStreetName());
		Assertions.assertEquals(expected.getNumber(), actual.getNumber());
		Assertions.assertEquals(expected.getComplement(), actual.getComplement());
		Assertions.assertEquals(expected.getNeighbourhood(), actual.getNeighbourhood());
		Assertions.assertEquals(expected.getCity(), actual.getCity());
		Assertions.assertEquals(expected.getState(), actual.getState());
		Assertions.assertEquals(expected.getCountry(), actual.getCountry());
		Assertions.assertEquals(expected.getZipcode(), actual.getZipcode());
		Assertions.assertEquals(expected.getLatitude(), actual.getLatitude());
		Assertions.assertEquals(expected.getLongitude(), actual.getLongitude());
	}

}
