package integrations.turnitin.com.membersearcher.model;

import java.util.List;

public record UserMembershipList(List<UserMembership> memberships) {

    public List<UserMembership> memberships() {
        if (memberships != null)
            return List.copyOf(memberships);
        return List.of();
    }
}
