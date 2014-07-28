package com.it.reloved.utils;

import java.util.List;

public class AddressDTO {

	private String street_number="";
	private String route="";
	private List<String> sublocality= null;
	private String locality="";
	private String administrative_area_level_2="";
	private String administrative_area_level_1="";
	private String postal_code="";
	private String country="";
	private String formatted_address="";
	/**
	 * @param street_number
	 * @param route
	 * @param sublocality
	 * @param locality
	 * @param administrative_area_level_2
	 * @param administrative_area_level_1
	 * @param postal_code
	 * @param country
	 * @param formatted_address
	 */
	public AddressDTO(String street_number, String route,
			List<String> sublocality, String locality,
			String administrative_area_level_2,
			String administrative_area_level_1, String postal_code,
			String country,String formatted_address) {
		this.street_number = street_number;
		this.route = route;
		this.sublocality = sublocality;
		this.locality = locality;
		this.administrative_area_level_2 = administrative_area_level_2;
		this.administrative_area_level_1 = administrative_area_level_1;
		this.postal_code = postal_code;
		this.country = country;
		this.formatted_address = formatted_address;
	}
	public String getStreet_number() {
		return street_number;
	}
	public void setStreet_number(String street_number) {
		this.street_number = street_number;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public List<String> getSublocality() {
		return sublocality;
	}
	public void setSublocality(List<String> sublocality) {
		this.sublocality = sublocality;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getAdministrative_area_level_2() {
		return administrative_area_level_2;
	}
	public void setAdministrative_area_level_2(String administrative_area_level_2) {
		this.administrative_area_level_2 = administrative_area_level_2;
	}
	public String getAdministrative_area_level_1() {
		return administrative_area_level_1;
	}
	public void setAdministrative_area_level_1(String administrative_area_level_1) {
		this.administrative_area_level_1 = administrative_area_level_1;
	}
	public String getPostal_code() {
		return postal_code;
	}
	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

}
