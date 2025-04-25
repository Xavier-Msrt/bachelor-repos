package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.ContactDTO.State;
import be.vinci.pae.business.dto.InternshipDTO;
import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import be.vinci.pae.dal.dao.ContactDAO;
import be.vinci.pae.dal.dao.InternshipDAO;
import be.vinci.pae.dal.dao.InternshipSupervisorDAO;
import be.vinci.pae.utils.ApplicationBinderTest;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InternshipUCCImplTest {

  private static InternshipDTO myInternshipDTO;
  private static ContactDTO contactDTOToUpdate;
  private static ContactDTO contactDTO;
  private static InternshipUCC myInternshipUCC;
  private static InternshipDAO myInternshipDAO;
  private static ContactDAO myContactDAO;
  private static InternshipSupervisorDAO myInternshipSupervisorsDAO;
  private static BizFactory myFactory;


  @BeforeAll
  static void beforeAll() {
    // setUp locator
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
    myInternshipUCC = locator.getService(InternshipUCC.class);
    myInternshipDAO = locator.getService(InternshipDAO.class);
    myContactDAO = locator.getService(ContactDAO.class);
    myInternshipSupervisorsDAO = locator.getService(InternshipSupervisorDAO.class);
    myFactory = locator.getService(BizFactory.class);
  }

  @BeforeEach
  void beforeEach() {
    // setup dto object
    myInternshipDTO = myFactory.getInternship();
    contactDTOToUpdate = myFactory.getContact();
    contactDTO = myFactory.getContact();
  }

  @AfterEach
  void afterEach() {
    // reset dao
    reset(myInternshipDAO);
    reset(myContactDAO);
    reset(myInternshipSupervisorsDAO);
  }

  @Test
  @DisplayName("Try to get the internship of student id")
  void getOneByStudentId() {
    // act
    Mockito.when(myInternshipDAO.getOneByStudentId(1)).thenReturn(myInternshipDTO);

    // assert
    assertEquals(myInternshipDTO, myInternshipUCC.getOneByStudentId(1));
    verify(myInternshipDAO).getOneByStudentId(1);
  }

  @Test
  @DisplayName("Try to get the count of internship")
  void getInternships() {
    Mockito.when(myInternshipDAO.getInternshipsCount()).thenReturn(6);

    // assert
    assertEquals(6, myInternshipUCC.getInternshipsCount());
    verify(myInternshipDAO).getInternshipsCount();
  }


  @Test
  @DisplayName("Try to get the count of internship by year")
  void getInternshipCountByYear() {
    Mockito.when(myInternshipDAO.getInternshipsCountByYear(1)).thenReturn(6);

    // assert
    assertEquals(6, myInternshipUCC.getInternshipsCountByYear(1));
    verify(myInternshipDAO).getInternshipsCountByYear(1);
  }


  @Test
  @DisplayName("Try to create as internship already exist ")
  void createOneWithInternship() {

    // act
    contactDTOToUpdate.setState(State.ACCEPTED);
    contactDTOToUpdate.setStudentIdContact(1);

    myInternshipDTO.setContactInternship(contactDTOToUpdate);
    myInternshipDTO.setIdInternship(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setSchoolYearIdInternship(1);
    myInternshipDTO.setInternSupervisorId(1);

    Mockito.when(myInternshipDAO.getOneByStudentId(1)).thenReturn(myInternshipDTO);

    // assert
    assertThrows(ContractException.class,
        () -> myInternshipUCC.createOneInternship(myInternshipDTO));
    verify(myInternshipDAO).getOneByStudentId(1);
  }

  @Test
  @DisplayName("Try to create the internship with no contact")
  void createOneWithNoContact() {

    // act
    contactDTOToUpdate.setState(State.ACCEPTED);
    contactDTOToUpdate.setStudentIdContact(1);
    contactDTOToUpdate.setCompanyIdContact(2);

    myInternshipDTO.setContactInternship(contactDTOToUpdate);
    myInternshipDTO.setIdInternship(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setSchoolYearIdInternship(1);
    myInternshipDTO.setInternSupervisorId(1);

    // assert
    assertThrows(NotFoundException.class,
        () -> myInternshipUCC.createOneInternship(myInternshipDTO));
    verify(myInternshipDAO).getOneByStudentId(1);
    verify(myContactDAO).getOneWithContactID(1, 2);
  }

  @Test
  @DisplayName("Try to create the internship with no contact admitted")
  void createOneWithNoContactAdmitted() {

    // act
    contactDTOToUpdate.setState(State.ACCEPTED);
    contactDTOToUpdate.setStudentIdContact(1);
    contactDTOToUpdate.setCompanyIdContact(2);

    ContactDTO contactDTOBeforeUpdate = myFactory.getContact();
    contactDTOBeforeUpdate.setStudentIdContact(1);
    contactDTOBeforeUpdate.setCompanyIdContact(2);
    contactDTOBeforeUpdate.setState(State.TURNED_DOWN);

    myInternshipDTO.setContactInternship(contactDTOToUpdate);
    myInternshipDTO.setIdInternship(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setSchoolYearIdInternship(1);
    myInternshipDTO.setInternSupervisorId(1);

    // assert
    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOBeforeUpdate);

    assertThrows(ContractException.class,
        () -> myInternshipUCC.createOneInternship(myInternshipDTO));
    verify(myInternshipDAO).getOneByStudentId(1);
    verify(myContactDAO).getOneWithContactID(1, 2);
  }

  @Test
  @DisplayName("Try to create the internship with different version")
  void createOneWithDifferentContactVersion() {

    // act
    contactDTOToUpdate.setState(State.ACCEPTED);
    contactDTOToUpdate.setStudentIdContact(1);
    contactDTOToUpdate.setCompanyIdContact(2);

    ContactDTO contactDTOBeforeUpdate = myFactory.getContact();
    contactDTOBeforeUpdate.setStudentIdContact(1);
    contactDTOBeforeUpdate.setCompanyIdContact(2);
    contactDTOBeforeUpdate.setState(State.ADMITTED);

    myInternshipDTO.setContactInternship(contactDTOToUpdate);
    myInternshipDTO.setIdInternship(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setSchoolYearIdInternship(1);
    myInternshipDTO.setInternSupervisorId(1);

    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOBeforeUpdate);

    // assert
    assertThrows(ConflictException.class,
        () -> myInternshipUCC.createOneInternship(myInternshipDTO));
    verify(myInternshipDAO).getOneByStudentId(1);
    verify(myContactDAO).getOneWithContactID(1, 2);
    verify(myContactDAO).updateStateWithContactID(contactDTOToUpdate);
  }

  @Test
  @DisplayName("Try to create the internship with no supervisor")
  void createOneWithNoSupervisor() {

    // act
    contactDTOToUpdate.setState(State.ACCEPTED);
    contactDTOToUpdate.setStudentIdContact(1);
    contactDTOToUpdate.setCompanyIdContact(2);

    ContactDTO contactDTOBeforeUpdate = myFactory.getContact();
    contactDTOBeforeUpdate.setStudentIdContact(1);
    contactDTOBeforeUpdate.setCompanyIdContact(2);
    contactDTOBeforeUpdate.setState(State.ADMITTED);

    ContactDTO contactDTOAfterUpdate = myFactory.getContact();
    contactDTOAfterUpdate.setStudentIdContact(1);
    contactDTOAfterUpdate.setCompanyIdContact(2);
    contactDTOAfterUpdate.setState(State.ACCEPTED);

    InternshipSupervisorDTO internshipSupervisorsDTO = myFactory.getInternshipSupervisor();
    internshipSupervisorsDTO.setIdSupervisor(1);

    myInternshipDTO.setContactInternship(contactDTOToUpdate);
    myInternshipDTO.setIdInternship(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setSchoolYearIdInternship(1);
    myInternshipDTO.setInternSupervisorId(1);

    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOBeforeUpdate);
    Mockito.when(myContactDAO.updateStateWithContactID(contactDTOToUpdate))
        .thenReturn(contactDTOAfterUpdate);

    // assert
    assertThrows(NotFoundException.class,
        () -> myInternshipUCC.createOneInternship(myInternshipDTO));
    verify(myInternshipDAO).getOneByStudentId(1);
    verify(myContactDAO).getOneWithContactID(1, 2);
    verify(myContactDAO).updateStateWithContactID(contactDTOToUpdate);
    verify(myInternshipSupervisorsDAO).getOneISWithID(1);
  }

  @Test
  @DisplayName("Try to create the internship")
  void createOne() {

    // act
    contactDTOToUpdate.setState(State.ACCEPTED);
    contactDTOToUpdate.setStudentIdContact(1);
    contactDTOToUpdate.setCompanyIdContact(2);

    ContactDTO contactDTOBeforeUpdate = myFactory.getContact();
    contactDTOBeforeUpdate.setStudentIdContact(1);
    contactDTOBeforeUpdate.setCompanyIdContact(2);
    contactDTOBeforeUpdate.setState(State.ADMITTED);

    ContactDTO contactDTOAfterUpdate = myFactory.getContact();
    contactDTOAfterUpdate.setStudentIdContact(1);
    contactDTOAfterUpdate.setCompanyIdContact(2);
    contactDTOAfterUpdate.setState(State.ACCEPTED);

    InternshipSupervisorDTO internshipSupervisorDTO = myFactory.getInternshipSupervisor();
    internshipSupervisorDTO.setIdSupervisor(1);

    myInternshipDTO.setContactInternship(contactDTOToUpdate);
    myInternshipDTO.setIdInternship(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setSchoolYearIdInternship(1);
    myInternshipDTO.setInternSupervisorId(1);

    InternshipDTO internshipDTO = myFactory.getInternship();
    internshipDTO.setContactInternship(contactDTOToUpdate);
    internshipDTO.setIdInternship(1);
    internshipDTO.setStudentIdInternship(1);
    internshipDTO.setSchoolYearIdInternship(1);
    internshipDTO.setInternSupervisorId(1);

    Mockito.when(myContactDAO.getOneWithContactID(1, 2)).thenReturn(contactDTOBeforeUpdate);
    Mockito.when(myContactDAO.updateStateWithContactID(contactDTOToUpdate))
        .thenReturn(contactDTOAfterUpdate);
    Mockito.when(myInternshipSupervisorsDAO.getOneISWithID(1)).thenReturn(internshipSupervisorDTO);
    Mockito.when(myInternshipDAO.createOneInternship(myInternshipDTO)).thenReturn(internshipDTO);

    // assert
    assertEquals(myInternshipUCC.createOneInternship(myInternshipDTO), internshipDTO);
    verify(myInternshipDAO).getOneByStudentId(1);
    verify(myContactDAO).getOneWithContactID(1, 2);
    verify(myContactDAO).updateStateWithContactID(contactDTOToUpdate);
    verify(myInternshipSupervisorsDAO).getOneISWithID(1);
    verify(myInternshipDAO).createOneInternship(myInternshipDTO);
  }


  @Test
  @DisplayName("Try to update the internship doesn't exist")
  void updateInternShipProjectDoesntExist() {

    // act
    contactDTO.setState(State.ACCEPTED);
    contactDTO.setStudentIdContact(1);
    contactDTO.setCompanyIdContact(2);

    myInternshipDTO.setContactInternship(contactDTO);
    myInternshipDTO.setIdInternship(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setSchoolYearIdInternship(1);
    myInternshipDTO.setInternSupervisorId(1);

    // assert
    assertThrows(NotFoundException.class,
        () -> myInternshipUCC.updateInternShipProject(myInternshipDTO));

    verify(myInternshipDAO).getOneByStudentId(1);
  }

  @Test
  @DisplayName("Try to update the internship with incorrect value (InternSupervisorId)")
  void updateInternShipProjectWithIncorrectValue() {

    // act
    contactDTO.setState(State.ACCEPTED);
    contactDTO.setStudentIdContact(1);
    contactDTO.setCompanyIdContact(2);

    myInternshipDTO.setContactInternship(contactDTO);
    myInternshipDTO.setInternSupervisorId(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setCompanyIdInternship(1);

    InternshipDTO internshipDTO = myFactory.getInternship();
    internshipDTO.setContactInternship(contactDTO);
    internshipDTO.setInternSupervisorId(2);

    Mockito.when(myInternshipDAO.getOneByStudentId(1)).thenReturn(internshipDTO);

    // assert
    assertThrows(ContractException.class,
        () -> myInternshipUCC.updateInternShipProject(myInternshipDTO));

    verify(myInternshipDAO).getOneByStudentId(1);
  }

  @Test
  @DisplayName("Try to update the internship with incorrect value (CompanyIdInternship)")
  void updateInternShipProjectWithIncorrectCompany() {

    // act
    contactDTO.setState(State.ACCEPTED);
    contactDTO.setStudentIdContact(1);
    contactDTO.setCompanyIdContact(2);

    myInternshipDTO.setContactInternship(contactDTO);
    myInternshipDTO.setInternSupervisorId(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setCompanyIdInternship(1);

    InternshipDTO internshipDTO = myFactory.getInternship();
    internshipDTO.setContactInternship(contactDTO);
    internshipDTO.setInternSupervisorId(1);
    myInternshipDTO.setCompanyIdInternship(2);

    Mockito.when(myInternshipDAO.getOneByStudentId(1)).thenReturn(internshipDTO);

    // assert
    assertThrows(ContractException.class,
        () -> myInternshipUCC.updateInternShipProject(myInternshipDTO));

    verify(myInternshipDAO).getOneByStudentId(1);
  }


  @Test
  @DisplayName("Try to update the internship with incorrect value (StudentIdInternship)")
  void updateInternShipProjectWithIncorrectStudent() {

    // act
    contactDTO.setState(State.ACCEPTED);
    contactDTO.setStudentIdContact(1);
    contactDTO.setCompanyIdContact(2);

    myInternshipDTO.setContactInternship(contactDTO);
    myInternshipDTO.setInternSupervisorId(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setCompanyIdInternship(1);

    InternshipDTO internshipDTO = myFactory.getInternship();
    internshipDTO.setContactInternship(contactDTO);
    internshipDTO.setInternSupervisorId(1);
    internshipDTO.setCompanyIdInternship(1);
    internshipDTO.setStudentIdInternship(2);

    Mockito.when(myInternshipDAO.getOneByStudentId(1)).thenReturn(internshipDTO);

    // assert
    assertThrows(ContractException.class,
        () -> myInternshipUCC.updateInternShipProject(myInternshipDTO));

    verify(myInternshipDAO).getOneByStudentId(1);
  }


  @Test
  @DisplayName("Try to update the internship with incorrect value (ContactInternship)")
  void updateInternShipProjectWithNoContact() {

    // act
    contactDTO.setState(State.ACCEPTED);
    contactDTO.setStudentIdContact(1);
    contactDTO.setCompanyIdContact(2);

    myInternshipDTO.setContactInternship(contactDTO);
    myInternshipDTO.setInternSupervisorId(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setCompanyIdInternship(1);

    InternshipDTO internshipDTO = myFactory.getInternship();
    internshipDTO.setContactInternship(contactDTO);
    internshipDTO.setInternSupervisorId(1);
    internshipDTO.setCompanyIdInternship(1);
    internshipDTO.setStudentIdInternship(1);

    Mockito.when(myInternshipDAO.getOneByStudentId(1)).thenReturn(internshipDTO);

    // assert
    assertThrows(NotFoundException.class,
        () -> myInternshipUCC.updateInternShipProject(myInternshipDTO));

    verify(myInternshipDAO).getOneByStudentId(1);
  }


  @Test
  @DisplayName("Try to update the internship with contact state not accepted")
  void updateInternShipProjectAccept() {

    // act
    contactDTO.setState(State.INTERRUPTED);
    contactDTO.setStudentIdContact(1);
    contactDTO.setCompanyIdContact(2);

    myInternshipDTO.setContactInternship(contactDTO);
    myInternshipDTO.setInternSupervisorId(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setCompanyIdInternship(1);

    InternshipDTO internshipDTO = myFactory.getInternship();
    internshipDTO.setContactInternship(contactDTO);
    internshipDTO.setInternSupervisorId(1);
    internshipDTO.setCompanyIdInternship(1);
    internshipDTO.setStudentIdInternship(1);

    Mockito.when(myInternshipDAO.getOneByStudentId(1)).thenReturn(internshipDTO);
    Mockito.when(myContactDAO.getOneWithContactID(1, 1)).thenReturn(contactDTO);

    // assert
    assertThrows(ContractException.class,
        () -> myInternshipUCC.updateInternShipProject(myInternshipDTO));

    verify(myInternshipDAO).getOneByStudentId(1);
    verify(myContactDAO).getOneWithContactID(1, 1);
  }

  @Test
  @DisplayName("Try to update the internship with bad version")
  void updateInternShipProjectVersion() {

    // act
    contactDTO.setState(State.ACCEPTED);
    contactDTO.setStudentIdContact(1);
    contactDTO.setCompanyIdContact(2);

    myInternshipDTO.setContactInternship(contactDTO);
    myInternshipDTO.setInternSupervisorId(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setCompanyIdInternship(1);

    InternshipDTO internshipDTO = myFactory.getInternship();
    internshipDTO.setContactInternship(contactDTO);
    internshipDTO.setInternSupervisorId(1);
    internshipDTO.setCompanyIdInternship(1);
    internshipDTO.setStudentIdInternship(1);

    Mockito.when(myInternshipDAO.getOneByStudentId(1)).thenReturn(internshipDTO);
    Mockito.when(myContactDAO.getOneWithContactID(1, 1)).thenReturn(contactDTO);

    // assert
    assertThrows(ConflictException.class,
        () -> myInternshipUCC.updateInternShipProject(myInternshipDTO));

    verify(myInternshipDAO).getOneByStudentId(1);
    verify(myContactDAO).getOneWithContactID(1, 1);
  }


  @Test
  @DisplayName("Try to update the internship with good value")
  void updateInternShipProject() {

    // act
    contactDTO.setState(State.ACCEPTED);
    contactDTO.setStudentIdContact(1);
    contactDTO.setCompanyIdContact(2);

    myInternshipDTO.setContactInternship(contactDTO);
    myInternshipDTO.setInternSupervisorId(1);
    myInternshipDTO.setStudentIdInternship(1);
    myInternshipDTO.setCompanyIdInternship(1);

    InternshipDTO internshipDTO = myFactory.getInternship();
    internshipDTO.setContactInternship(contactDTO);
    internshipDTO.setInternSupervisorId(1);
    internshipDTO.setCompanyIdInternship(1);
    internshipDTO.setStudentIdInternship(1);

    Mockito.when(myInternshipDAO.getOneByStudentId(1)).thenReturn(internshipDTO);
    Mockito.when(myContactDAO.getOneWithContactID(1, 1)).thenReturn(contactDTO);
    Mockito.when(myInternshipDAO.updateInternShipProject(myInternshipDTO))
        .thenReturn(internshipDTO);

    // assert
    assertEquals(internshipDTO, myInternshipUCC.updateInternShipProject(myInternshipDTO));

    verify(myInternshipDAO).getOneByStudentId(1);
    verify(myContactDAO).getOneWithContactID(1, 1);
    verify(myInternshipDAO).updateInternShipProject(myInternshipDTO);
  }


}