package hu.elte.IssueTracker.dtos;

import hu.elte.IssueTracker.entities.Issue;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IssueListDTO extends Issue {
    private long count;
    
    public IssueListDTO(Integer id, String title, String description, String place, Issue.Status status, LocalDateTime created_at, LocalDateTime updated_at, long count) {
        super(id, title, description, place, status, created_at, updated_at, null, null);
        this.count = count;
    }
}
