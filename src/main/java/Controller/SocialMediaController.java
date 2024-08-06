package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages/{messageId}", this::getMessageByIdHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.patch("/messages/{messageId}", this::updateMessageHandler);
        app.delete("/messages/{messageId}", this::deleteMessageHandler);
        app.get("/accounts/{userId}/messages", this::getMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account success = accountService.userRegister(account.getUsername(), account.getPassword());
        if(success != null){
            context.json(mapper.writeValueAsString(success));
            context.status(200);
        } else {
            context.status(400);
        }    
    }

    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account credentials = mapper.readValue(context.body(), Account.class);
        Account account = accountService.login(credentials.getUsername(), credentials.getPassword());
        if(account != null){
            context.json(mapper.writeValueAsString(account));
            context.status(200);
        } else {
            context.status(401);
        }
    }

    private void createMessageHandler(Context context) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null) {
            context.json(mapper.writeValueAsString(addedMessage));
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void getMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("messageId"));
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            
        } else {
            context.json(message);
        }
    }

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void updateMessageHandler(Context context) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int messageId = Integer.parseInt(context.pathParam("messageId"));
        Message updatedMessage = messageService.updateMessage(messageId, message);
        if (updatedMessage != null) {
            context.json(mapper.writeValueAsString(updatedMessage));
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void deleteMessageHandler(Context context) throws Exception {
        int messageId = Integer.parseInt(context.pathParam("messageId"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        if (deletedMessage != null) {
            context.json(deletedMessage);
            context.status(200);
        } else {
            context.status(200);
            context.result("");
        }
    }

    private void getMessagesByUserHandler(Context context) {
        int userId = Integer.parseInt(context.pathParam("userId"));
        List<Message> messages = messageService.getMessagesByUserId(userId);
        if (messages == null) {
            context.status(404);
        } else {
            context.json(messages);
        }
    }
}