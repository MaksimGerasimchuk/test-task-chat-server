package service.impl;

import com.gerasimchuk.chat.domain.repo.api.UserRepository;
import com.gerasimchuk.chat.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void whenGetAllUsersThenMocksInvokedAccordingly() {
        // given
        when(userRepositoryMock.getAll()).thenReturn(List.of());

        //when
        var actual = userService.getAll();

        //then
        assertEquals(0, actual.size());
    }
}
