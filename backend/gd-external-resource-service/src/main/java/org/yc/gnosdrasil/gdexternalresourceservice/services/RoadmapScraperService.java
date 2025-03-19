package org.yc.gnosdrasil.gdexternalresourceservice.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdexternalresourceservice.dtos.response.RoadmapResponseDTO;

import java.io.IOException;
import java.util.*;

@Service
public class RoadmapScraperService {

    public List<RoadmapResponseDTO> getRoadmap() {
        String url = "https://roadmap.sh/javascript";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements elements = document.select("g[data-node-id]");

        // Map to store topics by their nodeId
//        Map<String, Topic> topicMap = new HashMap<>();

        // List to store root topics (topics without a parent)
        List<RoadmapResponseDTO> topics = new ArrayList<>();

        // First pass: Create all topics and subtopics
        for (Element element : elements) {
//                        Topic topic = new Topic();
//            topic.setNodeId(element.attr("data-node-id"));
//            topic.setType(element.attr("data-type"));
//            topic.setTitle(element.attr("data-title"));
//            topic.setParentId(element.attr("data-parent-id"));
//            topic.setParentTitle(element.attr("data-parent-title"));

            // Add to the map
            topics.add(RoadmapResponseDTO.builder()
                    .nodeId(element.attr("data-node-id"))
                    .title(element.attr("data-title"))
                    .type(element.attr("data-type"))
                    .build());
//);
        }

        // Second pass: Link subtopics to their parent topics
//        for (Topic topic : topicMap.values()) {
//            if (topic.getParentId() != null && !topic.getParentId().isEmpty()) {
//                Topic parentTopic = topicMap.get(topic.getParentId());
//                if (parentTopic != null) {
//                    parentTopic.getSubtopics().add(topic);
//                }
//            } else {
//                // If no parentId, it's a root topic
//                rootTopics.add(topic);
//            }
//        }

        return topics;
    }
}