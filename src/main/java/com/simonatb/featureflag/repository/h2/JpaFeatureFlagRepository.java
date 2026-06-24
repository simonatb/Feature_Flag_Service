package com.simonatb.featureflag.repository.h2;

import com.simonatb.featureflag.entity.FeatureFlag;
import com.simonatb.featureflag.repository.FeatureFlagRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaFeatureFlagRepository extends JpaRepository<FeatureFlag, Long>, FeatureFlagRepository {

    Optional<FeatureFlag> findByName(String name);

}
