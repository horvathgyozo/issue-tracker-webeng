package hu.elte.IssueTracker.repositories;

import hu.elte.IssueTracker.dtos.IssueListDTO;
import hu.elte.IssueTracker.entities.Issue;
import hu.elte.IssueTracker.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends CrudRepository<Issue, Integer> {
//    public Iterable<Issue> findAllByUser(User user);
    
    @Query("SELECT new hu.elte.IssueTracker.dtos.IssueListDTO(i.id, i.title, i.description, i.place, i.status, i.created_at, i.updated_at, count(m.id)) FROM Issue i LEFT JOIN i.messages m GROUP BY i")
    List<IssueListDTO> findAllIssueWithMessageCount();
}
