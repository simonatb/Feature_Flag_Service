package com.simonatb.featureflag.controller;

import com.simonatb.featureflag.dto.CreateFeatureFlagRequest;
import com.simonatb.featureflag.dto.FeatureFlagEvaluationResponse;
import com.simonatb.featureflag.dto.UpdateFeatureFlagRequest;
import com.simonatb.featureflag.entity.FeatureFlag;
import com.simonatb.featureflag.service.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flags")
@RequiredArgsConstructor
public class FeatureFlagController {

    private final FeatureFlagService service;

    @PostMapping
    public ResponseEntity<FeatureFlag> createFeatureFlag(@RequestBody CreateFeatureFlagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createFeatureFlag(request));
    }

    @GetMapping
    public List<FeatureFlag> getAllFeatureFlags() {
        return service.getAllFeatureFlags();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureFlag> getFeatureFlagById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFeatureFlagById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FeatureFlag> updateFeatureFlag(@PathVariable Long id,
                                                         @RequestBody UpdateFeatureFlagRequest request) {
        return ResponseEntity.ok(service.updateFeatureFlag(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeatureFlag(@PathVariable Long id) {
        service.deleteFeatureFlag(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{name}/evaluate")
    public ResponseEntity<FeatureFlagEvaluationResponse> evaluateFeatureFlag(@PathVariable String name) {
        return ResponseEntity.ok(service.evaluateFeatureFlag(name));
    }

}
