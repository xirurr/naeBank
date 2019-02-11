package web.service;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import web.Repositories.AccRepo;
import web.Repositories.UserRepo;
import web.domain.Account;
import web.domain.Role;
import web.domain.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private AccRepo realAccRepo;

    @MockBean
    private UserRepo userRepo;


    @MockBean
    private AccountService accountService;

    @MockBean
    private AccRepo accRepo;

    @Test
    public void addUser() {
        User user = new User();

        boolean created = userService.addUser(user);
        Assert.assertTrue(created);
        Assert.assertTrue(user.isActive());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(accountService, Mockito.times(1)).addUserAccount(user);
    }

    @Test
    public void addUserFailTest() {
        User user = new User();
        user.setUsername("Ivan");
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("Ivan");
        boolean isUserCreated = userService.addUser(user);
        Assert.assertFalse(isUserCreated);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void getAccSumm() {
        User user = new User();
        Account one = new Account(BigDecimal.valueOf(1000), user, "first");
        Account two = new Account(BigDecimal.valueOf(730), user, "second");
        List<Account> accs = new ArrayList<>();
        accs.add(one);
        accs.add(two);
        Mockito.doReturn(accs)
                .when(accRepo)
                .findByUser(user);
        BigDecimal summ = userService.getAccSumm(user);
        Assert.assertEquals(summ.toString(), "1730");
    }

    @Test
    public void getUsersWithSumm() {

        Model model = new ExtendedModelMap();
        model.addAttribute("test", "test");
        Pageable pageable = PageRequest.of(0, 2);

        User user = new User();
        Account one = new Account(BigDecimal.valueOf(1000), user, "first");
        User user2 = new User();
        Account two = new Account(BigDecimal.valueOf(2000), user2, "first");

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        List<Account> accs = new ArrayList<>();
        accs.add(one);
        List<Account> accs2 = new ArrayList<>();
        accs2.add(two);

        Page<User> page = new PageImpl<>(users);

        Mockito.doReturn(accs)
                .doReturn(accs2)
                .when(accRepo)
                .findByUser(user);


        Mockito.doReturn(page)
                .when(userRepo)
                .findAll(pageable);

        ExtendedModelMap asdf = (ExtendedModelMap) userService.getUsersWithSumm(pageable, model);

        Page<User> page2 = (Page<User>) asdf.get("page");

        Assert.assertEquals(page2.getContent().get(0).getSumm().toString(), "1000");
        Assert.assertEquals(page2.getContent().get(1).getSumm().toString(), "2000");
    }
}