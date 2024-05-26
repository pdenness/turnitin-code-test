package integrations.turnitin.com.membersearcher.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserMembershipListTest {

    @Test
    public void testModifyingUserMembershipListHandlesNull() {
        UserMembershipList members = new UserMembershipList(null);

        assertThrows(UnsupportedOperationException.class,
                () -> members.memberships().set(0, new UserMembership(null, null, null, null)));
    }

    @Test
    public void testModifyingUserMembershipListThrowsUnsupportedOperationException() {
        List<UserMembership> users = new ArrayList<>();
        users.add(new UserMembership("a", "00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c", "student", new User("00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c", "test", "test@gmail.com")));
        UserMembershipList members = new UserMembershipList(users);

        assertThrows(UnsupportedOperationException.class,
                () -> members.memberships().set(0, new UserMembership(null, null, null, null)));
    }
}