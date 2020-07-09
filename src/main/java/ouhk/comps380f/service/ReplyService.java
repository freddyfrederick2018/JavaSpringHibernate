package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import ouhk.comps380f.exception.ReplyNotFound;
import ouhk.comps380f.model.Reply;

public interface ReplyService {

    public long createReply(String customerName, String body, long ticketid) throws IOException;
    
    public List<Reply> getReplies(long ticket_id);
    
    public Reply getReply(long id);

    public void updateReply(long id, String body)
            throws IOException, ReplyNotFound;

    public void delete(long id) throws ReplyNotFound;

}
