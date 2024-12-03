package com.ai.resume.builder.controllers;

import com.ai.resume.builder.dto.AdditionalDetailsRequest;
import com.ai.resume.builder.dto.AdditionalDetailsResponse;
import com.ai.resume.builder.services.AdditionalDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/resume")
@AllArgsConstructor
@Tag(name = "Resume Additional Details API", description = "Operations related to additional details of resume")
public class AdditionalDetailsController {
    private final AdditionalDetailsService additionalDetailsService;

    @Operation(summary = "Save Additional Details",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PostMapping(
            value = "/{resumeId}/additional-details",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AdditionalDetailsResponse> saveAdditionalDetails(@RequestBody @Valid AdditionalDetailsRequest additionalDetailsRequest, @PathVariable("resumeId") String resumeId) {
        AdditionalDetailsResponse response = additionalDetailsService.saveAdditionalDetails(additionalDetailsRequest, UUID.fromString(resumeId));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Additional Details",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @GetMapping(
            value = "/{resumeId}/additional-details",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AdditionalDetailsResponse> getAdditionalDetails(@PathVariable("resumeId") String resumeId) {
        AdditionalDetailsResponse response = additionalDetailsService.getAdditionalDetails(UUID.fromString(resumeId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update Additional Details",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(
            value = "/{resumeId}/additional-details/{additionalDetailsId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AdditionalDetailsResponse> updateAdditionalDetails(@RequestBody @Valid AdditionalDetailsRequest additionalDetailsRequest, @PathVariable("resumeId") String resumeId, @PathVariable("additionalDetailsId") String additionalDetailsId) {
        AdditionalDetailsResponse response = additionalDetailsService.updateAdditionalDetails(additionalDetailsRequest, UUID.fromString(resumeId), UUID.fromString(additionalDetailsId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
