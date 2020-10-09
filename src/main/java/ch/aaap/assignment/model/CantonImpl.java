package ch.aaap.assignment.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class CantonImpl implements Canton {

	private final String code;
	private final String name;
	
	public CantonImpl(String code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Canton canton) {
		return code.compareTo(canton.getCode());
	}
}
