package com.simonatb.featureflag.repository;

import com.simonatb.featureflag.entity.FeatureFlag;

import java.util.List;
import java.util.Optional;

public interface FeatureFlagRepository {

    FeatureFlag save(FeatureFlag flag);

    Optional<FeatureFlag> findById(Long id);

    Optional<FeatureFlag> findByName(String name);

    List<FeatureFlag> findAll();

    void deleteById(Long id);

}
