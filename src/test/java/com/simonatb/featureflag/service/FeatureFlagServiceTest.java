package com.simonatb.featureflag.service;

import com.simonatb.featureflag.dto.CreateFeatureFlagRequest;
import com.simonatb.featureflag.dto.UpdateFeatureFlagRequest;
import com.simonatb.featureflag.entity.FeatureFlag;
import com.simonatb.featureflag.exception.DuplicateFeatureFlagNameException;
import com.simonatb.featureflag.exception.FeatureFlagNotFoundException;
import com.simonatb.featureflag.mapper.FeatureFlagMapper;
import com.simonatb.featureflag.repository.FeatureFlagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeatureFlagServiceTest {

    @Mock
    private FeatureFlagRepository repository;

    @Mock
    private FeatureFlagMapper mapper;

    @InjectMocks
    private FeatureFlagService service;

    @Test
    void testCreateFeatureFlagSuccessfully() {
        CreateFeatureFlagRequest request = new CreateFeatureFlagRequest("Test-flag", "description", true);
        FeatureFlag flag = new FeatureFlag(1L, "Test-flag", "description", true, null, null);

        when(repository.findByName("Test-flag")).thenReturn(Optional.empty());
        when(mapper.toFeatureFlag(request)).thenReturn(flag);
        when(repository.save(flag)).thenReturn(flag);

        FeatureFlag result = service.createFeatureFlag(request);

        assertNotNull(result, "Feature flag should not be null");
        assertEquals("Test-flag", result.getName(), "The feature flag name should be \"Test-flag\"");
        assertEquals("description", result.getDescription(), "Descriptions should be equal");
        assertTrue(result.isEnabled(), "The flag should be enabled");

        verify(repository).findByName("Test-flag");
        verify(mapper).toFeatureFlag(request);
        verify(repository).save(flag);
    }

    @Test
    void testCreateFeatureFlagDuplicateName() {
        CreateFeatureFlagRequest request = new CreateFeatureFlagRequest("Duplicate-flag", "description", false);
        FeatureFlag existing = new FeatureFlag(1L, "Duplicate-flag", "description", false, null, null);

        when(repository.findByName("Duplicate-flag")).thenReturn(Optional.of(existing));

        assertThrows(DuplicateFeatureFlagNameException.class, () -> service.createFeatureFlag(request),
            "There should be a duplicate flag so an exception should be thrown");

        verify(repository).findByName("Duplicate-flag");
        verify(repository, never()).save(any());
    }

    @Test
    void testDeleteFeatureFlagSuccessfully() {
        FeatureFlag flag = new FeatureFlag(1L, "Test-flag", "description", true, null, null);

        when(repository.findById(1L)).thenReturn(Optional.of(flag));

        service.deleteFeatureFlag(1L);

        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testDeleteFeatureFlagNonExistingFlag() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FeatureFlagNotFoundException.class, () -> service.deleteFeatureFlag(1L),
            "Should throw an exception because feature flag with id 1L does not exist");

        verify(repository).findById(1L);
    }

    @Test
    public void testUpdateFeatureFlagSuccessfully() {
        UpdateFeatureFlagRequest request = new UpdateFeatureFlagRequest(false, "description change");
        FeatureFlag flag = new FeatureFlag(1L, "Test-flag", "description", true, null, null);

        when(repository.findById(1L)).thenReturn(Optional.of(flag));
        when(repository.save(flag)).thenReturn(flag);

        FeatureFlag result = service.updateFeatureFlag(1L, request);

        assertEquals("description change", result.getDescription(),
            "The flag description should be changed");
        assertFalse(result.isEnabled(),
            "The feature flag should not be enabled");

        verify(repository).findById(1L);
        verify(repository).save(result);
    }

    @Test
    public void testUpdateFeatureFlagNotExisting() {
        UpdateFeatureFlagRequest request = new UpdateFeatureFlagRequest(false, "description change");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FeatureFlagNotFoundException.class, () -> service.updateFeatureFlag(1L, request),
            "Exception should be thrown because there isn't a flag with id 1L");

        verify(repository).findById(1L);
    }

}
