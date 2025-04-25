package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.ContactDTO.MeetingPlace;
import be.vinci.pae.business.dto.ContactDTO.State;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.utils.Utils;
import be.vinci.pae.dal.dao.CompanyDAO;
import be.vinci.pae.dal.dao.ContactDAO;
import be.vinci.pae.dal.dao.SchoolYearDAO;
import be.vinci.pae.utils.ApplicationBinderTest;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ContactUCCImplTest {

  private static ContactUCC myContactUCC;
  private static ContactDTO contactDTOToUpdate;
  private static ContactDTO contactDTOReturn;
  private static ContactDTO contactDTOToInsert;
  private static ContactDAO myContactDAO;
  private static CompanyDAO myCompanyDAO;
  private static SchoolYearDAO mySchoolYearDAO;
  private static Utils myUtils;
  private static CompanyDTO companyDTOToReturn;
  private static UserDTO userDTOToCreate;
  private static SchoolYearDTO schoolYearDTOToReturn;
  private static BizFactory myFactory;
  private static List<ContactDTO> contactList;


  @BeforeAll
  static void beforeAll() {

    // setUp locator
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());

    myContactUCC = locator.getService(ContactUCC.class);
    myFactory = locator.getService(BizFactory.class);
    myCompanyDAO = locator.getService(CompanyDAO.class);
    myContactDAO = locator.getService(ContactDAO.class);
    mySchoolYearDAO = locator.getService(SchoolYearDAO.class);
    myUtils = locator.getService(Utils.class);
  }

  @BeforeEach
  void beforeEach() {

    // setUp contactDTO getWithID(1,2)
    contactDTOToUpdate = myFactory.getContact();

    // setUp contact updateWith(ContactDTO c)
    contactDTOReturn = myFactory.getContact();

    // setUp list of contacts with student id 1
    contactList = List.of(contactDTOToUpdate);

    // setUp companyDTO to return getOneByID(2)
    companyDTOToReturn = myFactory.getCompany();

    // setUp userDTO
    userDTOToCreate = myFactory.getUser();

    // setUp schoolYear
    schoolYearDTOToReturn = myFactory.getSchoolYear();

    // setUp contact
    contactDTOToInsert = myFactory.getContact();

  }

  @AfterEach
  void afterEach() {
    // reset dao
    reset(mySchoolYearDAO);
    reset(myContactDAO);
    reset(myCompanyDAO);
  }

  @Test
  @DisplayName("Try to obtain an existing contact with the identifier")
  void getOneWithGoodID() {

    // act
    contactDTOToUpdate.setState(State.ACCEPTED);
    contactDTOToUpdate.setCompanyIdContact(2);
    contactDTOToUpdate.setStudentIdContact(1);

    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOToUpdate);
    ContactDTO contactToGet = myContactUCC.getOneWithID(1, 2);

    // assert
    assertEquals(contactDTOToUpdate, contactToGet);
    verify(myContactDAO).getOneWithContactID(1, 2);
  }

  @Test
  @DisplayName("Try to obtain a non-existing contact with the identifier")
  void getOneWithBadID() {

    // act
    ContactDTO contactToGet = myContactUCC.getOneWithID(2, 2);

    // assert
    assertNull(contactToGet);
    verify(myContactDAO).getOneWithContactID(2, 2);
  }

  @Test
  @DisplayName("Try to update to turned down without a reason of refusal")
  void updateOneWithOutReasonRefusal() {

    // act
    ContactDTO contactTest = myFactory.getContact();
    contactTest.setState(State.TURNED_DOWN);
    contactTest.setReasonRefusal(null);

    // assert
    assertThrows(ContractException.class, () -> myContactUCC.updateStateWithID(contactTest));
  }

  @Test
  @DisplayName("Try to update to admitted without a meeting place")
  void updateOneWithOutMeetingPlace() {

    // act
    ContactDTO contactTest = myFactory.getContact();
    contactTest.setState(State.ADMITTED);
    contactTest.setMeetingPlace(null);

    // assert
    assertThrows(ContractException.class, () -> myContactUCC.updateStateWithID(contactTest));
  }

  @Test
  @DisplayName("Try to update to accepted without an internship")
  void updateOneWithInAccepted() {

    // act
    ContactDTO contactTest = myFactory.getContact();
    contactTest.setState(State.ACCEPTED);
    contactTest.setMeetingPlace(null);

    // assert
    assertThrows(ContractException.class, () -> myContactUCC.updateStateWithID(contactTest));
  }

  @Test
  @DisplayName("Try to update a non-existing contact")
  void updateOneNotFound() {

    // act
    ContactDTO contactTest = myFactory.getContact();
    contactTest.setState(State.ADMITTED);
    contactTest.setMeetingPlace(MeetingPlace.REMOTE);
    contactTest.setSchoolYearIdContact(1);
    contactTest.setStudentIdContact(10);
    contactTest.setCompanyIdContact(10);
    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOToUpdate);

    // assert
    assertThrows(NotFoundException.class, () -> myContactUCC.updateStateWithID(contactTest));
    verify(myContactDAO).getOneWithContactID(10, 10);
  }

  @Test
  @DisplayName("Try to update not in process")
  void updateOneNotInProcess() {

    // act
    ContactDTO contactTest = myFactory.getContact();
    contactTest.setState(State.STARTED);
    contactTest.setMeetingPlace(MeetingPlace.REMOTE);
    contactTest.setSchoolYearIdContact(1);
    contactTest.setStudentIdContact(1);
    contactTest.setCompanyIdContact(2);
    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOToUpdate);

    // assert
    contactDTOToUpdate.setState(State.BLACKLISTED);
    contactDTOToUpdate.setMeetingPlace(null);
    assertThrows(ContractException.class, () -> myContactUCC.updateStateWithID(contactTest));
    verify(myContactDAO).getOneWithContactID(1, 2);
  }

  @Test
  @DisplayName("Try to update a contact with the same state")
  void updateOneWithSameState() {

    // act
    ContactDTO contactTest = myFactory.getContact();
    contactTest.setState(State.STARTED);
    contactTest.setMeetingPlace(null);
    contactTest.setSchoolYearIdContact(1);
    contactTest.setStudentIdContact(1);
    contactTest.setCompanyIdContact(2);

    // arrange
    contactDTOToUpdate.setState(State.STARTED);
    contactDTOToUpdate.setMeetingPlace(null);
    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOToUpdate);

    // assert
    assertThrows(ContractException.class, () -> myContactUCC.updateStateWithID(contactTest));
    verify(myContactDAO).getOneWithContactID(1, 2);
  }


  @Test
  @DisplayName("Try to update a contact to turned down but different version")
  void updateOneToTurnedDown() {

    // act
    ContactDTO contactTest = myFactory.getContact();
    contactTest.setState(State.TURNED_DOWN);
    contactTest.setReasonRefusal("No project available");
    contactTest.setSchoolYearIdContact(1);
    contactTest.setStudentIdContact(1);
    contactTest.setCompanyIdContact(2);

    // arrange
    contactDTOToUpdate.setVersionContact(1);
    contactDTOToUpdate.setStudentIdContact(1);
    contactDTOToUpdate.setCompanyIdContact(2);
    contactDTOToUpdate.setSchoolYearIdContact(1);
    contactDTOToUpdate.setState(State.ADMITTED);
    contactDTOToUpdate.setMeetingPlace(MeetingPlace.REMOTE);

    contactDTOReturn.setVersionContact(2);
    contactDTOReturn.setStudentIdContact(1);
    contactDTOReturn.setCompanyIdContact(2);
    contactDTOReturn.setSchoolYearIdContact(1);
    contactDTOReturn.setState(State.TURNED_DOWN);
    contactDTOReturn.setReasonRefusal("No project available");

    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOToUpdate);

    Mockito.when(myContactDAO.updateStateWithContactID(contactTest))
        .thenReturn(null);

    // assert
    assertThrows(ConflictException.class, () -> myContactUCC.updateStateWithID(contactTest));
    verify(myContactDAO).getOneWithContactID(1, 2);
    verify(myContactDAO).updateStateWithContactID(contactTest);
  }

  @Test
  @DisplayName("Try to update a contact to admitted")
  void updateOneToAdmitted() {

    // act
    ContactDTO contactTest = myFactory.getContact();
    contactTest.setState(State.ADMITTED);
    contactTest.setMeetingPlace(MeetingPlace.REMOTE);
    contactTest.setSchoolYearIdContact(1);
    contactTest.setStudentIdContact(1);
    contactTest.setCompanyIdContact(2);

    // arrange
    contactDTOToUpdate.setVersionContact(1);
    contactDTOToUpdate.setStudentIdContact(1);
    contactDTOToUpdate.setCompanyIdContact(2);
    contactDTOToUpdate.setSchoolYearIdContact(1);
    contactDTOToUpdate.setState(State.STARTED);
    contactDTOToUpdate.setMeetingPlace(null);

    contactDTOReturn.setVersionContact(2);
    contactDTOReturn.setStudentIdContact(1);
    contactDTOReturn.setCompanyIdContact(2);
    contactDTOReturn.setSchoolYearIdContact(1);
    contactDTOReturn.setState(State.ADMITTED);
    contactDTOReturn.setMeetingPlace(MeetingPlace.REMOTE);

    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOToUpdate);

    Mockito.when(myContactDAO.updateStateWithContactID(contactTest))
        .thenReturn(contactDTOReturn);

    // assert
    ContactDTO contactTestReturn = myContactUCC.updateStateWithID(contactTest);
    assertEquals(contactTestReturn, contactDTOReturn);
    verify(myContactDAO).getOneWithContactID(1, 2);
    verify(myContactDAO).updateStateWithContactID(contactTest);
  }

  @Test
  @DisplayName("Try to get the list of contacts of non-existant user")
  void getAllFromIdIdUserNotExist2() {

    // act
    Mockito.when(myContactDAO.getAllFromId(3)).thenReturn(null);

    // assert
    assertThrows(NotFoundException.class, () -> myContactUCC.getAllFromId(3));
    verify(myContactDAO).getAllFromId(3);
  }

  @Test
  @DisplayName("Try to get the list of contacts of a user")
  void getAllFromId() {

    // act
    Mockito.when(myContactDAO.getAllFromId(1)).thenReturn(contactList);

    // assert
    assertEquals(contactList, myContactUCC.getAllFromId(1));
    verify(myContactDAO).getAllFromId(1);
  }

  @Test
  @DisplayName("Try to create a contact but can found the company")
  void createOneWithNOCompany() {

    // act
    userDTOToCreate.setIdUser(1);

    // assert
    assertThrows(ContractException.class, () -> myContactUCC.createNewContact(3, userDTOToCreate));
    verify(myCompanyDAO).getOneById(3);
  }

  @Test
  @DisplayName("Try to create a contact but all ready exist")
  void createOneWithExist() {

    // act
    userDTOToCreate.setIdUser(1);

    companyDTOToReturn.setIdCompany(2);

    contactDTOToUpdate.setState(State.ACCEPTED);
    contactDTOToUpdate.setCompanyIdContact(2);
    contactDTOToUpdate.setStudentIdContact(1);

    Mockito.when(myCompanyDAO.getOneById(2)).thenReturn(companyDTOToReturn);
    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOToUpdate);

    // assert
    assertThrows(ConflictException.class, () -> myContactUCC.createNewContact(2, userDTOToCreate));
    verify(myCompanyDAO).getOneById(2);
    verify(myContactDAO).getOneWithContactID(1, 2);
  }

  @Test
  @DisplayName("Try to create a contact with no school year")
  void createOneNoSchoolYear() {

    // act
    userDTOToCreate.setIdUser(1);
    companyDTOToReturn.setIdCompany(2);

    schoolYearDTOToReturn.setIdSchoolYear(1);
    schoolYearDTOToReturn.setDateStart(2023);
    schoolYearDTOToReturn.setDateEnd(2024);

    contactDTOReturn.setState(State.STARTED);
    contactDTOReturn.setCompanyIdContact(2);
    contactDTOReturn.setStudentIdContact(1);
    contactDTOReturn.setSchoolYearIdContact(1);

    Mockito.when(myCompanyDAO.getOneById(2)).thenReturn(companyDTOToReturn);
    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(null);
    Mockito.when(mySchoolYearDAO.getOneByStartingDate(2023)).thenReturn(null);

    // assert
    assertThrows(FatalException.class, () -> myContactUCC.createNewContact(2, userDTOToCreate));
    verify(myCompanyDAO).getOneById(2);
    verify(myContactDAO).getOneWithContactID(1, 2);
  }

  @Test
  @DisplayName("Try to create a new contact")
  void createOne() {

    // act
    userDTOToCreate.setIdUser(1);
    companyDTOToReturn.setIdCompany(2);

    schoolYearDTOToReturn.setIdSchoolYear(1);
    schoolYearDTOToReturn.setDateStart(2023);
    schoolYearDTOToReturn.setDateEnd(2024);

    contactDTOReturn.setState(State.STARTED);
    contactDTOReturn.setCompanyIdContact(2);
    contactDTOReturn.setStudentIdContact(1);
    contactDTOReturn.setSchoolYearIdContact(1);

    contactDTOToInsert.setState(State.STARTED);
    contactDTOToInsert.setCompanyIdContact(2);
    contactDTOToInsert.setStudentIdContact(1);
    contactDTOToInsert.setStudentIdContact(1);

    Mockito.when(myCompanyDAO.getOneById(2)).thenReturn(companyDTOToReturn);
    Mockito.when(mySchoolYearDAO.getOneByStartingDate(2023)).thenReturn(schoolYearDTOToReturn);
    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(null);

    // assert
    assertNull(myContactUCC.createNewContact(2, userDTOToCreate));
    verify(myCompanyDAO).getOneById(2);
    verify(myContactDAO).getOneWithContactID(1, 2);
    verify(myCompanyDAO).getOneById(2);
  }

  @Test
  @DisplayName("Try to get all contacts of company by their id")
  void getAllContactFromCompanyId() {
    // act
    Mockito.when(myCompanyDAO.getOneById(1)).thenReturn(companyDTOToReturn);
    Mockito.when(myContactDAO.getAllContactByComapnyId(1)).thenReturn(contactList);

    // assert
    assertEquals(contactList, myContactUCC.getAllFromCompanyId(1));
    verify(myContactDAO).getAllContactByComapnyId(1);

  }

  @Test
  @DisplayName("Try to get all contacts with bad company id")
  void getAllContactFromBadCompanyId() {
    // act
    Mockito.when(myCompanyDAO.getOneById(1)).thenReturn(null);
    Mockito.when(myContactDAO.getAllContactByComapnyId(1)).thenReturn(contactList);

    // assert
    assertThrows(NotFoundException.class, () -> myContactUCC.getAllFromCompanyId(1));
    verify(myContactDAO, times(0)).getAllContactByComapnyId(1);

  }


}
