package edu.stanford.fsi.reap.service;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.fsi.reap.dto.GeoLocation;
import edu.stanford.fsi.reap.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class GoogleMapService {
  private static final String AUTOCOMPLETE_URL =
      "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

  private static final String FIND_PLACE_URL =
      "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?";

  // TODO:
  private static final String API_KEY = "";

  private final RestTemplate restTemplate;

  @Autowired
  public GoogleMapService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public JsonNode getGlaceAutocomplete(String input) {
    String url =
        UriComponentsBuilder.fromHttpUrl(AUTOCOMPLETE_URL)
            .queryParam("types", "geocode")
            .queryParam("key", API_KEY)
            .queryParam("input", input)
            .toUriString();

    try {
      return restTemplate.getForObject(url, JsonNode.class);
    } catch (Exception e) {
      log.error("Error fetching autocomplete results from Google Maps API: {}", e.getMessage());
      return null;
    }
  }

  public JsonNode findPlace(String input) {
    String url =
        UriComponentsBuilder.fromHttpUrl(FIND_PLACE_URL)
            .queryParam("inputtype", "textquery")
            .queryParam("key", API_KEY)
            .queryParam("input", input)
            .queryParam("fields", "formatted_address,name,geometry")
            .toUriString();

    try {
      return restTemplate.getForObject(url, JsonNode.class);
    } catch (Exception e) {
      log.error("Error fetching find place results from Google Maps API: {}", e.getMessage());
      throw new BadRequestAlertException("Find Place Error!");
    }
  }

  public GeoLocation getPlaceGeoLocation(String input) {
    JsonNode json = this.findPlace(input);

    int first = 0;
    try {
      JsonNode elected = json.get("candidates").get(first);
      JsonNode location = elected.get("geometry").get("location");
      return new GeoLocation(input, location.get("lat").asDouble(), location.get("lng").asDouble());
    } catch (Exception e) {
      log.error("Error fetching place geo location API: {}", e.getMessage());
      return null;
    }
  }
}
