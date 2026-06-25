package com.simonatb.featureflag.controller;

import com.simonatb.featureflag.dto.UpdateFeatureFlagRequest;
import com.simonatb.featureflag.entity.FeatureFlag;
import com.simonatb.featureflag.exception.FeatureFlagNotFoundException;
import com.simonatb.featureflag.mapper.FeatureFlagMapper;
import com.simonatb.featureflag.service.FeatureFlagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FeatureFlagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeatureFlagService service;

    @MockitoBean
    private FeatureFlagMapper mapper;

    @Test
    void testCreateFeatureFlagReturns201() throws Exception {
        FeatureFlag flag = new FeatureFlag(1L, "my-flag", "description", false, null, null);

        when(service.createFeatureFlag(any())).thenReturn(flag);

        mockMvc.perform(post("/flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "my-flag",
                        "description": "description",
                        "enabled": false
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("my-flag"))
            .andExpect(jsonPath("$.enabled").value(false));
    }

    @Test
    void testCreateFeatureFlagBlankNameReturns400() throws Exception {
        mockMvc.perform(post("/flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "",
                        "description": "description",
                        "enabled": false
                    }
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteFeatureFlagReturns204() throws Exception {
        mockMvc.perform(delete("/flags/1"))
            .andExpect(status().isNoContent());

        verify(service).deleteFeatureFlag(1L);
    }

    @Test
    void testDeleteFeatureFlagReturns404() throws Exception {
        doThrow(new FeatureFlagNotFoundException("Feature flag with id 1L not found"))
            .when(service).deleteFeatureFlag(1L);

        mockMvc.perform(delete("/flags/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFeatureFlagReturns200() throws Exception {
        FeatureFlag flag = new FeatureFlag(1L, "my-flag", "updated description", false, null, null);

        when(service.updateFeatureFlag(eq(1L), any())).thenReturn(flag);

        mockMvc.perform(patch("/flags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "enabled": false,
                        "description": "updated description"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.enabled").value(false))
            .andExpect(jsonPath("$.description").value("updated description"));
    }

    @Test
    void testUpdateFeatureFlagReturns404() throws Exception {
        doThrow(new FeatureFlagNotFoundException("Feature flag with id 1L not found"))
            .when(service).updateFeatureFlag(eq(1L), any());

        mockMvc.perform(patch("/flags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "enabled": false,
                        "description": "updated description"
                    }
                    """))
            .andExpect(status().isNotFound());
    }

}