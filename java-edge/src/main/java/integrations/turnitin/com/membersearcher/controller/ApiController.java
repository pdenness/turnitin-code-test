package integrations.turnitin.com.membersearcher.controller;

import integrations.turnitin.com.membersearcher.model.UserMembershipList;
import integrations.turnitin.com.membersearcher.service.MembershipService;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiController {
	private final Logger logger = LoggerFactory.getLogger(ApiController.class);
	private final MembershipService membershipService;

	public ApiController(MembershipService membershipService) {
		this.membershipService = membershipService;
	}

	@GetMapping("/course/members")
	public CompletableFuture<UserMembershipList> fetchMemberships(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email
	) {
		if (StringUtils.isBlank(name) && StringUtils.isBlank(email)) {
			logger.info("No name or email provided. Getting all memberships");
			return membershipService.fetchAllMembershipsWithUsers();
		}
		logger.info("name={} and email={} provided.", name, email);
		return membershipService.fetchMembershipsWithUsers(name, email);
	}
}
