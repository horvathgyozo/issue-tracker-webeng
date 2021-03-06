package hu.elte.IssueTracker.restcontrollers;

import hu.elte.IssueTracker.entities.Issue;
import hu.elte.IssueTracker.entities.Label;
import hu.elte.IssueTracker.entities.Message;
import hu.elte.IssueTracker.entities.User;
import hu.elte.IssueTracker.repositories.IssueRepository;
import hu.elte.IssueTracker.repositories.LabelRepository;
import hu.elte.IssueTracker.repositories.MessageRepository;
import hu.elte.IssueTracker.repositories.UserRepository;
import hu.elte.IssueTracker.security.AuthenticatedUser;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/issues")
public class IssueRestController {
    
    @Autowired
    private IssueRepository issueRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private LabelRepository labelRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User getUser(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username).get();
    }
    
    @GetMapping("")
    public ResponseEntity<Iterable<Issue>> getAll(Principal principal) {
//        return ResponseEntity.ok(issueRepository.findAll());
        User user = getUser(principal);
//        User user = authenticatedUser.getUser();
// Another alternative: JsonIgnore or FetchType.EAGER on OneToMany lists        
        User.Role role = user.getRole();
        if (role.equals(User.Role.ROLE_ADMIN)) {
            return ResponseEntity.ok(issueRepository.findAll());
        } else {
            return ResponseEntity.ok(user.getIssues());
        }
    }
    
//    @Secured({ "ROLE_USER" })
    @GetMapping("/{id}")
    public ResponseEntity<Issue> get(@PathVariable Integer id) {
        Optional<Issue> issue = issueRepository.findById(id);
        if (issue.isPresent()) {
            return ResponseEntity.ok(issue.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("")
    public ResponseEntity<Issue> post(@RequestBody Issue issue, Principal principal) {
        User user = getUser(principal);
        issue.setUser(user);
        Issue savedIssue = issueRepository.save(issue);
        return ResponseEntity.ok(savedIssue);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Issue> update
            (@PathVariable Integer id,
             @RequestBody Issue issue) {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            issue.setId(id);
            return ResponseEntity.ok(issueRepository.save(issue));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
            
    @DeleteMapping("/{id}")
    public ResponseEntity<Issue> delete
            (@PathVariable Integer id) {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            issueRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
     
    /*
    @GetMapping("/{id}/messages")
    public ResponseEntity<Iterable<Message>> messages
            (@PathVariable Integer id) {
        Optional<Issue> issue = issueRepository.findById(id);
        if (issue.isPresent()) {
            return ResponseEntity.ok(issue.get().getMessages());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
            
    @PostMapping("/{id}/messages")
    public ResponseEntity<Message> insertMessage
            (@PathVariable Integer id,
             @RequestBody Message message) {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            Issue issue = oIssue.get();
            message.setIssue(issue);
            return ResponseEntity.ok(
                messageRepository.save(message));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
            
    @GetMapping("/{id}/labels")
    public ResponseEntity<Iterable<Label>> labels
        (@PathVariable Integer id) {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            return ResponseEntity.ok(oIssue.get().getLabels());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
        
    @PostMapping("/{id}/labels")
    public ResponseEntity<Label> insertLabel
        (@PathVariable Integer id, 
         @RequestBody Label label) {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            Issue issue = oIssue.get();
            Label newLabel = labelRepository.save(label);
            issue.getLabels().add(newLabel);
            issueRepository.save(issue);
            return ResponseEntity.ok(newLabel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
        
    @PutMapping("/{id}/labels")
    public ResponseEntity<Iterable<Label>> modifyLabels
        (@PathVariable Integer id,
         @RequestBody List<Label> labels) {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            Issue issue = oIssue.get();
            
            for (Label label: labels) {
                if (label.getId() == null) {
                    labelRepository.save(label);
                }
            }
            
            issue.setLabels(labels);
            issueRepository.save(issue);
            return ResponseEntity.ok(labels);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    */
}
