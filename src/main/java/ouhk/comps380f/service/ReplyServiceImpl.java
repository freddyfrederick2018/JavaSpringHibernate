package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ouhk.comps380f.dao.ReplyRepository;
import ouhk.comps380f.dao.TicketRepository;
import ouhk.comps380f.exception.ReplyNotFound;
import ouhk.comps380f.model.Reply;
import ouhk.comps380f.model.Ticket;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Resource
    private ReplyRepository replyRepo;
    
    @Resource
    private TicketRepository ticketRepo;
    
    @Override
    @Transactional
    public List<Reply> getReplies(long ticketId) {
        return replyRepo.findAll();
    }

    @Override
    @Transactional
    public Reply getReply(long id) {
        return replyRepo.findById(id).orElse(null);
    }
    
    public List<Ticket> getTickets() {
        return ticketRepo.findAll();
    }

    public Ticket getTicket(long id) {
        return ticketRepo.findById(id).orElse(null);
    }

    

    @Override
    @Transactional(rollbackFor = ReplyNotFound.class)
    public void delete(long id) throws ReplyNotFound {
        Reply deletedReply = replyRepo.findById(id).orElse(null);
        if (deletedReply == null) {
            throw new ReplyNotFound();
        }
        replyRepo.delete(deletedReply);
    }

    @Override
    @Transactional
    public long createReply(String customerName,
            String body, long ticketid) throws IOException {
        Reply reply = new Reply();
        reply.setCustomerName(customerName);
        reply.setBody(body);
        Reply savedReply = replyRepo.save(reply);
        return savedReply.getId();
    }

    @Override
    @Transactional(rollbackFor = ReplyNotFound.class)
    public void updateReply(long id, String body)
            throws IOException, ReplyNotFound {
        Reply updatedTicket = replyRepo.findById(id).orElse(null);
        if (updatedTicket == null) {
            throw new ReplyNotFound();
        }
        
        updatedTicket.setBody(body);
        
        replyRepo.save(updatedTicket);
    }
}
