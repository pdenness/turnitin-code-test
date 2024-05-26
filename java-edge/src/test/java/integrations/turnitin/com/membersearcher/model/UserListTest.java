package integrations.turnitin.com.membersearcher.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserListTest {

    @Test
    public void testModifyingUserListHandlesNull() {
        UserList members = new UserList(null);

        assertThrows(UnsupportedOperationException.class,
                () -> members.users().set(0, new User(null, null, null)));
    }

    @Test
    public void testModifyingUserListThrowsUnsupportedOperationException() {
        List<User> users = new ArrayList<>();
        users.add(new User("00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c", "test", "test@gmail.com"));
        UserList members = new UserList(users);

        assertThrows(UnsupportedOperationException.class,
                () -> members.users().set(0, new User(null, null, null)));
    }
}