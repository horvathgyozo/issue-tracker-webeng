package hu.elte.IssueTracker.repositories;

import hu.elte.IssueTracker.entities.Label;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends CrudRepository<Label, Integer> {
    
}
