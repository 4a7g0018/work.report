package com.yan.work.report.model;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkData {
    private Map<String, WorkMatter> workMatters;
    private Integer lastNumber;
}
