package web.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@EqualsAndHashCode(of={"id"})
public class Transaction {
    public Transaction() {
    }
    @Id
    //@Min(0)
    @GeneratedValue
    private Long id;

    private LocalDate date;


    @Min(1)
    private BigDecimal ammount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="sender_id")
    private User sender;

    @OneToOne
    @JoinColumn(name ="sender_account")
    private Account senderAccount;

    private TransactionType type;

    @OneToOne
    @JoinColumn(name ="reciever_account")
    private Account recieverAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="reciever_id")
    private User reciever;






    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public void setAmmount(BigDecimal ammount) {
        this.ammount = ammount;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public Account getRecieverAccount() {
        return recieverAccount;
    }

    public void setRecieverAccount(Account recieverAccount) {
        this.recieverAccount = recieverAccount;
    }


    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
