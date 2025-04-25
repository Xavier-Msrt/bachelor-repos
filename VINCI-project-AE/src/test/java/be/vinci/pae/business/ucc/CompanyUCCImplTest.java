package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.dto.CompanyDTO;
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
import java.util.ArrayList;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CompanyUCCImplTest {

  private static CompanyDTO companyDTO;
  private static UserDTO userDTO;
  private static SchoolYearDTO schoolYearDTO;

  private static BizFactory myFactory;
  private static CompanyUCC myCompanyUCC;
  private static CompanyDAO myCompanyDAO;
  private static SchoolYearDAO schoolYearDAO;
  private static ContactDAO contactDAO;
  private static Utils utils;


  @BeforeAll
  static void beforeAll() {
    // setUp locator
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());
    myFactory = locator.getService(BizFactory.class);
    myCompanyUCC = locator.getService(CompanyUCC.class);
    myCompanyDAO = locator.getService(CompanyDAO.class);
    schoolYearDAO = locator.getService(SchoolYearDAO.class);
    contactDAO = locator.getService(ContactDAO.class);
    utils = locator.getService(Utils.class);
  }

  @BeforeEach
  void beforeEach() {
    // setUp DTO object
    companyDTO = myFactory.getCompany();
    userDTO = myFactory.getUser();
    schoolYearDTO = myFactory.getSchoolYear();
  }

  @AfterEach
  void afterEach() {
    // reset the DAO
    reset(myCompanyDAO);
    reset(schoolYearDAO);
    reset(contactDAO);

  }

  @Test
  @DisplayName("Try to get a company that doesn't exist")
  void getOneCompanyNotExist() {
    // assert
    assertThrows(NotFoundException.class, () -> myCompanyUCC.getOneCompanyById(1));
    verify(myCompanyDAO).getOneById(1);
  }

  @Test
  @DisplayName("Try to get a company with id")
  void getOneCompanyWithId() {
    // act
    companyDTO.setIdCompany(1);
    Mockito.when(myCompanyDAO.getOneById(1)).thenReturn(companyDTO);

    // assert
    assertEquals(companyDTO, myCompanyUCC.getOneCompanyById(1));
    verify(myCompanyDAO).getOneById(1);
  }

  @Test
  @DisplayName("Try to get all company")
  void getAllCompanies() {
    // act
    ArrayList<CompanyDTO> companies = new ArrayList<>();
    CompanyDTO company2 = myFactory.getCompany();

    companies.add(companyDTO);
    companies.add(company2);

    Mockito.when(myCompanyDAO.getAllCompanies()).thenReturn(companies);

    // assert
    assertEquals(companies, myCompanyUCC.getAll());
    verify(myCompanyDAO).getAllCompanies();
  }

  @Test
  @DisplayName("Try to get all company with number of internship for all years")
  void getAllWithNumberInternshipWithoutYear() {
    // act
    ArrayList<CompanyDTO> companies = new ArrayList<>();
    CompanyDTO company2 = myFactory.getCompany();
    companies.add(companyDTO);
    companies.add(company2);

    Mockito.when(myCompanyDAO.getAllWithNumberInternshipWithoutYear()).thenReturn(companies);

    // act
    assertEquals(companies, myCompanyUCC.getAllWithNumberInternshipWithoutYear());
    verify(myCompanyDAO).getAllWithNumberInternshipWithoutYear();
  }

  @Test
  @DisplayName("Try to get all company with number of internship of a specific year")
  void getAllWithNumberInternship() {
    // act
    ArrayList<CompanyDTO> companies = new ArrayList<>();
    CompanyDTO company2 = myFactory.getCompany();
    companies.add(companyDTO);
    companies.add(company2);

    int idYear = 1;
    Mockito.when(myCompanyDAO.getAllWithNumberInternship(idYear)).thenReturn(companies);

    // act
    assertEquals(companies, myCompanyUCC.getAllWithNumberInternship(idYear));
    verify(myCompanyDAO).getAllWithNumberInternship(idYear);
  }

  @Test
  @DisplayName("Trying to insert a company that already exists with id")
  void insertNewCompanyAlreadyID() {
    // act
    companyDTO.setTradeName("Odoo");
    companyDTO.setDesignation("Farm 1");
    userDTO.setIdUser(1);
    Mockito.when(myCompanyDAO.getOneByTradeNameDesignation("Odoo", "Farm 1"))
        .thenReturn(companyDTO);

    // assert
    assertThrows(ConflictException.class, () -> myCompanyUCC.insertNewCompany(userDTO, companyDTO));
    verify(myCompanyDAO).getOneByTradeNameDesignation("Odoo", "Farm 1");
  }

  @Test
  @DisplayName("Trying to insert a company that already exists with email")
  void insertNewCompanyAlreadyExistEmail() {
    // act
    companyDTO.setEmailCompany("odoo@test.be");
    companyDTO.setTradeName("Odoo");
    companyDTO.setDesignation("Farm 1");
    userDTO.setIdUser(1);

    Mockito.when(myCompanyDAO.getOneByEmail("odoo@test.be"))
        .thenReturn(companyDTO);

    // assert
    assertThrows(ConflictException.class, () -> myCompanyUCC.insertNewCompany(userDTO, companyDTO));
    verify(myCompanyDAO).getOneByTradeNameDesignation("Odoo", "Farm 1");
    verify(myCompanyDAO).getOneByEmail("odoo@test.be");
  }


  @Test
  @DisplayName("Trying to insert a company fail company insert")
  void insertNewCompanyFailCompany() {
    // act
    companyDTO.setEmailCompany("odoo@test.be");
    companyDTO.setTradeName("Odoo");
    companyDTO.setDesignation("Farm 1");
    userDTO.setIdUser(1);
    Mockito.when(myCompanyDAO.getOneByTradeNameDesignation("Odoo", "Farm 1"))
        .thenReturn(null);

    // assert
    assertThrows(FatalException.class, () -> myCompanyUCC.insertNewCompany(userDTO, companyDTO));
    verify(myCompanyDAO).getOneByTradeNameDesignation("Odoo", "Farm 1");
    verify(myCompanyDAO).getOneByEmail("odoo@test.be");
  }


  @Test
  @DisplayName("Trying to insert a company")
  void insertNewCompany() {
    // act
    companyDTO.setTradeName("Odoo");
    companyDTO.setDesignation("Farm 1");
    userDTO.setIdUser(1);
    schoolYearDTO.setIdSchoolYear(1);
    Mockito.when(myCompanyDAO.getOneByTradeNameDesignation("Odoo", "Farm 1"))
        .thenReturn(null);
    Mockito.when(myCompanyDAO.insertCompany(companyDTO)).thenReturn(companyDTO);
    Mockito.when(schoolYearDAO.getOneByStartingDate(utils.currentSchoolYearStart()))
        .thenReturn(schoolYearDTO);
    // simulate return of the contact Mockito.any()) because the contact is created in the method
    Mockito.when(contactDAO.addNewContact(Mockito.any())).thenReturn(myFactory.getContact());

    // assert
    assertEquals(companyDTO, myCompanyUCC.insertNewCompany(userDTO, companyDTO));
    verify(myCompanyDAO).getOneByTradeNameDesignation("Odoo", "Farm 1");
    verify(myCompanyDAO).insertCompany(companyDTO);
    verify(schoolYearDAO).getOneByStartingDate(utils.currentSchoolYearStart());
  }

  @Test
  @DisplayName("Trying to insert a company fail SchoolYear")
  void insertNewCompanyFailSchoolYear() {
    // act
    companyDTO.setTradeName("Odoo");
    companyDTO.setDesignation("Farm 1");
    userDTO.setIdUser(1);
    schoolYearDTO.setIdSchoolYear(1);
    Mockito.when(myCompanyDAO.getOneByTradeNameDesignation("Odoo", "Farm 1"))
        .thenReturn(null);
    Mockito.when(myCompanyDAO.insertCompany(companyDTO)).thenReturn(companyDTO);

    // assert
    assertThrows(FatalException.class, () -> myCompanyUCC.insertNewCompany(userDTO, companyDTO));
    verify(myCompanyDAO).getOneByTradeNameDesignation("Odoo", "Farm 1");
    verify(myCompanyDAO).insertCompany(companyDTO);
    verify(schoolYearDAO).getOneByStartingDate(utils.currentSchoolYearStart());

  }

  @Test
  @DisplayName("Trying to insert a company but CompanyDAO have problem")
  void insertNewCompanyFatalException() {
    // act
    companyDTO.setTradeName("Odoo");
    companyDTO.setDesignation("Farm 1");
    userDTO.setIdUser(1);
    Mockito.when(myCompanyDAO.getOneByTradeNameDesignation("Odoo", "Farm 1")).thenThrow(
        FatalException.class);

    // assert
    assertThrows(FatalException.class, () -> myCompanyUCC.insertNewCompany(userDTO, companyDTO));
    verify(myCompanyDAO).getOneByTradeNameDesignation("Odoo", "Farm 1");
  }

  @Test
  @DisplayName("Trying to insert a company fail contact")
  void insertNewCompanyFailContact() {
    // act
    companyDTO.setTradeName("Odoo");
    companyDTO.setDesignation("Farm 1");
    userDTO.setIdUser(1);
    schoolYearDTO.setIdSchoolYear(1);
    Mockito.when(myCompanyDAO.getOneByTradeNameDesignation("Odoo", "Farm 1"))
        .thenReturn(null);
    Mockito.when(schoolYearDAO.getOneByStartingDate(utils.currentSchoolYearStart()))
        .thenReturn(schoolYearDTO);
    Mockito.when(myCompanyDAO.insertCompany(companyDTO)).thenReturn(companyDTO);

    // assert
    assertThrows(FatalException.class, () -> myCompanyUCC.insertNewCompany(userDTO, companyDTO));
    verify(myCompanyDAO).getOneByTradeNameDesignation("Odoo", "Farm 1");
    verify(myCompanyDAO).insertCompany(companyDTO);
    verify(schoolYearDAO).getOneByStartingDate(utils.currentSchoolYearStart());
  }


  @Test
  @DisplayName("Try to blacklist a company")
  public void blacklistOneCompanySuccess() {
    CompanyDTO companyDTO = myFactory.getCompany();
    companyDTO.setIdCompany(1);
    companyDTO.setIsBlacklisted(false);

    Mockito.when(myCompanyDAO.getOneById(companyDTO.getIdCompany())).thenReturn(companyDTO);
    Mockito.when(myCompanyDAO.blacklistCompany(companyDTO)).thenReturn(companyDTO);

    assertDoesNotThrow(() -> myCompanyUCC.blacklistOneCompany(companyDTO));
    verify(myCompanyDAO).getOneById(companyDTO.getIdCompany());
    verify(myCompanyDAO).blacklistCompany(companyDTO);
  }

  @Test
  @DisplayName("Try to blacklist a company that does not exist")
  public void blacklistOneCompanyNotExist() {
    CompanyDTO companyDTO = myFactory.getCompany();
    companyDTO.setIdCompany(1);
    companyDTO.setIsBlacklisted(true);

    Mockito.when(myCompanyDAO.getOneById(companyDTO.getIdCompany())).thenReturn(null);

    assertThrows(NotFoundException.class, () -> myCompanyUCC.blacklistOneCompany(companyDTO));
    verify(myCompanyDAO).getOneById(companyDTO.getIdCompany());
  }

  @Test
  @DisplayName("Try to blacklist a company that is already blacklisted")
  public void blacklistOneCompanyAlreadyBlacklisted() {
    CompanyDTO companyDTO = myFactory.getCompany();
    companyDTO.setIdCompany(1);
    companyDTO.setIsBlacklisted(true);

    Mockito.when(myCompanyDAO.getOneById(companyDTO.getIdCompany())).thenReturn(companyDTO);

    assertThrows(ContractException.class, () -> myCompanyUCC.blacklistOneCompany(companyDTO));
    verify(myCompanyDAO).getOneById(companyDTO.getIdCompany());
  }

  @Test
  @DisplayName("Try to blacklist a company but CompanyDAO have problem")
  public void blacklistOneCompanyFatalException() {
    CompanyDTO companyDTO = myFactory.getCompany();
    companyDTO.setIdCompany(1);
    companyDTO.setIsBlacklisted(false);

    Mockito.when(myCompanyDAO.getOneById(companyDTO.getIdCompany())).thenReturn(companyDTO);
    Mockito.when(myCompanyDAO.blacklistCompany(companyDTO)).thenThrow(FatalException.class);

    assertThrows(FatalException.class, () -> myCompanyUCC.blacklistOneCompany(companyDTO));
    verify(myCompanyDAO).getOneById(companyDTO.getIdCompany());
  }


  @Test
  @DisplayName("Try to blacklist a company but with a difference version")
  public void blacklistOneCompanyDifferenceVersion() {
    CompanyDTO companyDTO = myFactory.getCompany();
    companyDTO.setIdCompany(1);
    companyDTO.setIsBlacklisted(false);

    Mockito.when(myCompanyDAO.getOneById(companyDTO.getIdCompany())).thenReturn(companyDTO);

    assertThrows(ConflictException.class, () -> myCompanyUCC.blacklistOneCompany(companyDTO));
    verify(myCompanyDAO).getOneById(companyDTO.getIdCompany());
  }


}