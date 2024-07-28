package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public boolean register(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.length() <= 4) {
            return false;
        }
        Account existingAccount = accountDAO.getAccountByUsername(username);
        if (existingAccount != null) {
            return false;
        }
        Account newAccount = new Account(username, password);
        return accountDAO.createAccount(newAccount);
    }

    public Account login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }
}
