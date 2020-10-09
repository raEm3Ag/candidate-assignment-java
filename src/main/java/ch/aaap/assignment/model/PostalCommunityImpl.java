package ch.aaap.assignment.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Builder
public class PostalCommunityImpl implements PostalCommunity {

  public final String zipCode;
  public final String zipCodeAddition;
  public final String name;

  public final List<PoliticalCommunity> politicalCommunities;

  protected PostalCommunityImpl(String zipCode, String zipCodeAddition, String name,
      List<PoliticalCommunity> politicalCommunities) {
    this.zipCode = zipCode;
    this.zipCodeAddition = zipCodeAddition;
    this.name = name;
    this.politicalCommunities = new ArrayList<>();
  }

  @Override
  public String getZipCode() {
    return zipCode;
  }

  @Override
  public String getZipCodeAddition() {
    return zipCodeAddition;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (name == null ? 0 : name.hashCode());
    result = prime * result + (zipCode == null ? 0 : zipCode.hashCode());
    result = prime * result + (zipCodeAddition == null ? 0 : zipCodeAddition.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PostalCommunityImpl other = (PostalCommunityImpl) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (zipCode == null) {
      if (other.zipCode != null) {
        return false;
      }
    } else if (!zipCode.equals(other.zipCode)) {
      return false;
    }
    if (zipCodeAddition == null) {
      if (other.zipCodeAddition != null) {
        return false;
      }
    } else if (!zipCodeAddition.equals(other.zipCodeAddition)) {
      return false;
    }
    return true;
  }

  @Override
  public List<PoliticalCommunity> getPoliticalCommunities() {
    return politicalCommunities;
  }
}
