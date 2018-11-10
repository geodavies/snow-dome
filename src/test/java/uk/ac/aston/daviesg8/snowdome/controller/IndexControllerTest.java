package uk.ac.aston.daviesg8.snowdome.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void nonLoggedInUserIsRedirectedToLogin() throws Exception {
        this.mockMvc.perform(get("/")
                .session(super.getNewMockHttpSession(false)))
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void loggedInUserIsRedirectedToLessons() throws Exception {
        this.mockMvc.perform(get("/")
                .session(super.getNewMockHttpSession(true)))
                .andExpect(redirectedUrl("/lessons"));
    }

}
