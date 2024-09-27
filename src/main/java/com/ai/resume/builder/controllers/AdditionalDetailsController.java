package com.ai.resume.builder.controllers;

import com.ai.resume.builder.dto.AdditionalDetailsRequest;
import com.ai.resume.builder.dto.AdditionalDetailsResponse;
import com.ai.resume.builder.services.AdditionalDetailsServiceImplementation;
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
public class AdditionalDetailsController {
    private final AdditionalDetailsServiceImplementation additionalDetailsServiceImplementation;

    @Transactional
    @PostMapping(
            value = "/{resumeId}/additional-details",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AdditionalDetailsResponse> saveAdditionalDetails(
            @RequestBody @Valid AdditionalDetailsRequest additionalDetailsRequest,
            @PathVariable("resumeId") String resumeId) {

        AdditionalDetailsResponse response = this.additionalDetailsServiceImplementation
                .saveAdditionalDetails(additionalDetailsRequest, UUID.fromString(resumeId));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping(
            value = "/{resumeId}/additional-details",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AdditionalDetailsResponse> getAdditionalDetails(@PathVariable("resumeId") String resumeId) {

        AdditionalDetailsResponse response = this.additionalDetailsServiceImplementation
                .getAdditionalDetails(UUID.fromString(resumeId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    @PutMapping(
            value = "/{resumeId}/additional-details/{additionalDetailsId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AdditionalDetailsResponse> updateAdditionalDetails(
            @RequestBody @Valid AdditionalDetailsRequest additionalDetailsRequest,
            @PathVariable("resumeId") String resumeId,
            @PathVariable("additionalDetailsId") String additionalDetailsId) {

        AdditionalDetailsResponse response = this.additionalDetailsServiceImplementation
                .updateAdditionalDetails(additionalDetailsRequest, UUID.fromString(resumeId), UUID.fromString(additionalDetailsId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
