package ouhk.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.exception.AttachmentNotFound;
import ouhk.comps380f.exception.TicketNotFound;
import ouhk.comps380f.exception.ReplyNotFound;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.Ticket;
import ouhk.comps380f.model.Reply;
import ouhk.comps380f.service.AttachmentService;
import ouhk.comps380f.service.TicketService;
import ouhk.comps380f.service.ReplyService;
import ouhk.comps380f.view.DownloadingView;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private ReplyService replyService;

    @GetMapping({"/lecture"})
    public String lecture(ModelMap model) {
        model.addAttribute("ticketDatabase", ticketService.getTickets());
        return "lecture";
    }
    
    @GetMapping({"/lab"})
    public String lab(ModelMap model) {
        model.addAttribute("ticketDatabase", ticketService.getTickets());
        return "lab";
    }
    
    @GetMapping({"/other"})
    public String other(ModelMap model) {
        model.addAttribute("ticketDatabase", ticketService.getTickets());
        return "other";
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("add", "ticketForm", new Form());
    }

    public static class Form {

        private String subject;
        private String category;
        private String body;
        private List<MultipartFile> attachments;
        
        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }

        
    }

    @PostMapping("/create")
    public String create(Form form, Principal principal) throws IOException {
        long ticketId = ticketService.createTicket(principal.getName(),
                form.getSubject(), form.getCategory(), form.getBody(), form.getAttachments());
        return "redirect:/ticket/view/" + ticketId;
    }
    
    @GetMapping("/reply/{ticketId}")
    public ModelAndView reply() {
        
        return new ModelAndView("reply", "replyForm", new ReplyForm());
    }
    
    public static class ReplyForm {

        private String body;
        private long ticketId;
        
        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public long getTicketId() {
            return ticketId;
        }

        public void setTicketId(long ticketId) {
            this.ticketId = ticketId;
        }
        
    }
    
    @PostMapping("/reply/{ticketId}")
    public String reply(@PathVariable("ticketId") long ticketId, ReplyForm replyForm, Principal principal) throws IOException {
        
        long replyId = replyService.createReply(principal.getName(), 
                replyForm.getBody(), ticketId);
        System.out.print("Reply to "+ ticketId);
        return "redirect:/ticket/view/" + ticketId;
    }
    
    @GetMapping("/reply/delete/{replyId}")
    public String deleteReply(@PathVariable("replyId") long replyId)
            throws ReplyNotFound {
        
        replyService.delete(replyId);
        return "/index";
    }


    @GetMapping("/view/{ticketId}")
    public String view(@PathVariable("ticketId") long ticketId,
            ModelMap model) {
        Ticket ticket = ticketService.getTicket(ticketId);
        if (ticket == null) {
            return "redirect:/ticket/list";
        }
        model.addAttribute("ticket", ticket);
        return "view";
    }

    @GetMapping("/{ticketId}/attachment/{attachment:.+}")
    public View download(@PathVariable("ticketId") long ticketId,
            @PathVariable("attachment") String name) {
        Attachment attachment = attachmentService.getAttachment(ticketId, name);
        if (attachment != null) {
            return new DownloadingView(attachment.getName(),
                    attachment.getMimeContentType(), attachment.getContents());
        }
        return new RedirectView("/ticket/list", true);
    }

    @GetMapping("/{ticketId}/delete/{attachment:.+}")
    public String deleteAttachment(@PathVariable("ticketId") long ticketId,
            @PathVariable("attachment") String name) throws AttachmentNotFound {
        ticketService.deleteAttachment(ticketId, name);
        return "redirect:/ticket/edit/" + ticketId;
    }

    @GetMapping("/edit/{ticketId}")
    public ModelAndView showEdit(@PathVariable("ticketId") long ticketId,
            Principal principal, HttpServletRequest request) {
        Ticket ticket = ticketService.getTicket(ticketId);
        if (ticket == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return new ModelAndView(new RedirectView("/index", true));
        }
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("ticket", ticket);
        Form ticketForm = new Form();
        ticketForm.setSubject(ticket.getSubject());
        ticketForm.setBody(ticket.getBody());
        modelAndView.addObject("ticketForm", ticketForm);
        return modelAndView;
    }

    @PostMapping("/edit/{ticketId}")
    public String edit(@PathVariable("ticketId") long ticketId, Form form,
            Principal principal, HttpServletRequest request)
            throws IOException, TicketNotFound {
        Ticket ticket = ticketService.getTicket(ticketId);
        if (ticket == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return "redirect:/ticket/list";
        }
        ticketService.updateTicket(ticketId, form.getSubject(),
                form.getBody(), form.getAttachments());
        return "redirect:/ticket/view/" + ticketId;
    }

    @GetMapping("/delete/{ticketId}")
    public String deleteTicket(@PathVariable("ticketId") long ticketId)
            throws TicketNotFound {
        ticketService.delete(ticketId);
        return "/index";
    }

}
