package org.yc.gnosdrasil.gdexternalresourceservice.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resource {
//    private String id;
    private String title;
    private String link;
    private String snippet;
    private String pageSource;
    private String stepId;
}
