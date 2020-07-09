package ouhk.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.dao.TicketUserRepository;
import ouhk.comps380f.exception.TicketUserNotFound;
import ouhk.comps380f.model.TicketUser;
import ouhk.comps380f.service.TicketUserService;

@Controller
@RequestMapping("/user")
public class TicketUserController {

    @Resource
    TicketUserRepository ticketUserRepo;
    
    @Resource
    TicketUserService ticketUserService;

    @GetMapping({"", "/list"})
    public String list(ModelMap model) {
        model.addAttribute("ticketUsers", ticketUserRepo.findAll());
        return "listUser";
    }

    public static class Form {

        private String username;
        private String password;
        private String[] roles;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("addUser", "ticketUser", new Form());
    }

    @PostMapping("/create")
    public View create(Form form) throws IOException {
        TicketUser user = new TicketUser(form.getUsername(),
                form.getPassword(), form.getRoles()
        );
        ticketUserRepo.save(user);
        return new RedirectView("/user/list", true);
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register", "ticketUser", new Form());
    }
    
    @PostMapping("/register")
    public View register(Form form) throws IOException {
        TicketUser user = new TicketUser(form.getUsername(),
                form.getPassword(), form.getRoles()
        );
        ticketUserRepo.save(user);
        return new RedirectView("/", true);
    }
    
    @GetMapping("/edit/{username}")
    public ModelAndView showEdit(@PathVariable("username") String username,
            Principal principal, HttpServletRequest request) {
        TicketUser user = ticketUserRepo.findById(username).orElse(null);
        if (user == null
                || !request.isUserInRole("ROLE_ADMIN")) {
            return new ModelAndView(new RedirectView("/index", true));
        }
        ModelAndView modelAndView = new ModelAndView("editUser");
        modelAndView.addObject("user", user);
        Form userForm = new Form();
        userForm.setUsername(user.getUsername());
        userForm.setPassword(user.getPassword());
        modelAndView.addObject("userForm", userForm);
        return modelAndView;
    }

    @PostMapping("/edit/{username}")
    public String edit(@PathVariable("username") String username, Form form,
            Principal principal, HttpServletRequest request)
            throws IOException, TicketUserNotFound {
        TicketUser user = ticketUserRepo.findById(username).orElse(null);
        if (user == null
                || !request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/ticket/index";
        }
        ticketUserService.updateUser(username, form.getUsername(),
                form.getPassword(), form.getRoles());
        return "redirect:/user/list/";
    }
    
    @GetMapping("/delete/{username}")
    public View deleteTicket(@PathVariable("username") String username) {
        ticketUserRepo.delete(ticketUserRepo.findById(username).orElse(null));
        return new RedirectView("/user/list", true);
    }
}
