package com.example.addresses.infrasctructure;

import com.example.addresses.domain.AddressFinder;
import com.example.addresses.domain.AddressGeocoding;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AddressGoogleMapsAdapter implements AddressFinder {

	private final GeoApiContext geoApiContext;

	@Override
	@SneakyThrows
	public AddressGeocoding findGeoCodingByDescription(String addressDescription) {
		GeocodingResult[] results = GeocodingApi.newRequest(geoApiContext).address(addressDescription).await();
		if(results.length > 0){
			LatLng location = results[0].geometry.location;
			return new AddressGeocoding(location.lat, location.lng);
		}
		return null;
	}

}
