package ch.aaap.assignment.model;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public class PoliticalCommunityImpl implements PoliticalCommunity {

	  public final String number;
	  public final String name;
	  public final String shortName;
	  public final LocalDate lastUpdate;
	  public final Canton canton;
	  public final District district;
	  
	public PoliticalCommunityImpl(String number, String name, String shortName, LocalDate lastUpdate, Canton canton, District district) {
		this.number = number;
		this.name = name;
		this.shortName = shortName;
		this.lastUpdate = lastUpdate;
		this.canton = canton;
		this.district = district;
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
	public String getShortName() {
		return shortName;
	}

	@Override
	public LocalDate getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public Canton getCanton() {
		return canton;
	}

	@Override
	public District getDistrict() {
		return district;
	}
}
