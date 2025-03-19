package com.example.demo.dtos.repsonse;

import com.example.demo.utils.enums.LearningIntent;
import com.example.demo.utils.enums.LearningScope;
import com.example.demo.utils.enums.SkillLevel;

public record ClassificationResponseDTO(SkillLevel level,
                                        LearningIntent intent,
                                        String subject,
                                        LearningScope scope) {
}
