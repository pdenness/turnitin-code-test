package integrations.turnitin.com.membersearcher.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MembershipListTest {

    @Test
    public void testModifyingMemberShipListHandlesNull() {
        MembershipList members = new MembershipList(null);

        assertThrows(UnsupportedOperationException.class,
                () -> members.memberships().set(0, new Membership(null, null, null)));
    }

    @Test
    public void testModifyingMemberShipListThrowsUnsupportedOperationException() {
        List<Membership> users = new ArrayList<>();
        users.add(new Membership("a", "00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c", "student"));
        MembershipList members = new MembershipList(users);

        assertThrows(UnsupportedOperationException.class,
                () -> members.memberships().set(0, new Membership(null, null, null)));
    }
}