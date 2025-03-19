package org.yc.gnosdrasil.gdexternalresourceservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yc.gnosdrasil.gdexternalresourceservice.dtos.response.SerpApiResponseDTO;
import org.yc.gnosdrasil.gdexternalresourceservice.services.SerpApiService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SerpApiService serpApiService;

    @GetMapping
    public List<SerpApiResponseDTO> search(@RequestParam String query) {
        return serpApiService.search(query);
    }
}