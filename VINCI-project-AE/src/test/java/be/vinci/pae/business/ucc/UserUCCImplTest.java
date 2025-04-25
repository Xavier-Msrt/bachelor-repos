package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.biz.User;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.UserDTO.Role;
import be.vinci.pae.dal.dao.SchoolYearDAO;
import be.vinci.pae.dal.dao.UserDAO;
import be.vinci.pae.utils.ApplicationBinderTest;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import java.util.ArrayList;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserUCCImplTest {

  private static UserUCC myUserUCC;
  private static UserDTO userDTO;
  private static User user;

  private static SchoolYearDTO schoolYearDTO;
  private static UserDAO myUserDAO;
  private static BizFactory myFactory;
  private static SchoolYearDAO mySchoolYearDAO;


  @BeforeAll
  static void beforeAll() {

    // setUp locator
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
    myUserUCC = locator.getService(UserUCC.class);
    myFactory = locator.getService(BizFactory.class);
    myUserDAO = locator.getService(UserDAO.class);
    mySchoolYearDAO = locator.getService(SchoolYearDAO.class);


  }

  @BeforeEach
  void beforeEach() {
    // setUp DTO object
    userDTO = myFactory.getUser();
    schoolYearDTO = myFactory.getSchoolYear();

  }

  @AfterEach
  void afterEach() {
    // reset DAO
    Mockito.reset(myUserDAO);
    Mockito.reset(mySchoolYearDAO);
  }


  // Login
  @Test
  @DisplayName("Try to login with good values")
  void login() {
    // act
    user = (User) userDTO;
    userDTO.setEmailUser("chisom.ubah@student.vinci.be");
    userDTO.setPassword(user.hashPassword("12345"));
    Mockito.when(myUserDAO.getOneByEmail("chisom.ubah@student.vinci.be")).thenReturn(userDTO);

    // assert
    assertEquals(userDTO, myUserUCC.login("chisom.ubah@student.vinci.be", "12345"));
    verify(myUserDAO).getOneByEmail("chisom.ubah@student.vinci.be");
  }

  @Test
  @DisplayName("Try to login with wrong password")
  void loginWrongPassword() {
    // act
    user = (User) userDTO;
    userDTO.setEmailUser("chisom.ubah@student.vinci.be");
    userDTO.setPassword(user.hashPassword("12345"));
    Mockito.when(myUserDAO.getOneByEmail("chisom.ubah@student.vinci.be")).thenReturn(userDTO);

    // assert
    assertThrows(ContractException.class,
        () -> myUserUCC.login("chisom.ubah@student.vinci.be", "0000"));
    verify(myUserDAO).getOneByEmail("chisom.ubah@student.vinci.be");
  }

  @Test
  @DisplayName("Try to login with non-existant user")
  void loginUserNotExist() {
    // act
    Mockito.when(myUserDAO.getOneByEmail("johachim.qi@student.vinci.be")).thenReturn(null);

    // assert
    assertThrows(NotFoundException.class, () -> {
      myUserUCC.login("johachim.qi@student.vinci.be", "12345");
    });
    verify(myUserDAO).getOneByEmail("johachim.qi@student.vinci.be");
  }


  //  GetOneByIdUser
  @Test
  @DisplayName("Try to get user info of non-existant user")
  void getOneByIdUserNotExist() {
    // act
    Mockito.when(myUserDAO.getOneById(2)).thenReturn(null);

    // assert
    assertNull(myUserUCC.getOneById(1));
    verify(myUserDAO).getOneById(1);
  }

  @Test
  @DisplayName("Try to get user info of existant user")
  void getOneByIdUserExistant() {
    // act
    Mockito.when(myUserDAO.getOneById(2)).thenReturn(userDTO);
    // assert
    assertEquals(userDTO, myUserUCC.getOneById(2));
    verify(myUserDAO).getOneById(2);
  }

  // GetAllUser
  @Test
  @DisplayName("Try to get all user")
  void getAllUsers() {
    // act
    ArrayList<UserDTO> users = new ArrayList<>();
    UserDTO userDTO2 = myFactory.getUser();

    userDTO2.setEmailUser("xavier.massart@student.vinci.be");
    users.add(userDTO);
    users.add(userDTO2);

    Mockito.when(myUserDAO.getAllUsers()).thenReturn(users);

    // assert
    assertEquals(users, myUserUCC.getAllUsers());
    verify(myUserDAO).getAllUsers();
  }

  // GetAllUser
  @Test
  @DisplayName("Try to get all user count")
  void getAllUsersCount() {
    // act
    Mockito.when(myUserDAO.getUsersCount()).thenReturn(10);

    // assert
    assertEquals(10, myUserUCC.getUsersCount());
    verify(myUserDAO).getUsersCount();
  }

  @Test
  @DisplayName("Try to get all user")
  void getUserCountByYear() {
    // act
    Mockito.when(myUserDAO.getUsersCountByYear(2024)).thenReturn(24);

    // assert
    assertEquals(24, myUserUCC.getUsersCountByYear(2024));
    verify(myUserDAO).getUsersCountByYear(2024);
  }

  // Register
  @Test
  @DisplayName("Try to register with bad email role combo")
  void registerWithBadComboEmailRole() {
    // act
    userDTO.setEmailUser("prof.prof@vinci.be");
    userDTO.setRole(Role.valueOf("STUDENT"));

    // assert
    assertThrows(ContractException.class, () -> {
      myUserUCC.register(userDTO);
    });
    verify(myUserDAO, times(0)).insertUser(userDTO);
  }

  @Test
  @DisplayName("Try to register with en email that already exist")
  void registerWithEmailExist() {
    // act
    userDTO.setEmailUser("chisom.ubah@student.vinci.be");
    Mockito.when(myUserDAO.getOneByEmail("chisom.ubah@student.vinci.be")).thenReturn(userDTO);
    UserDTO testUser = myFactory.getUser();
    testUser.setEmailUser("chisom.ubah@student.vinci.be");
    testUser.setPassword("12345");
    testUser.setRole(Role.valueOf("STUDENT"));

    // assert
    assertThrows(ContractException.class, () -> {
      myUserUCC.register(testUser);
    });

    verify(myUserDAO, times(0)).insertUser(userDTO);
  }

  @Test
  @DisplayName("Try to register with good value")
  void registerWithGoodValue() {
    // act
    userDTO.setEmailUser("chisom.ubah@student.vinci.be");
    userDTO.setPassword("12345");
    userDTO.setRole(Role.valueOf("STUDENT"));
    Mockito.when(myUserDAO.getOneByEmail("chisom.ubah@student.vinci.be")).thenReturn(null);
    Mockito.when(myUserDAO.insertUser(userDTO)).thenReturn(userDTO);
    Mockito.when(mySchoolYearDAO.getOneByStartingDate(2023)).thenReturn(schoolYearDTO);

    // act
    assertEquals(userDTO, myUserUCC.register(userDTO));
    verify(mySchoolYearDAO).getOneByStartingDate(2023);

  }

  @Test
  @DisplayName("Try to register with good value")
  void registerWithBadSchoolYear() {
    // act
    userDTO.setEmailUser("chisom.ubah@student.vinci.be");
    userDTO.setPassword("12345");
    userDTO.setRole(Role.valueOf("STUDENT"));
    Mockito.when(myUserDAO.getOneByEmail("chisom.ubah@student.vinci.be")).thenReturn(null);
    Mockito.when(myUserDAO.insertUser(userDTO)).thenReturn(userDTO);
    Mockito.when(mySchoolYearDAO.getOneByStartingDate(2023)).thenReturn(null);

    // assert
    assertThrows(NotFoundException.class, () -> myUserUCC.register(userDTO));
    verify(mySchoolYearDAO).getOneByStartingDate(2023);
    verify(myUserDAO, times(0)).insertUser(userDTO);
  }

  @Test
  @DisplayName("Try to update user phone number")
  void updateUserPhoneNumberWithSamePhoneNumber() {
    // act
    userDTO.setPhoneNumberUser("0123456");
    userDTO.setIdUser(1);

    Mockito.when(myUserDAO.getOneById(1)).thenReturn(userDTO);

    //assert
    assertThrows(ContractException.class, () -> myUserUCC.updateUserPhoneNumber(userDTO));
    verify(myUserDAO).getOneById(1);
  }

  @Test
  @DisplayName("Try to update user with different version")
  void updateUserPhoneNumberWithDifferentVersion() {
    // act
    userDTO.setPhoneNumberUser("0123456");
    userDTO.setIdUser(1);
    userDTO.setVersionUser(1);

    UserDTO userDTO1 = myFactory.getUser();
    userDTO1.setPhoneNumberUser("7894561230");

    Mockito.when(myUserDAO.getOneById(1)).thenReturn(userDTO1);

    //assert
    assertThrows(ConflictException.class, () -> myUserUCC.updateUserPhoneNumber(userDTO));
    verify(myUserDAO).getOneById(1);
    verify(myUserDAO).updateUserPhoneNumber(userDTO);
  }

  @Test
  @DisplayName("Try to update user")
  void updateUserPhoneNumber() {
    // act
    userDTO.setPhoneNumberUser("0123456");
    userDTO.setIdUser(1);
    userDTO.setVersionUser(1);

    UserDTO userDTO1 = myFactory.getUser();
    userDTO1.setPhoneNumberUser("7894561230");

    Mockito.when(myUserDAO.getOneById(1)).thenReturn(userDTO1);
    Mockito.when(myUserDAO.updateUserPhoneNumber(userDTO)).thenReturn(userDTO);

    //assert
    assertEquals(userDTO, myUserUCC.updateUserPhoneNumber(userDTO));
    verify(myUserDAO).getOneById(1);
    verify(myUserDAO).updateUserPhoneNumber(userDTO);
  }


  @Test
  @DisplayName("Try to update update password")
  void updatePassword() {
    // act
    user = (User) userDTO;
    userDTO.setIdUser(1);
    userDTO.setVersionUser(1);

    UserDTO userDTOReturn = myFactory.getUser();
    userDTOReturn.setIdUser(1);
    userDTOReturn.setVersionUser(1);
    userDTOReturn.setPassword(user.hashPassword("12345"));

    Mockito.when(myUserDAO.getOneById(1)).thenReturn(userDTOReturn);
    Mockito.when(myUserDAO.updateUserPassword(userDTO)).thenReturn(userDTOReturn);

    //assert
    assertEquals(userDTOReturn, myUserUCC.updatePassword(userDTO, "12345", "12345"));
    verify(myUserDAO).getOneById(1);
    verify(myUserDAO).updateUserPassword(userDTO);
  }


  @Test
  @DisplayName("Try to update update password with bad old password")
  void updatePasswordWithBadPassword() {
    // act
    user = (User) userDTO;
    userDTO.setIdUser(1);
    userDTO.setVersionUser(1);

    UserDTO userDTOReturn = myFactory.getUser();
    userDTOReturn.setIdUser(1);
    userDTOReturn.setVersionUser(1);
    userDTOReturn.setPassword(user.hashPassword("1234"));

    Mockito.when(myUserDAO.getOneById(1)).thenReturn(userDTOReturn);

    //assert
    assertThrows(ContractException.class,
        () -> myUserUCC.updatePassword(userDTO, "4545", "00000"));

    verify(myUserDAO).getOneById(1);
  }


  @Test
  @DisplayName("Try to update update password with error")
  void updatePasswordWithError() {
    // act
    user = (User) userDTO;
    userDTO.setIdUser(1);
    userDTO.setVersionUser(1);

    UserDTO userDTOReturn = myFactory.getUser();
    userDTOReturn.setIdUser(1);
    userDTOReturn.setVersionUser(1);
    userDTOReturn.setPassword(user.hashPassword("12345"));

    Mockito.when(myUserDAO.getOneById(1)).thenReturn(userDTOReturn);
    Mockito.when(myUserDAO.updateUserPassword(userDTO)).thenThrow(FatalException.class);

    //assert
    assertThrows(FatalException.class, () -> myUserUCC.updatePassword(userDTO, "12345", "12345"));
    verify(myUserDAO).getOneById(1);
    verify(myUserDAO).updateUserPassword(userDTO);
  }

}
