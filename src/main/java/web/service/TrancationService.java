package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import web.Repositories.AccRepo;
import web.Repositories.TransRepo;
import web.Repositories.UserRepo;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.TransactionType;
import web.domain.User;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrancationService {
    @Autowired
    TransRepo transRepo;
    @Autowired
    AccRepo accRepo;
    @Autowired
    UserRepo userRepo;

    public boolean newTransaction(Transaction transaction, User user) {
        transaction.setDate(LocalDate.now());
        transaction.setSender(user);

        switch (transaction.getType().name()) {
            case "SEND":
                List<Account> reciverAcc = accRepo.findByUser(transaction.getReciever());
                transaction.setRecieverAccount(reciverAcc.get(0));
                trancast(transaction);
                break;

            case "SELFTRANS":
                trancast(transaction);
                break;

            case "ADDFOUNDS":
                transaction.setReciever(user);
                specialTrans(transaction);
                break;
        }
        transRepo.save(transaction);
        /*if (trancast(transaction)) {
            transRepo.save(transaction);
        }*/
        return true;
    }

    public boolean trancast(Transaction transaction) {
        Account senderAccount = transaction.getSenderAccount();
        Account recieverAccount = transaction.getRecieverAccount();
        senderAccount.setAmmount(senderAccount.getAmmount().subtract(transaction.getAmmount()));
        recieverAccount.setAmmount(recieverAccount.getAmmount().add(transaction.getAmmount()));
        accRepo.save(senderAccount);
        accRepo.save(recieverAccount);
        return true;
    }

    public boolean specialTrans(Transaction transaction) {
        Account recieverAccount = transaction.getRecieverAccount();
        recieverAccount.setAmmount(recieverAccount.getAmmount().add(transaction.getAmmount()));
        return true;
    }

    public Model getSimpleTransactionList(User user, Pageable pageble, Model model, String link) {
        Page<Transaction> page;
        List<User> userList = userRepo.findAll();
        List<Account> accounts = accRepo.findByUser(user);

        page = transRepo.findBySenderRecieverId(user.getId(), pageble);
        model.addAttribute("users", userList);
        model.addAttribute("accounts", accounts);
        model.addAttribute("page", page);
        model.addAttribute("url", link);
        return model;
    }

}
