package hu.elte.IssueTracker.controllers;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    private User getUser(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username).get();
    }

    @GetMapping("")
    public String list(Model model, Principal principal) {
//        model.addAttribute("issues", issueRepository.findAll());
//        model.addAttribute("issues", issueRepository.findAllIssueWithMessageCount());
//        model.addAttribute("issues", issueRepository.findAllByUserWithMessageCount(getUser(principal)));
//        model.addAttribute("issues", issueRepository.findAllByUserWithMessageCount(authenticatedUser.getUser()));
        User user = getUser(principal);
        Iterable<Issue> issues = user.getRole() == User.Role.ROLE_USER
                ? user.getIssues()
                : issueRepository.findAll();
        model.addAttribute("issues", issues);
        return "list";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Integer id, Model model, Message message) throws Exception {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            Issue issue = oIssue.get();
            model.addAttribute("issue", issue);
//            model.addAttribute("message", new Message());
            return "issue";
        }
        throw new Exception("No such issue id");
    }

    @GetMapping("/new")
    public String addForm(Model model) {
        model.addAttribute("issue", new Issue());
        model.addAttribute("allLabels", labelRepository.findAll());
        model.addAttribute("issueLabels", new ArrayList<Integer>());

        return "issue-form";
    }

    @PostMapping("/new")
    public String addIssue(@Valid Issue issue, BindingResult bindingResult,
            @RequestParam(value = "labels", required = false) ArrayList<Integer> labels, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allLabels", labelRepository.findAll());
            model.addAttribute("issueLabels", labels == null ? new ArrayList<Integer>() : labels);
            return "issue-form";
        }

//        if (labels != null) {
//            for (int i : labels) {
//                Label l = new Label();
//                l.setId(i);
//                issue.getLabels().add(l);
//            }
//        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username).get();
        issue.setUser(user);
        issue.setStatus(Issue.Status.NEW);

        issueRepository.save(issue);
        return "redirect:/issues";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) throws Exception {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            model.addAttribute("issue", oIssue.get());
            model.addAttribute("allLabels", labelRepository.findAll());

            List<Integer> issueLabels = new ArrayList<>();
            for (Label l : oIssue.get().getLabels()) {
                issueLabels.add(l.getId());
            }
            model.addAttribute("issueLabels", issueLabels);
//            System.out.println(issueLabels.toString());

            return "issue-form";
        }
        throw new Exception("No such issue id");
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/{id}/edit")
    public String editIssue(@PathVariable Integer id, @Valid Issue issue, BindingResult bindingResult,
            @RequestParam(value = "labels", required = false) ArrayList<Integer> labels, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allLabels", labelRepository.findAll());
            model.addAttribute("issueLabels", labels);
            return "issue-form";
        }

        Issue dbIssue = issueRepository.findById(id).get();
        issue.setUser(dbIssue.getUser());
//        issue.getLabels().removeIf(l -> l.getId() == null);
//        if (labels != null) {
//            labels.stream().map((i) -> {
//                Label l = new Label();
//                l.setId(i);
//                return l;
//            }).forEachOrdered((l) -> {
//                issue.getLabels().add(l);
//            });
//        }
        issueRepository.save(issue);
        return "redirect:/issues";
    }

    @GetMapping("/{id}/delete")
    public String deleteIssue(@PathVariable Integer id) {
        try {
            issueRepository.deleteById(id);
        } catch (Exception e) {
        }
        return "redirect:/issues";
    }

    @PostMapping("/{id}/message")
    public String addMessage(@PathVariable Integer id, @Valid Message message, BindingResult bindingResult, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return get(id, model, message);
        }

        Issue issue = issueRepository.findById(id).get();
        message.setId(null);
        message.setIssue(issue);
        messageRepository.save(message);
        return "redirect:/issues";
    }

}
