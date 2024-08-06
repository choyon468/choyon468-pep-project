package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    private final MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message updateMessage(int message_id, Message message) {
        // Validate message content
        if(message.getMessage_text() == null || message.getMessage_text().isEmpty()) {
            return null;
        }
    
        if(message.getMessage_text().length() > 255) {
            return null;
        } 
    
        // Check if the message ID exists in the database
        if(messageDAO.getMessageById(message_id) != null){
            messageDAO.updateMessage(message_id, message);
            return messageDAO.getMessageById(message_id);
        } else {
            System.out.println("Message ID does not exist.");
            return null;
        }
    }

    public Message deleteMessageById(int messageId) {
        Message message = messageDAO.getMessageById(messageId);
        if (message != null) {
            messageDAO.deleteMessageById(messageId);
            return message;
        }
        return null;
    }

    public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }
}
