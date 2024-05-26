package integrations.turnitin.com.membersearcher.controller;

import integrations.turnitin.com.membersearcher.model.User;
import integrations.turnitin.com.membersearcher.model.UserMembership;
import integrations.turnitin.com.membersearcher.model.UserMembershipList;
import integrations.turnitin.com.membersearcher.service.MembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MembershipService membershipService;

    @BeforeEach
    public void setUp() {
        UserMembershipList members = new UserMembershipList(List.of(
                new UserMembership("a",  "00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c", "instructor",
                        new User("00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c","test one","test1@example.com")),
                new UserMembership("b", "f57975d2-e6ae-4f4a-aada-ee6cdcede0d1", "student",
                        new User("f57975d2-e6ae-4f4a-aada-ee6cdcede0d1", "test two", "test2@example.com"))
        ));
        when(membershipService.fetchAllMembershipsWithUsers()).thenReturn(CompletableFuture.completedFuture(members));
    }

    @Test
    public void testMembershipsEndpointReturnsMemberships() throws Exception {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/course/members");

        final MvcResult result = mvc.perform(request).andReturn();
        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberships").isNotEmpty())
                .andExpect(jsonPath("$.memberships", hasSize(2)))
                .andExpect(jsonPath("$.memberships[0].user.name").value("test one"))
                .andExpect(jsonPath("$.memberships[0].user.email").value("test1@example.com"))
                .andExpect(jsonPath("$.memberships[0].user.id").value("00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c"))
                .andExpect(jsonPath("$.memberships[0].id").value("a"))
                .andExpect(jsonPath("$.memberships[0].userId").value("00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c"))
                .andExpect(jsonPath("$.memberships[0].role").value("instructor"));
    }

    @Test
    public void testMembershipsEndpointHandlesQueryParams() throws Exception {
        UserMembershipList members = new UserMembershipList(List.of(
                new UserMembership("a",  "00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c", "instructor",
                        new User("00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c","test one","test1@example.com"))
        ));
        when(membershipService.fetchMembershipsWithUsers("test", "test@gmail.com")).thenReturn(CompletableFuture.completedFuture(members));

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/course/members")
                .queryParam("name", "test")
                .queryParam("email", "test@gmail.com");
        final MvcResult result = mvc.perform(request).andReturn();
        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberships").isNotEmpty())
                .andExpect(jsonPath("$.memberships", hasSize(1)))
                .andExpect(jsonPath("$.memberships[0].user.name").value("test one"))
                .andExpect(jsonPath("$.memberships[0].user.email").value("test1@example.com"));
    }
}
