package integrations.turnitin.com.membersearcher.service;

import integrations.turnitin.com.membersearcher.client.MembershipBackendClient;
import integrations.turnitin.com.membersearcher.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class MembershipService {
	private final Logger logger = LoggerFactory.getLogger(MembershipService.class);
	private final MembershipBackendClient membershipBackendClient;

	public MembershipService(MembershipBackendClient membershipBackendClient) {
		this.membershipBackendClient = membershipBackendClient;
	}

	/**
	 * Method to fetch all memberships with their associated user details included.
	 * This method calls out to the php-backend service and fetches all memberships,
	 * it then calls to fetch all the user details and
	 * associates them with their corresponding membership.
	 *
	 * @return A CompletableFuture containing a fully populated MembershipList object.
	 */
	public CompletableFuture<UserMembershipList> fetchAllMembershipsWithUsers() {
		return membershipBackendClient.fetchMemberships()
				.thenCompose(members -> {
					if (isMembersListNullOrEmpty(members)) {
						logger.info("No memberships found");
						return CompletableFuture.completedFuture(new UserMembershipList(List.of()));
					}
					return membershipBackendClient.fetchUsers()
							.thenApply(userList -> {
								if (isUsersListNullOrEmpty(userList)) {
									logger.warn("No users returned");
									return new UserMembershipList(List.of());
								}
								List<UserMembership> userMemberships = getUserMemberships(members, userList);
								return new UserMembershipList(userMemberships);
							});
				});
	}

	private static List<UserMembership> getUserMemberships(MembershipList members, UserList userList) {
        return members.memberships()
				.stream()
				.filter(member -> getUserById(userList, member).isPresent())
				.map(member -> {
					//noinspection OptionalGetWithoutIsPresent Done in filter
					User user = getUserById(userList, member).get();
					return new UserMembership(member.id(), member.userId(), member.role(), user);
				}).toList();
	}

	private static Optional<User> getUserById(UserList userList, Membership member) {
		return userList.users().stream()
				.filter(user -> Objects.equals(user.id(), member.userId()))
				.findFirst();
	}

	private static boolean isUsersListNullOrEmpty(UserList userList) {
		return userList.users() == null || userList.users().isEmpty();
	}

	private static boolean isMembersListNullOrEmpty(MembershipList members) {
		return members.memberships() == null || members.memberships().isEmpty();
	}
}
