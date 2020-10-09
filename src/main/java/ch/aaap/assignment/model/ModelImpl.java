package ch.aaap.assignment.model;

import java.util.Set;

import lombok.Builder;

@Builder
public class ModelImpl implements Model {

	 private Set<PoliticalCommunity> politicalCommunities;

	 private  Set<PostalCommunity> postalCommunities;

	 private  Set<Canton> cantons;

	 private  Set<District> districts;


	@Override
	public Set<PoliticalCommunity> getPoliticalCommunities() {
		return politicalCommunities;
	}

	@Override
	public Set<PostalCommunity> getPostalCommunities() {
		return postalCommunities;
	}

	@Override
	public Set<Canton> getCantons() {
		return cantons;
	}

	@Override
	public Set<District> getDistricts() {
		return districts;
	}

}
