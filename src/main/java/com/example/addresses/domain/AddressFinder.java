package com.example.addresses.domain;

public interface AddressFinder {

	AddressGeocoding findGeoCodingByDescription(String addressDescription);
}
