package com.augusto.test.web.controller;

import com.augusto.test.app.AppConfiguration;
import com.augusto.test.web.WebConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfiguration.class, WebConfiguration.class})
public class TestControllerTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MockHttpSession session;
    @Autowired
    private MockHttpServletRequest request;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldReturnVersion1ForCallToVersion1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/people")
                .accept("application/vnd.app.resource-1.0+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", equalTo("John")));
    }

    @Test
    public void shouldReturnVersion2ForCallToVersion2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/people")
                .accept("application/vnd.app.resource-2.0+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", equalTo("Carl")));
    }

    @Test
    public void shouldReturnUnboundedVersionForCallToVersion32() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/people")
                .accept("application/vnd.app.resource-3.2+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", equalTo("Douglas")));
    }

    @Test
    public void shouldReturnNotFoundForInvalidVersion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/people")
                .accept("application/vnd.app.resource-0.2+json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundIfResourceHeaderIsMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/people")
                .accept(MediaType.ALL))
                .andExpect(status().isNotFound());
    }
}