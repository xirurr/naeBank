package web.service.IFaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.domain.User;

import java.math.BigDecimal;

public interface IUserService {
    public boolean addUser(User user);
    public boolean activateUser(User user);
    public BigDecimal getAccSumm(User user);
    public Page<User> getUsersWithSumm(Pageable pageble);
}
