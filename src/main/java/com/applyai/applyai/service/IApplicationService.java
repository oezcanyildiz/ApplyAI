package com.applyai.applyai.service;

import java.util.List;

import com.applyai.applyai.dto.request.CreateApplicationRequest;
import com.applyai.applyai.dto.request.UpdateApplicationRequest;
import com.applyai.applyai.dto.response.ApplicationResponse;

public interface IApplicationService {

    ApplicationResponse createApplication(CreateApplicationRequest request);

    ApplicationResponse updateApplication(Long id, UpdateApplicationRequest request);

    ApplicationResponse getApplicationById(Long id);

    List<ApplicationResponse> getAllApplications();
    
    void deleteApplication(Long id);

}
