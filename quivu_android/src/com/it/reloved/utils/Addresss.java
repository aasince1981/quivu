package com.it.reloved.utils;

import android.util.Log;

public class Addresss {

	private String addressline_1 = "";
	private String addressline_2 = "";

	

	public AdressLines getAddressLines(AddressDTO addressDTO) {
		AdressLines addressLines = null;
		 StringBuilder sb_1 = new StringBuilder();
		 StringBuilder sb_2 = new StringBuilder();
		if (addressDTO != null) {
			if (!addressDTO.getStreet_number().equals("")) {
				sb_1.append("" + addressDTO.getStreet_number()).append(" ");
			}
			if (!addressDTO.getRoute().equals("")) {
				sb_1.append("" + addressDTO.getRoute()).append(" ");
			}
			if (addressDTO.getSublocality().size() == 1) {
				if (!addressDTO.getSublocality().get(0).equals("")&&!addressDTO.getSublocality().get(0).equals(addressDTO.getStreet_number())) {
					sb_1.append("" + addressDTO.getSublocality().get(0))
							.append(" ");
				}
			} else if (addressDTO.getSublocality().size() == 2) {
				if (!addressDTO.getSublocality().get(1).equals("")
						&& !addressDTO.getSublocality().get(1)
								.equals(addressDTO.getSublocality().get(0))&& !addressDTO.getSublocality().get(1).equals(addressDTO.getStreet_number())) {
					sb_1.append("" + addressDTO.getSublocality().get(1))
							.append(" ");
				}
			}

			addressline_1 = "" + sb_1.toString();
			Log.v(getClass().getSimpleName(), "addressline_1="+addressline_1);
			
			if (!addressDTO.getLocality().equals("")) {
				sb_2.append("" + addressDTO.getLocality());
			}
			if (!addressDTO.getAdministrative_area_level_2().equals("")
					&& !addressDTO.getAdministrative_area_level_2().equals(
							addressDTO.getLocality())) {
				sb_2.append(" ").append("" + addressDTO.getAdministrative_area_level_2());
						
			}
			if (!addressDTO.getAdministrative_area_level_1().equals("")) {
				sb_2.append(",").append("" + addressDTO.getAdministrative_area_level_1());
					
			}
			if (!addressDTO.getPostal_code().equals("")) {
				sb_2.append(" ").append("" + addressDTO.getPostal_code());
			}
	/*		if (!addressDTO.getCountry().equals("")) {
				sb_2.append("" + addressDTO.getCountry());
			}*/
			addressline_2 = "" + sb_2.toString();
			Log.v(getClass().getSimpleName(), "addressline_2="+addressline_2);
			
			addressLines = new AdressLines(addressline_1, addressline_2);
		}

		return addressLines;
	}

	public class AdressLines {
		String addressline_1 = "";
		String addressline_2 = "";

		/**
		 * @param addressline_1
		 * @param addressline_2
		 */
		public AdressLines(String addressline_1, String addressline_2) {
			this.addressline_1 = addressline_1;
			this.addressline_2 = addressline_2;
		}

		public String getAddressline_1() {
			return addressline_1;
		}

		public void setAddressline_1(String addressline_1) {
			this.addressline_1 = addressline_1;
		}

		public String getAddressline_2() {
			return addressline_2;
		}

		public void setAddressline_2(String addressline_2) {
			this.addressline_2 = addressline_2;
		}

	}

}
