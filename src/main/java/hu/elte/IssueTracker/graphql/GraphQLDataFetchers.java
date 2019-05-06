package hu.elte.IssueTracker.graphql;

import graphql.schema.DataFetcher;
import hu.elte.IssueTracker.entities.Issue;
import hu.elte.IssueTracker.repositories.IssueRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphQLDataFetchers {

    @Autowired
    private IssueRepository issueRepository;
    
    public DataFetcher getIssueByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            Integer issueId = dataFetchingEnvironment.getArgument("id");
            Optional<Issue> oIssue = issueRepository.findById(issueId);
            return oIssue.isPresent() ? oIssue.get() : null;
        };
    }

    public DataFetcher getUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Issue issue = dataFetchingEnvironment.getSource();
            return issue.getUser();
        };
    }
}