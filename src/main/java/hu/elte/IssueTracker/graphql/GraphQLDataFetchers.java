package hu.elte.IssueTracker.graphql;

import com.google.common.collect.Lists;
import graphql.schema.DataFetcher;
import hu.elte.IssueTracker.entities.Issue;
import hu.elte.IssueTracker.repositories.IssueRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    
    public DataFetcher getIssuesDataFetcher() {
        return dataFetchingEnvironment -> {
            return issueRepository.findAll();
        };
    }

    public DataFetcher getUserDataFetcher() {
        return dataFetchingEnvironment -> {
            Issue issue = dataFetchingEnvironment.getSource();
            return issue.getUser();
        };
    }
    
    public DataFetcher getIssueCreatorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, Object> issueInputMap = dataFetchingEnvironment.getArgument("issue");
            Issue issue = new Issue();
            issue.setTitle(issueInputMap.get("title").toString());
            issue.setDescription(issueInputMap.get("description").toString());
            issue.setStatus(Issue.Status.valueOf(issueInputMap.get("status").toString()));
            issue.setPlace(issueInputMap.get("place").toString());
            return issueRepository.save(issue);
        };
    }
}