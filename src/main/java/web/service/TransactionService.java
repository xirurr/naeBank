package web.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.Filter.Filter;
import web.Filter.SearchCriteria;
import web.Repositories.AccRepo;
import web.Repositories.TransRepo;
import web.Repositories.UserRepo;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.User;
import web.service.IFaces.ITransactionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {
    private final TransRepo transRepo;
    private final AccRepo accRepo;
    private final UserRepo userRepo;

    private final Filter filter;

    public TransactionService(TransRepo tr, AccRepo ar, UserRepo ur, Filter f) {
        this.transRepo = tr;
        this.accRepo = ar;
        this.userRepo = ur;
        this.filter = f;
    }

    @Override
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
                transaction.setReciever(transaction.getSender());
                trancast(transaction);
                break;

            case "ADDFOUNDS":
                transaction.setReciever(user);
                Account var = new Account();
                var.setId(1L);
                transaction.setSenderAccount(var);
                specialTrans(transaction);
                break;
        }
        transRepo.save(transaction);

        return true;
    }
    @Override
    public boolean trancast(Transaction transaction) {
        Account senderAccount = transaction.getSenderAccount();
        Account recieverAccount = transaction.getRecieverAccount();
        senderAccount.setAmmount(senderAccount.getAmmount().subtract(transaction.getAmmount()));
        recieverAccount.setAmmount(recieverAccount.getAmmount().add(transaction.getAmmount()));
        accRepo.save(senderAccount);
        accRepo.save(recieverAccount);
        return true;
    }
    @Override
    public boolean specialTrans(Transaction transaction) {
        Account recieverAccount = transaction.getRecieverAccount();
        recieverAccount.setAmmount(recieverAccount.getAmmount().add(transaction.getAmmount()));
        accRepo.save(recieverAccount);
        return true;
    }

    @Override
    public List<Transaction> getFilteredTransactions(User user, String idFilter, String datefilter, String ammount, String senderFilter, String recieverFilter) {
        String userId;
        List<Transaction> filteredAll;
        LocalDate datefilter1=null;
        LocalDate datefilter2=null;
        if (user == null) {
            userId = "%";
        } else {
            userId = user.getId().toString();
        }
        if (idFilter.equals("")) {
            idFilter = "%";
        } else {
            idFilter = "%" + idFilter + "%";
        }
        if (datefilter.equals("")) {
            datefilter1 = LocalDate.parse("1900-01-01");
            datefilter2 = LocalDate.parse("3000-01-01");
        } else {
            String[] split = datefilter.split("/");
            datefilter1 = LocalDate.parse(split[0]);
            datefilter2 = LocalDate.parse(split[1]);
        }
        if (ammount.equals("")) {
            ammount = "%";
        } else {
            ammount = "%" + ammount + "%";
        }
        senderFilter = getSenderRecieverFilterString(senderFilter);
        recieverFilter = getSenderRecieverFilterString(recieverFilter);
        if (senderFilter.equals("%АВТОПОПОЛНЕНИЕ%")) {
            filteredAll = transRepo.findAutoAddedFiltered(idFilter,  datefilter1,datefilter2, ammount, recieverFilter, userId);
        } else {
            filteredAll = transRepo.findFilteredAll(idFilter, datefilter1,datefilter2, ammount, senderFilter, recieverFilter, userId);
        }
        return filteredAll;
    }

    private String getSenderRecieverFilterString(String filterString) {
        if (filterString.equals("")) {
            filterString = "%";
        } else {
            filterString = "%" + filterString + "%";
        }
        return filterString;
    }

}
