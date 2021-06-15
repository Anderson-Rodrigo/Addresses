package com.example.addresses.application;

import com.example.addresses.domain.Address;
import com.example.addresses.domain.AddressRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

import static com.example.addresses.application.AddressRestTestFixture.newMockedAddress;
import static com.example.addresses.application.AddressRestTestFixture.newMockedAddressRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class AddressRestTest {

	private String url = "http://localhost:%s/addresses-api/v1/addresses/";
	private String urlComParametro = "http://localhost:%s/addresses-api/v1/addresses/%s";

	@LocalServerPort
	private int port;

	@Autowired
	private AddressRepository addressRepository;

	@AfterEach
	public void tearDown(){
		addressRepository.deleteAll();
	}

	@Test
	public void shouldCreateOneAddress(){
		given()
			.header("Content-type", "application/json")
			.and()
			.body(newMockedAddressRequest())
			.when()
			.post(String.format(url, port))
			.then()
			.statusCode(is(HttpStatus.CREATED.value()));
	}

	@Test
	public void shouldCreateOneAddressWithoutLatitudeAndLongitude(){
		AddressRequestDto addressRequestDto = newMockedAddressRequest();
		addressRequestDto.setLongitude(null);
		addressRequestDto.setLongitude(null);

		given()
			.header("Content-type", "application/json")
			.and()
			.body(addressRequestDto)
			.when()
			.post(String.format(url, port))
			.then()
			.statusCode(is(HttpStatus.CREATED.value()));
	}

	@Test
	public void shouldUpdateOneAddress(){
		UUID addressId = addressRepository.save(newMockedAddress()).getId();
		given()
			.header("Content-type", "application/json")
			.and()
			.body(newMockedAddressRequest())
			.when()
			.put(String.format(urlComParametro, port, addressId))
			.then()
			.statusCode(is(HttpStatus.OK.value()));
	}

	@Test
	public void shouldDeleteOneAddress(){
		UUID addressId = addressRepository.save(newMockedAddress()).getId();
		when()
			.delete(String.format(urlComParametro, port, addressId))
			.then()
			.statusCode(is(HttpStatus.OK.value()));

	}

	@Test
	public void shouldReturnOneAddress(){
		UUID addressId = addressRepository.save(newMockedAddress()).getId();
		when()
			.get(String.format(urlComParametro, port, addressId))
			.then()
			.statusCode(is(HttpStatus.OK.value()))
			.body(notNullValue());

	}

	@Test
	public void shouldReturnAllAddress(){
		List<Address> addressesList = new ArrayList<>();
		addressesList.add(newMockedAddress());
		addressesList.add(newMockedAddress());

		addressRepository.saveAll(addressesList);
		when()
			.get(String.format(url, port))
			.then()
			.statusCode(is(HttpStatus.OK.value()))
			.body(notNullValue());

	}

	@Test
	public void shouldReturnAddressByStreetName(){
		List<Address> addressesList = new ArrayList<>();
		addressesList.add(newMockedAddress());
		addressesList.add(newMockedAddress());

		addressRepository.saveAll(addressesList);
		when()
			.get(String.format(url.concat("search?streetName=%s"), port, addressesList.get(0).getStreetName()))
			.then()
			.statusCode(is(HttpStatus.OK.value()))
			.body(notNullValue());

	}

}
