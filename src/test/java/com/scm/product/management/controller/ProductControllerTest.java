package com.scm.product.management.controller;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    MockWebServer userWebServer;

    @Before
    public void init() throws IOException {
        userWebServer = new MockWebServer();
        userWebServer.start(8011);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).build();
    }

    @After
    public void tearDown() throws IOException{
        userWebServer.shutdown();
    }

    @Test
    public void createProduct_happyflow() throws  Exception{
        userWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Content-Type","application/json").setBody("true"));
        MvcResult result = this.mockMvc.perform(post("/dealer/createproduct")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA=")
                .content("{\n" +
                        "\t\"productName\":\"Wiper\",\n" +
                        "\t\"stock\" : \"10\",\n" +
                        "\t\"vendorId\" : \"9\",\n" +
                        "\t\"availability\" : \"Y\",\n" +
                        "\t\"launchDate\" : \"\",\n" +
                        "\t\"cost\" : 400.00,\n" +
                        "\t\"productDescription\" : \"Hyndai\"\n" +
                        "}"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
        Assert.assertFalse(result.getResponse().getContentAsString().contains("\"product\":null"));
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"errors\":[]"));
    }

    @Test
    public void createProduct_badrequest() throws  Exception{
        MvcResult result = this.mockMvc.perform(post("/dealer/createproduct")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA=")
                .content("{\n" +
                        "\t\"orderedBy\" : null,\n" +
                        "\t\"orderDetails\" : [{\n" +
                        "\t\t\"productId\" : null,\n" +
                        "\t\t\"quantity\" : null\n" +
                        "\t},{\n" +
                        "\t\t\"productId\" : null,\n" +
                        "\t\t\"quantity\" : null\n" +
                        "\t}]\n" +
                        "\t\n" +
                        "}"))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"product\":null"));
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"PM-FV01\""));
    }


    @Test
    @Sql({"/data.sql"})
    public void editProduct_happyflow() throws  Exception{
        MvcResult result = this.mockMvc.perform(put("/dealer/editproduct/1")
                .param("stock","15")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA="))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"stock\":15"));
        Assert.assertTrue(result.getResponse().getContentAsString().contains("\"errors\":null"));
    }

    @Test
    public void editProduct_badRequest() throws  Exception{
        MvcResult result = this.mockMvc.perform(put("/dealer/editproduct/1")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA="))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void deleteProduct() throws  Exception{
        MvcResult result = this.mockMvc.perform(delete("/dealer/deleteproduct/1")
                .contentType("application/json")
                .header("Authorization","Basic Z291dGhhbTpKdW5AMjA="))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        Assert.assertEquals("Product Deleted Successfully",result.getResponse().getContentAsString());
    }
}
