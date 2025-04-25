package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.InternshipSupervisorDTO;
import be.vinci.pae.dal.dao.CompanyDAO;
import be.vinci.pae.dal.dao.InternshipSupervisorDAO;
import be.vinci.pae.utils.ApplicationBinderTest;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InternshipSupervisorUCCImplTest {

  private static InternshipSupervisorDTO mySupervisorsDTO;
  private static InternshipSupervisorUCC mySupervisorUCC;
  private static InternshipSupervisorDAO mySupervisorDAO;
  private static CompanyDAO myCompanyDAO;
  private static BizFactory myFactory;

  @BeforeAll
  static void beforeAll() {
    // setUp locator
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
    mySupervisorUCC = locator.getService(InternshipSupervisorUCC.class);
    mySupervisorDAO = locator.getService(InternshipSupervisorDAO.class);
    myCompanyDAO = locator.getService(CompanyDAO.class);
    myFactory = locator.getService(BizFactory.class);
  }

  @BeforeEach
  void beforeEach() {
    // setup dto object
    mySupervisorsDTO = myFactory.getInternshipSupervisor();
  }

  @AfterEach
  void afterEach() {
    reset(mySupervisorDAO);
    reset(myCompanyDAO);
  }

  @Test
  @DisplayName("Try to add a supervisor existing")
  void addISAlreadyExist() {
    // act
    mySupervisorsDTO.setEmailSupervisor("test@vinci.be");

    Mockito.when(mySupervisorDAO.getOneInternshipSupervisorByEmail("test@vinci.be"))
        .thenReturn(mySupervisorsDTO);

    // assert
    assertThrows(ConflictException.class,
        () -> mySupervisorUCC.addInternshipSupervisors(mySupervisorsDTO));
    verify(mySupervisorDAO).getOneInternshipSupervisorByEmail("test@vinci.be");
  }

  @Test
  @DisplayName("Try to add a supervisor with no company")
  void addISWithNoCompany() {
    // act
    mySupervisorsDTO.setEmailSupervisor("test@vinci.be");
    mySupervisorsDTO.setCompanyIdSupervisor(1);

    // assert
    assertThrows(NotFoundException.class,
        () -> mySupervisorUCC.addInternshipSupervisors(mySupervisorsDTO));
    verify(mySupervisorDAO).getOneInternshipSupervisorByEmail("test@vinci.be");
    verify(myCompanyDAO).getOneById(1);
  }

  @Test
  @DisplayName("Try to add a supervisor")
  void addIS() {
    // act
    mySupervisorsDTO.setEmailSupervisor("test@vinci.be");
    mySupervisorsDTO.setCompanyIdSupervisor(1);

    CompanyDTO companyDTO = myFactory.getCompany();
    companyDTO.setIdCompany(1);

    Mockito.when(myCompanyDAO.getOneById(1)).thenReturn(companyDTO);
    Mockito.when(mySupervisorDAO.insertInternshipSupervisor(mySupervisorsDTO))
        .thenReturn(mySupervisorsDTO);

    // assert
    assertEquals(mySupervisorsDTO, mySupervisorUCC.addInternshipSupervisors(mySupervisorsDTO));
    verify(mySupervisorDAO).getOneInternshipSupervisorByEmail("test@vinci.be");
    verify(myCompanyDAO).getOneById(1);
    verify(mySupervisorDAO).insertInternshipSupervisor(mySupervisorsDTO);
  }

  @Test
  @DisplayName("Try to get a list of supervisor with no company")
  void getAllISWithNoCompany() {

    // assert
    assertThrows(NotFoundException.class, () -> mySupervisorUCC.getAllInternshipSupervisors(1));
    verify(myCompanyDAO).getOneById(1);
  }

  @Test
  @DisplayName("Try to get a list of supervisor")
  void getAllIS() {
    // act
    mySupervisorsDTO.setIdSupervisor(1);

    CompanyDTO companyDTO1 = myFactory.getCompany();
    companyDTO1.setIdCompany(1);

    List<InternshipSupervisorDTO> list = new ArrayList<>();
    list.add(mySupervisorsDTO);

    Mockito.when(myCompanyDAO.getOneById(1)).thenReturn(companyDTO1);
    Mockito.when(mySupervisorDAO.getAllISWithCompanyID(1)).thenReturn(list);

    // assert
    assertEquals(list, mySupervisorUCC.getAllInternshipSupervisors(1));
    verify(myCompanyDAO).getOneById(1);
    verify(mySupervisorDAO).getAllISWithCompanyID(1);
  }
}