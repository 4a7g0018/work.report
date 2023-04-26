package com.yan.work.report.model;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkMatter {
    private String title;
    private String text;
    private String startDate;
    private String dateline;
    private Boolean success;
    private List<WorkMatter> subItem;
}
