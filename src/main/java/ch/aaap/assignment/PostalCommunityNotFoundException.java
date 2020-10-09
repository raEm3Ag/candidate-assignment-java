package ch.aaap.assignment;

public class PostalCommunityNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PostalCommunityNotFoundException(String postalCommunityNumber) {
    super("Postal community number " + postalCommunityNumber + "not found");
  }

  public PostalCommunityNotFoundException() {
    super();
  }
}
