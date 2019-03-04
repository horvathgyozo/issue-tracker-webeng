package hu.elte.IssueTracker.controllers;

import hu.elte.IssueTracker.entities.Issue;
import hu.elte.IssueTracker.entities.Message;
import hu.elte.IssueTracker.repositories.IssueRepository;
import hu.elte.IssueTracker.repositories.MessageRepository;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("")
    public String list(Model model) {
//        model.addAttribute("issues", issueRepository.findAll());
        model.addAttribute("issues", issueRepository.findAllIssueWithMessageCount());
        return "list";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Integer id, Model model, Message message) throws Exception {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            Issue issue = oIssue.get();
            model.addAttribute("issue", issue);
            model.addAttribute("messages", messageRepository.findAllByIssue(issue));
//            model.addAttribute("message", new Message());
            return "issue";
        }
        throw new Exception("No such issue id");
    }

    @GetMapping("/new")
    public String addForm(Model model) {
        model.addAttribute("issue", new Issue());
        return "issue-form";
    }

    @PostMapping("/new")
    public String addIssue(@Valid Issue issue, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "issue-form";
        }

        issueRepository.save(issue);
        return "redirect:/issues";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) throws Exception {
        Optional<Issue> oIssue = issueRepository.findById(id);
        if (oIssue.isPresent()) {
            model.addAttribute("issue", oIssue.get());
            return "issue-form";
        }
        throw new Exception("No such issue id");
    }

    @PostMapping("/{id}/edit")
    public String editIssue(@PathVariable Integer id, @Valid Issue issue, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "issue-form";
        }

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