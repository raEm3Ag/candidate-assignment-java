package ch.aaap.assignment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import ch.aaap.assignment.model.Canton;
import ch.aaap.assignment.model.CantonImpl;
import ch.aaap.assignment.model.District;
import ch.aaap.assignment.model.DistrictImpl;
import ch.aaap.assignment.model.Model;
import ch.aaap.assignment.model.ModelImpl;
import ch.aaap.assignment.model.PoliticalCommunity;
import ch.aaap.assignment.model.PoliticalCommunityImpl;
import ch.aaap.assignment.model.PostalCommunity;
import ch.aaap.assignment.model.PostalCommunityImpl;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import ch.aaap.assignment.raw.CSVUtil;

public class Application {

  private Model model = null;

  public Application() {
    initModel();
  }

  public static void main(String[] args) {
    new Application();
  }

  /** Reads the CSVs and initializes a in memory model */
  private void initModel() {
    final Set<CSVPoliticalCommunity> csvPoliticalCommunities = CSVUtil.getPoliticalCommunities();
    final Set<CSVPostalCommunity> csvPostalCommunities = CSVUtil.getPostalCommunities();

    final Map<String, Canton> cantons = new HashMap<>();
    final Map<String, District> districts = new HashMap<>();
    final Map<String, PoliticalCommunity> politicalCommunities = new HashMap<>();
    final List<PostalCommunity> postalCommunities = new ArrayList<>();

    csvPoliticalCommunities.forEach(politicalCommunity -> {
      if (!politicalCommunities.containsKey(politicalCommunity.getNumber())) {
        politicalCommunities.put(politicalCommunity.getNumber(),
            PoliticalCommunityImpl.builder().lastUpdate(politicalCommunity.getLastUpdate())
                .name(politicalCommunity.getName()).number(politicalCommunity.getNumber())
                .shortName(politicalCommunity.getShortName())
                .canton(getCanton(politicalCommunity, cantons))
                .district(getDistrict(politicalCommunity, districts)).build());
      }
    });

    csvPostalCommunities.forEach(csvPostalCommunity -> {
      final PostalCommunity postalCommunity = PostalCommunityImpl.builder()
          .name(csvPostalCommunity.getName()).zipCode(csvPostalCommunity.getZipCode())
          .zipCodeAddition(csvPostalCommunity.getZipCodeAddition()).build();
      if (!postalCommunities.contains(postalCommunity)) {
        postalCommunities.add(postalCommunity);
      }

      final Optional<PostalCommunity> postalCommunityOptional =
          postalCommunities.stream().filter(postal -> postal.equals(postalCommunity)).findAny();

      final PoliticalCommunity politicalCommunity =
          politicalCommunities.get(csvPostalCommunity.getPoliticalCommunityNumber());

      postalCommunityOptional.get().getPoliticalCommunities().add(politicalCommunity);
    });


    model = ModelImpl.builder().postalCommunities(new HashSet<>(postalCommunities))
        .politicalCommunities(new HashSet<>(politicalCommunities.values()))
        .cantons(new HashSet<>(cantons.values())).districts(new HashSet<>(districts.values()))
        .build();
  }

  private Canton getCanton(CSVPoliticalCommunity community, Map<String, Canton> cantons) {

    Canton canton = cantons.get(community.getCantonCode());

    if (canton != null) {
      return canton;
    } else {
      canton = CantonImpl.builder().name(community.getCantonName()).code(community.getCantonCode())
          .build();

      cantons.put(canton.getCode(), canton);
      return canton;
    }

  }

  private District getDistrict(CSVPoliticalCommunity community, Map<String, District> districts) {

    District district = districts.get(community.getDistrictNumber());

    if (district != null) {
      return district;
    } else {
      district = DistrictImpl.builder().name(community.getDistrictName())
          .number(community.getDistrictNumber()).build();

      districts.put(district.getNumber(), district);
      return district;
    }

  }

  /** @return model */
  public Model getModel() {
    return model;
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of political communities in given canton
   */
  public long getAmountOfPoliticalCommunitiesInCanton(String cantonCode) {

    final Canton canton = findCantonByCode(cantonCode);

    return model.getPoliticalCommunities().stream()
        .filter(community -> community.getCanton().equals(canton)).count();
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of districts in given canton
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) {

    final Canton canton = findCantonByCode(cantonCode);


    return model.getPoliticalCommunities().stream()
        .filter(politicalCommunity -> politicalCommunity.getCanton().equals(canton))
        .collect(Collectors.groupingBy(politicalCommunity -> politicalCommunity.getDistrict(),
            Collectors.counting()))
        .size();
  }

  private Canton findCantonByCode(String cantonCode) {
    return model.getCantons().stream().filter(c -> c.getCode().equals(cantonCode)).findAny()
        .orElseThrow(IllegalArgumentException::new);
  }

  /**
   * @param districtNumber of a district (e.g. 101)
   * @return amount of districts in given canton
   */
  public long getAmountOfPoliticalCommunitiesInDistrict(String districtNumber) {

    final District district =
        model.getDistricts().stream().filter(d -> d.getNumber().equals(districtNumber)).findAny()
            .orElseThrow(IllegalArgumentException::new);

    return model.getPoliticalCommunities().stream()
        .filter(community -> community.getDistrict().equals(district)).count();
  }

  /**
   * @param zipCode 4 digit zip code
   * @return district that belongs to specified zip code
   */
  public Set<String> getDistrictsForZipCode(String zipCode) {

    final List<PostalCommunity> postalCommunities = model.getPostalCommunities().stream()
        .filter(postalCommunity -> postalCommunity.getZipCode().equals(zipCode))
        .collect(Collectors.toList());

    return postalCommunities.stream()
        .flatMap(postalCommunity -> model.getPoliticalCommunities().stream()
            .filter(politicalCommunity -> postalCommunity.getPoliticalCommunities()
                .contains(politicalCommunity))
            .map(politicalCommunity -> politicalCommunity.getDistrict().getName()))
        .collect(Collectors.toSet());
  }

  /**
   * @param postalCommunityName name
   * @return lastUpdate of the political community by a given postal community name
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(
      String postalCommunityName) {

    final Optional<PostalCommunity> postalCommunityOptional = model.getPostalCommunities().stream()
        .filter(postalCommunity -> postalCommunity.getName().equals(postalCommunityName)).findAny();

    return postalCommunityOptional
        .map(postalCommunity -> model.getPoliticalCommunities().stream()
            .filter(politicalCommunity -> postalCommunity.getPoliticalCommunities()
                .contains(politicalCommunity))
            .map(politicalCommunity -> politicalCommunity.getLastUpdate()).findAny()
            .orElseThrow(PostalCommunityNotFoundException::new))
        .orElseThrow(PostalCommunityNotFoundException::new);
  }

  /**
   * https://de.wikipedia.org/wiki/Kanton_(Schweiz)
   *
   * @return amount of canton
   */
  public long getAmountOfCantons() {
    return model.getCantons().size();
  }

  /**
   * https://de.wikipedia.org/wiki/Kommunanz
   *
   * @return amount of political communities without postal communities
   */
  public long getAmountOfPoliticalCommunityWithoutPostalCommunities() {
    return model.getPoliticalCommunities().stream()
        .filter(politicalCommunity -> !existsPostalCommunity(politicalCommunity)).count();
  }

  private boolean existsPostalCommunity(PoliticalCommunity politicalCommunity) {
    return model.getPostalCommunities().stream().anyMatch(
        postalCommunity -> postalCommunity.getPoliticalCommunities().contains(politicalCommunity));
  }
}
