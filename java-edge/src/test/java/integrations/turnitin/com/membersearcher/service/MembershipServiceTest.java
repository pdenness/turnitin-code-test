package integrations.turnitin.com.membersearcher.service;

import integrations.turnitin.com.membersearcher.client.MembershipBackendClient;
import integrations.turnitin.com.membersearcher.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {
    @InjectMocks
    private MembershipService membershipService;

    @Mock
    private MembershipBackendClient membershipBackendClient;

    private User userOne;

    private User userTwo;

    private UserList userList;

    @BeforeEach
    public void setup() {
        MembershipList members = new MembershipList(List.of(
                new Membership("a", "00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c", "instructor"),
                new Membership("b", "f57975d2-e6ae-4f4a-aada-ee6cdcede0d1", "student")
        ));
        userOne = new User("00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c", "test one", "test1@example.com");
        userTwo = new User("f57975d2-e6ae-4f4a-aada-ee6cdcede0d1", "test two", "test2@example.com");
        userList = new UserList(List.of(userOne, userTwo));
        when(membershipBackendClient.fetchMemberships()).thenReturn(CompletableFuture.completedFuture(members));
    }

    @Test
    public void testFetchAllMemberships() throws Exception {
        when(membershipBackendClient.fetchUsers()).thenReturn(CompletableFuture.completedFuture(userList));
        UserMembershipList members = membershipService.fetchAllMembershipsWithUsers().get();

        verify(membershipBackendClient, times(1)).fetchUsers();
        verify(membershipBackendClient, times(0)).fetchUser(anyString());
        assertThat(members.memberships().get(0).user()).isEqualTo(userOne);
        assertThat(members.memberships().get(1).user()).isEqualTo(userTwo);
    }

    @Test
    public void testNoFetchUsersWhenMembershipsIsNull() throws Exception {
        when(membershipBackendClient.fetchMemberships())
                .thenReturn(CompletableFuture.completedFuture(new MembershipList(null)));

        membershipService.fetchAllMembershipsWithUsers().get();
        verify(membershipBackendClient, times(0)).fetchUsers();
    }

    @Test
    public void testNoFetchUsersWhenMembersEmpty() throws Exception {
        when(membershipBackendClient.fetchMemberships())
                .thenReturn(CompletableFuture.completedFuture(new MembershipList(List.of())));

        membershipService.fetchAllMembershipsWithUsers().get();
        verify(membershipBackendClient, times(0)).fetchUsers();
    }

    @Test
    public void testReturnsEmptyMembershipListWhenUsersListNull() throws Exception {
        UserList userList = new UserList(null);
        when(membershipBackendClient.fetchUsers()).thenReturn(CompletableFuture.completedFuture(userList));
        UserMembershipList members = membershipService.fetchAllMembershipsWithUsers().get();

        assertTrue(members.memberships().isEmpty());
    }

    @Test
    public void testReturnsMembershipListWhenUserIdNotFound() throws Exception {
        UserList userList = new UserList(List.of());
        when(membershipBackendClient.fetchUsers()).thenReturn(CompletableFuture.completedFuture(userList));
        UserMembershipList members = membershipService.fetchAllMembershipsWithUsers().get();

        assertTrue(members.memberships().isEmpty());
    }

    @Test
    public void testFetchMembershipsWhenUserIdIsNull() throws Exception {
        User user = new User(null, "test", "test@example.com");
        UserList userList = new UserList(List.of(user));
        when(membershipBackendClient.fetchUsers()).thenReturn(CompletableFuture.completedFuture(userList));
        UserMembershipList members = membershipService.fetchAllMembershipsWithUsers().get();
        assertTrue(members.memberships().isEmpty());
    }

    @Test
    public void testFetchMembershipsWhenMemberUserIdIsNull() throws Exception {
        MembershipList apimembers = new MembershipList(List.of(
                new Membership("a", null, "instructor")
        ));
        when(membershipBackendClient.fetchUsers()).thenReturn(CompletableFuture.completedFuture(userList));
        when(membershipBackendClient.fetchMemberships()).thenReturn(CompletableFuture.completedFuture(apimembers));
        UserMembershipList members = membershipService.fetchAllMembershipsWithUsers().get();
        assertTrue(members.memberships().isEmpty());
    }
}
