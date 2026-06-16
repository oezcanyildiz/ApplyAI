package com.applyai.applyai.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.applyai.applyai.dto.request.CreateApplicationRequest;
import com.applyai.applyai.dto.request.UpdateApplicationRequest;
import com.applyai.applyai.dto.response.ApplicationResponse;
import com.applyai.applyai.service.IApplicationService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final IApplicationService applicationService;

    public ApplicationController(IApplicationService applicationService){
        this.applicationService=applicationService;
    }
    
    @PostMapping
    public ResponseEntity <ApplicationResponse> createApp(@Valid @RequestBody CreateApplicationRequest request){
        return new ResponseEntity<>(
            applicationService.createApplication(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity <List<ApplicationResponse>> getAllApplications(){
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity <ApplicationResponse> getApplicationById(@PathVariable Long id){
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity <ApplicationResponse> updateApplication(@PathVariable Long id , @Valid @RequestBody UpdateApplicationRequest request){
        return ResponseEntity.ok(applicationService.updateApplication(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id ){
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
