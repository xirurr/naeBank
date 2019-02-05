package web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
@ToString(of ={"id,date,ammount,sender,reciever"})
@EqualsAndHashCode(of={"id"})
public class Transaction {
    public Transaction() {
    }
    @Id
    @GeneratedValue
    private Long id;

    private Timestamp date;

    private BigDecimal ammount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="reciever_id")
    private User reciever;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
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
}
