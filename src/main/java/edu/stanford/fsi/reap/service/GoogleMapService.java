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

  private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json";

  private static final String FIND_PLACE_URL_V1 =
      "https://places.googleapis.com/v1/places/{placeId}";

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

  public GeoLocation geocode(String address) {
    String url =
        UriComponentsBuilder.fromHttpUrl(GEOCODING_URL)
            .queryParam("address", address)
            .queryParam("key", API_KEY)
            .toUriString();

        try {
            JsonNode json = restTemplate.getForObject(url, JsonNode.class);
            if (json != null && "OK".equals(json.path("status").asText())) {
                JsonNode location = json.path("results").get(0).path("geometry").path("location");
                double lat = location.path("lat").asDouble();
                double lng = location.path("lng").asDouble();
                return new GeoLocation(address, lat, lng);
            } else {
                String status = json != null ? json.path("status").asText() : "null response";
                log.warn("Geocoding API error for address '{}': {}", address, status);
                return null;
            }
        } catch (Exception e) {
            log.error("Exception during geocoding for address '{}': {}", address, e.getMessage());
            return null;
        }
    }

    public GeoLocation getPlaceGeoLocationByPlaceId(String placeId) {
        String url =
                UriComponentsBuilder.fromUriString(FIND_PLACE_URL_V1)
                        .queryParam("fields", "id,displayName,formattedAddress,location")
                        .queryParam("key", API_KEY)
                        .buildAndExpand(placeId)
                        .toUriString();

        try {
            JsonNode json = restTemplate.getForObject(url, JsonNode.class);
            return new GeoLocation(
                    json.get("formattedAddress").asText(),
                    json.get("location").get("latitude").asDouble(),
                    json.get("location").get("longitude").asDouble());
        } catch (Exception e) {
            log.error(
                    "Error fetching find place results by place ID from Google Maps API V1: {}",
                    e.getMessage());
            throw new BadRequestAlertException(e.toString());
        }
    }
}
