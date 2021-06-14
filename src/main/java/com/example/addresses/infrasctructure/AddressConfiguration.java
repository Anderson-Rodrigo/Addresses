package com.example.addresses.infrasctructure;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressConfiguration {

	@Value("${google.geocoding.api-key}")
	private String googleGoApiKey;

	@Bean
	public GeoApiContext getGeoApiContext(){
		return new GeoApiContext.Builder().apiKey(googleGoApiKey).build();
	}

}
