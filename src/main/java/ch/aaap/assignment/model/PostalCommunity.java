package ch.aaap.assignment.model;

import java.util.List;

public interface PostalCommunity {

  String getZipCode();

  String getZipCodeAddition();

  String getName();

  List<PoliticalCommunity> getPoliticalCommunities();
}
