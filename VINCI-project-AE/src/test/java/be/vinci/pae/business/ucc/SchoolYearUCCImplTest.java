package be.vinci.pae.business.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.dal.dao.SchoolYearDAO;
import be.vinci.pae.utils.ApplicationBinderTest;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SchoolYearUCCImplTest {

  private static SchoolYearUCC mySchoolYearUCC;
  private static SchoolYearDAO mySchoolYearDAO;
  private static SchoolYearDTO mySchoolYearDTO;
  private static BizFactory myFactory;


  @BeforeAll
  static void beforeAll() {

    // setUp locator
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderTest());

    mySchoolYearUCC = locator.getService(SchoolYearUCC.class);
    mySchoolYearDAO = locator.getService(SchoolYearDAO.class);
    myFactory = locator.getService(BizFactory.class);

  }

  @BeforeEach
  void beforeEach() {
    // setUp object DTO
    mySchoolYearDTO = myFactory.getSchoolYear();
  }

  @AfterEach
  void afterEach() {
    // reset dao
    reset(mySchoolYearDAO);
  }

  @Test
  void getAll() {
    // act
    mySchoolYearDTO.setIdSchoolYear(1);
    List<SchoolYearDTO> list = new ArrayList<>();
    list.add(mySchoolYearDTO);

    Mockito.when(mySchoolYearDAO.getAll()).thenReturn(list);

    // assert
    assertEquals(list, mySchoolYearUCC.getAll());
    verify(mySchoolYearDAO).getAll();
  }
}