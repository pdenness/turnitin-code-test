package integrations.turnitin.com.membersearcher.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Membership(String id, @JsonProperty("user_id") String userId, String role) {
}
