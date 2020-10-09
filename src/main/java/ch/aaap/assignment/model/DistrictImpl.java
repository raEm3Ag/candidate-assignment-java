package ch.aaap.assignment.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class DistrictImpl implements District {

	private final String number;
	private final String name;
	
	public DistrictImpl(String number, String name) {
		this.number = number;
		this.name = name;
	}

	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(District district) {
		return number.compareTo(district.getNumber());
	}
}
