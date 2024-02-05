package evernote.mvpcalculator.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class Player {
    private String id;
    private String team;
    private Map<String, Object> attributes; // All non-essential player info
    private List<Integer> metrics; // Field might be useful for future development
    private Integer score;
}
