package org.yc.gnosdrasil.gdexternalresourceservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yc.gnosdrasil.gdexternalresourceservice.dtos.request.StepRequestDTO;
import org.yc.gnosdrasil.gdexternalresourceservice.dtos.response.SerpApiResponseDTO;
import org.yc.gnosdrasil.gdexternalresourceservice.entities.Resource;
import org.yc.gnosdrasil.gdexternalresourceservice.services.SerpApiService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SerpApiService serpApiService;

    @PostMapping("/resources")
    public List<Resource> search(@RequestBody StepRequestDTO stepRequestDTO) {
        return serpApiService.getResources(stepRequestDTO);
    }
}