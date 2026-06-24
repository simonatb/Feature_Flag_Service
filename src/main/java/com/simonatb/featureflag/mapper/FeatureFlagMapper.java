package com.simonatb.featureflag.mapper;

import com.simonatb.featureflag.dto.CreateFeatureFlagRequest;
import com.simonatb.featureflag.dto.FeatureFlagEvaluationResponse;
import com.simonatb.featureflag.entity.FeatureFlag;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagMapper {

    public FeatureFlag toFeatureFlag(CreateFeatureFlagRequest request) {
        if (request == null) {
            return null;
        }

        FeatureFlag flag = new FeatureFlag();
        flag.setName(request.name());
        flag.setDescription(request.description());
        flag.setEnabled(request.enabled());

        return flag;
    }

    public FeatureFlagEvaluationResponse toFeatureFlagEvaluationResponse(FeatureFlag flag) {
        if (flag == null) {
            return null;
        }

        return new FeatureFlagEvaluationResponse(flag.getName(), flag.isEnabled());
    }

}
