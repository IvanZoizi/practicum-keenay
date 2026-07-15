package org.example.dto.events;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailEvent {
    private Long idToken;
    private String email;
    private Integer attempt;
    private Throwable ex;
}
