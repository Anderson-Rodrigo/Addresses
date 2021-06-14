package com.example.addresses.application;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class AddressRequestDto implements Serializable {

	@Size(max = 255)
	@NotEmpty
	private String streetName;

	@Size(max = 255)
	@NotEmpty
	private String number;

	@Size(max = 255)
	private String complement;

	@Size(max = 255)
	@NotEmpty
	private String neighbourhood;

	@Size(max = 255)
	@NotEmpty
	private String city;

	@Size(max = 255)
	@NotEmpty
	private String state;

	@Size(max = 255)
	@NotEmpty
	private String country;

	@Size(max = 255)
	@NotEmpty
	private String zipcode;

	private Double latitude;

	private Double longitude;

}
