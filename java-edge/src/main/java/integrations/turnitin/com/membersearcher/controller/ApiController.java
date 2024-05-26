package integrations.turnitin.com.membersearcher.controller;

import integrations.turnitin.com.membersearcher.model.UserMembershipList;
import integrations.turnitin.com.membersearcher.service.MembershipService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiController {

	private final MembershipService membershipService;

	public ApiController(MembershipService membershipService) {
		this.membershipService = membershipService;
	}

	@GetMapping("/course/members")
	public CompletableFuture<UserMembershipList> fetchAllMemberships() {
		return membershipService.fetchAllMembershipsWithUsers();
	}
}
