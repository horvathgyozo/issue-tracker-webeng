package hu.elte.IssueTracker.repositories;

import hu.elte.IssueTracker.entities.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
//    public Iterable<Message> findAllByIssue(Issue issue);
}
