package com.example.addresses.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.addresses.domain.AddressServiceTestFixture.newMockedAddress;
import static com.example.addresses.domain.AddressServiceTestFixture.newMockedAddressGeocoding;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

	private AddressService subject;
	private AddressRepository addressRepository;
	private AddressFinder addressFinder;

	@BeforeEach
	public void setUp(){
		addressRepository = Mockito.mock(AddressRepository.class);
		addressFinder = Mockito.mock(AddressFinder.class);
		subject = new AddressService(addressRepository, addressFinder);
	}

	@Test
	public void shouldCreateOneAddress(){
		Address address = newMockedAddress();

		given(addressRepository.save(any(Address.class))).willReturn(address);
		Address createdAddress = subject.createAddress(address);

		Assertions.assertNotNull(createdAddress);
		assertEqualsProperties(address, createdAddress);

		verify(addressRepository).save(any(Address.class));
		verifyNoMoreInteractions(addressRepository);
	}

	@Test
	public void shouldCreateOneAddressWithoutLatitudeAndLongitude(){
		AddressGeocoding geocoding = newMockedAddressGeocoding();

		Address address = newMockedAddress();
		address.setLongitude(null);
		address.setLatitude(null);

		given(addressRepository.save(any(Address.class))).willReturn(address);
		given(addressFinder.findGeoCodingByDescription(anyString())).willReturn(geocoding);

		Address createdAddress = subject.createAddress(address);
		Assertions.assertNotNull(createdAddress);
		assertEqualsProperties(address, createdAddress);

		verify(addressRepository).save(any(Address.class));
		verify(addressFinder).findGeoCodingByDescription(anyString());

		verifyNoMoreInteractions(addressRepository);
		verifyNoMoreInteractions(addressFinder);
	}

//	@Test
//	public void shouldUpdateOnAddress(){
//		Address address = newMockedAddress();
//
//		given(addressRepository.save(any(Address.class))).willReturn(address);
//		Address createdAddress = subject.createAddress(address);
//		Assertions.assertNotNull(createdAddress);
//
//		createdAddress.setStreetName("TESTE");
//		createdAddress.setCountry("TESTE PAIS");
//		createdAddress.setState("TESTE STADO");
//		createdAddress.setCity("TESTE CIDADE");
//		createdAddress.setComplement("TESTE BAIRRO");
//		createdAddress.setNeighbourhood("TESTE COMPLEMENTO");
//
//		Address updatedAddress = subject.updateAddress(createdAddress);
//		Assertions.assertNotNull(updatedAddress);
//		assertEqualsProperties(createdAddress, updatedAddress);
//
//		verify(addressRepository).save(any(Address.class));
//		verifyNoMoreInteractions(addressRepository);
//
//
//	}

	@Test
	public void shouldUpdateOnAddres(){
		Address address = newMockedAddress();

		given(addressRepository.save(any(Address.class))).willReturn(address);

		Address updatedAddress = subject.updateAddress(address);
		Assertions.assertNotNull(updatedAddress);

		assertEqualsProperties(address, updatedAddress);

		verify(addressRepository).save(any(Address.class));
		verifyNoMoreInteractions(addressRepository);

	}

	@Test
	public void shouldDeleteOnAddress(){
		doNothing().when(addressRepository).delete(any(Address.class));

		subject.deleteAddress(newMockedAddress());

		verify(addressRepository).delete(any(Address.class));
		verifyNoMoreInteractions(addressRepository);
	}

	@Test
	public void shouldReturnOneAddress(){
		Optional<Address> address = Optional.of(newMockedAddress());

		given(addressRepository.findById(any(UUID.class))).willReturn(address);

		Optional<Address> findAddressById = subject.findAddressById(UUID.randomUUID());
		Assertions.assertTrue(findAddressById.isPresent());

		assertEqualsProperties(address.get(), findAddressById.get());

		verify(addressRepository).findById(any(UUID.class));
		verifyNoMoreInteractions(addressRepository);

	}

	@Test
	public void shouldReturnAllAddress(){
		List<Address> addressList = new ArrayList<>();
		addressList.add(newMockedAddress());
		addressList.add(newMockedAddress());

		given(addressRepository.findAll()).willReturn(addressList);

		List<Address> findAllAddresses = subject.findAllAddress();
		Assertions.assertNotNull(findAllAddresses);
		Assertions.assertEquals(2, findAllAddresses.size());

		assertEqualsProperties(addressList.get(0), findAllAddresses.get(0));
		assertEqualsProperties(addressList.get(1), findAllAddresses.get(1));

		verify(addressRepository).findAll();
		verifyNoMoreInteractions(addressRepository);
	}

	@Test
	public void shouldReturnAddressByStreetName(){
		List<Address> addressList = new ArrayList<>();
		addressList.add(newMockedAddress());
		addressList.add(newMockedAddress());

		given(addressRepository.findByStreetNameIgnoreCase(anyString())).willReturn(addressList);

		List<Address> findAllAddressesByStreetName = subject.findByStreetNameIgnoreCase(anyString());
		Assertions.assertNotNull(findAllAddressesByStreetName);
		Assertions.assertEquals(2, findAllAddressesByStreetName.size());

		assertEqualsProperties(addressList.get(0), findAllAddressesByStreetName.get(0));
		assertEqualsProperties(addressList.get(1), findAllAddressesByStreetName.get(1));

		verify(addressRepository).findByStreetNameIgnoreCase(anyString());
		verifyNoMoreInteractions(addressRepository);
	}

	private void assertEqualsProperties(Address expected, Address actual) {
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
