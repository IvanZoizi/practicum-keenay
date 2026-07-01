package org.example.dto.groups;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class GroupDTO {
    @NonNull
    private String title;
    @NonNull
    @Min(1)
    @Max(6)
    private Integer course;
}
