package integrations.turnitin.com.membersearcher.model;

import java.util.List;

public record UserList(List<User> users) {

    public List<User> users() {
        if (users != null)
            return List.copyOf(users);
        return List.of();
    }
}
