package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import web.Repositories.AccRepo;
import web.Repositories.TransRepo;
import web.Repositories.UserRepo;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.User;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
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
                transaction.setReciever(transaction.getSender());
                trancast(transaction);
                break;

            case "ADDFOUNDS":
                transaction.setReciever(user);
                specialTrans(transaction);
                break;
        }
        transRepo.save(transaction);

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

    public Model getUserTransList(User user, Pageable pageable, Model model, String link) {
        Page<Transaction> page;
        List<User> userList = userRepo.findAll();
        List<Account> accounts = accRepo.findByUser(user);
        page = transRepo.findBySenderRecieverId(user.getId(), pageable);
        model.addAttribute("users", userList);
        model.addAttribute("accounts", accounts);
        model.addAttribute("page", page);
        model.addAttribute("url", link);
        return model;
    }

    public Model getAllTransList(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble, Model model) {
        Page<Transaction> page;
        page = transRepo.findAll(pageble);
        List<Account> accountList = accRepo.findAll();
        List<User> userList = userRepo.findAll();
        model.addAttribute("page", page);
        model.addAttribute("url", "/transactions/all");
        model.addAttribute("accounts", accountList);
        model.addAttribute("users", userList);
        model.addAttribute("XMD", "true");
        return model;
    }

    public List<Transaction> getFilteredTransactions(User user, String idFilter, String datefilter, String ammount, String senderFilter, String recieverFilter) {
        List<Transaction> allTransList;
        if (user == null) {
            allTransList = transRepo.findAll();
        }
        else {
            allTransList = transRepo.findBySenderRecieverId(user.getId());
        }
        if (!idFilter.equals("")) {
            allTransList = filterById(allTransList, idFilter);
        }
        if (!datefilter.equals("")) {
            allTransList = filterByDate(allTransList, datefilter);
        }
        if (!ammount.equals("")) {
            allTransList = filterByAmmount(allTransList, ammount);
        }
        if (!senderFilter.equals("")) {
            if ("АВТОПОПОЛНЕНИЕ".contains(senderFilter)) {
                filterByNoSender(allTransList);
            } else {
                allTransList = filterBySender(allTransList, senderFilter);
            }
        }
        if (!recieverFilter.equals("")) {
            allTransList = filterByReciever(allTransList, recieverFilter);
        }
        return allTransList;

    }

    private List<Transaction> filterByNoSender(List<Transaction> allTransList) {
        allTransList.removeIf(o -> o.getSenderAccount() != null);
        return allTransList;
    }

    private List<Transaction> filterByReciever(List<Transaction> allTransList, String recieverFilter) {
        allTransList.removeIf(o ->
                !o.getReciever().toString().toLowerCase().contains(recieverFilter.toLowerCase()) &&
                        !o.getRecieverAccount().toString().toLowerCase().contains(recieverFilter.toLowerCase()));
        return allTransList;
    }

    private List<Transaction> filterBySender(List<Transaction> allTransList, String senderFilter) {
        allTransList.removeIf(o ->
                !o.getSender().toString().toLowerCase().contains(senderFilter.toLowerCase()) &&
                        (o.getSenderAccount()!=null && !o.getSenderAccount().toString().toLowerCase().contains(senderFilter.toLowerCase()))
                        );
        allTransList.removeIf(o->o.getSenderAccount()==null);
        return allTransList;
    }

    private List<Transaction> filterByAmmount(List<Transaction> allTransList, String ammount) {
        allTransList.removeIf(o -> !o.getAmmount().toString().contains(ammount));
        return allTransList;
    }

    private List<Transaction> filterByDate(List<Transaction> allTransList, String datefilter) {
        String[] split = datefilter.split("/");
        LocalDate one = LocalDate.parse(split[0]);
        LocalDate two = LocalDate.parse(split[1]);
        if (one.equals(two)){
            allTransList.removeIf(o->!o.getDate().equals(one));
        }

        allTransList.removeIf(o -> !(
                (o.getDate().isAfter(one)||o.getDate().isEqual(one)) &&
                        (o.getDate().isBefore(two)|| o.getDate().isEqual(two))));
        return allTransList;
    }

    public List<Transaction> filterById(List<Transaction> allTransList, String idFilter) {
        allTransList.removeIf(o -> !o.getId().toString().contains(idFilter));
        return allTransList;
    }
}
