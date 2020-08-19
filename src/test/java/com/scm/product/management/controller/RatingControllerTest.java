package com.scm.product.management.controller;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Before
    public void init() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).build();
    }

    @Test
    public void rateproduct_happyFlow() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/customer/rateproduct")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA=")
                .content("{\n" +
                        "\t\"ratingValue\" : 3.0,\n" +
                        "\t\"review\" : \"Okayish\",\n" +
                        "\t\"reviewDate\" :null,\n" +
                        "\t\"userId\" : 10,\n" +
                        "\t\"productId\" : 1\n" +
                        "}"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
        Assert.assertFalse(result.getResponse().getContentAsString().contains("\"product\":null"));
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"errors\":null"));
    }

    @Test
    public void rateproduct_badRequest() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/customer/rateproduct")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA=")
                .content("{\n" +
                        "\t\"ratingValue\" : null,\n" +
                        "\t\"review\" : \"Okayish\",\n" +
                        "\t\"reviewDate\" :null,\n" +
                        "\t\"userId\" : null,\n" +
                        "\t\"productId\" : 7\n" +
                        "}"))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"product\":null"));
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"PM-FV01\""));
    }
}
