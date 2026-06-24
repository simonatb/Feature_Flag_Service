package com.simonatb.featureflag.service;

import com.simonatb.featureflag.dto.CreateFeatureFlagRequest;
import com.simonatb.featureflag.dto.FeatureFlagEvaluationResponse;
import com.simonatb.featureflag.dto.UpdateFeatureFlagRequest;
import com.simonatb.featureflag.entity.FeatureFlag;
import com.simonatb.featureflag.exception.DuplicateFeatureFlagNameException;
import com.simonatb.featureflag.exception.FeatureFlagNotFoundException;
import com.simonatb.featureflag.mapper.FeatureFlagMapper;
import com.simonatb.featureflag.repository.FeatureFlagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureFlagService {

    private final FeatureFlagRepository repository;
    private final FeatureFlagMapper mapper;

    @Transactional
    public FeatureFlag createFeatureFlag(CreateFeatureFlagRequest request) {
        if (repository.findByName(request.name()).isPresent()) {
            throw new DuplicateFeatureFlagNameException(
                String.format("Feature flag with name %s already exists", request.name()));
        }

        return repository.save(mapper.toFeatureFlag(request));
    }

    public List<FeatureFlag> getAllFeatureFlags() {
        return repository.findAll();
    }

    public FeatureFlag getFeatureFlagById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new FeatureFlagNotFoundException(
                String.format("Feature flag with id %d not found", id)));
    }

    @Transactional
    public FeatureFlag updateFeatureFlag(Long id, UpdateFeatureFlagRequest request) {
        FeatureFlag flag = getFeatureFlagById(id);

        if (request.enabled() != null) {
            flag.setEnabled(request.enabled());
        }

        if (request.description() != null) {
            flag.setDescription(request.description());
        }

        return repository.save(flag);
    }

    @Transactional
    public void deleteFeatureFlag(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new FeatureFlagNotFoundException(
                String.format("Feature flag with id: %d not found", id));
        }

        repository.deleteById(id);
    }

    public FeatureFlagEvaluationResponse evaluateFeatureFlag(String name) {
        FeatureFlag flag = repository.findByName(name)
            .orElseThrow(() -> new FeatureFlagNotFoundException(
                String.format("Feature flag with name %s not found", name)));

        return mapper.toFeatureFlagEvaluationResponse(flag);
    }

}
