package ch.aaap.assignment.model;

import java.util.List;

public interface PostalCommunity {

  public String getZipCode();

  public String getZipCodeAddition();

  public String getName();

  public List<PoliticalCommunity> getPoliticalCommunities();
}
