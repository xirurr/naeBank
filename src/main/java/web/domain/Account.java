package web.domain;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@EqualsAndHashCode(of = {"id"})
public class Account {
    public Account(User user) {
        this.user = user;
        ammount = BigDecimal.ZERO;
    }

    public Account() {
    }

    public Account(BigDecimal ammount, User user, String tag) {
        this.ammount = ammount;
        this.user = user;
        this.tag = tag;
    }

    @Id
    @GeneratedValue
    private Long id;


    private BigDecimal ammount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    private String tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public void setAmmount(BigDecimal ammount) {
        this.ammount = ammount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        if (tag != null) {
            return id + tag;
        }
        return id + "";
    }
}
