package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import DAO.AccountDAO;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    AccountDAO accountDAO;

    public SocialMediaController(){
        AccountDAO accountDAO = new AccountDAO();
        this.accountService = new AccountService(accountDAO);
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
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        boolean success = accountService.register(account.getUsername(), account.getPassword());
        if(success){
            context.json(mapper.writeValueAsString(account));
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


}