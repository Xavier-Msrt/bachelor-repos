package be.vinci.pae.utils;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.BizFactoryImpl;
import be.vinci.pae.business.ucc.CompanyUCC;
import be.vinci.pae.business.ucc.CompanyUCCImpl;
import be.vinci.pae.business.ucc.ContactUCC;
import be.vinci.pae.business.ucc.ContactUCCImpl;
import be.vinci.pae.business.ucc.InternshipSupervisorUCC;
import be.vinci.pae.business.ucc.InternshipSupervisorUCCImpl;
import be.vinci.pae.business.ucc.InternshipUCC;
import be.vinci.pae.business.ucc.InternshipUCCImpl;
import be.vinci.pae.business.ucc.SchoolYearUCC;
import be.vinci.pae.business.ucc.SchoolYearUCCImpl;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.ucc.UserUCCImpl;
import be.vinci.pae.business.utils.Utils;
import be.vinci.pae.business.utils.UtilsImpl;
import be.vinci.pae.dal.DALBizService;
import be.vinci.pae.dal.DALService;
import be.vinci.pae.dal.DALServiceImpl;
import be.vinci.pae.dal.dao.CompanyDAO;
import be.vinci.pae.dal.dao.CompanyDAOImpl;
import be.vinci.pae.dal.dao.ContactDAO;
import be.vinci.pae.dal.dao.ContactDAOImpl;
import be.vinci.pae.dal.dao.InternshipDAO;
import be.vinci.pae.dal.dao.InternshipDAOImpl;
import be.vinci.pae.dal.dao.InternshipSupervisorDAO;
import be.vinci.pae.dal.dao.InternshipSupervisorDAOImpl;
import be.vinci.pae.dal.dao.SchoolYearDAO;
import be.vinci.pae.dal.dao.SchoolYearDAOImpl;
import be.vinci.pae.dal.dao.UserDAO;
import be.vinci.pae.dal.dao.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

/**
 * Application binder.
 */
@Provider
public class ApplicationBinderTest extends AbstractBinder {

  /**
   * Configure method binding interface with their implementation.
   */
  @Override
  protected void configure() {
    bind(BizFactoryImpl.class).to(BizFactory.class).in(Singleton.class);
    bind(UtilsImpl.class).to(Utils.class).in(Singleton.class);
    bind(Mockito.mock(DALServiceImpl.class)).to(DALService.class).to(DALBizService.class);

    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
    bind(Mockito.mock(ContactDAOImpl.class)).to(ContactDAO.class);
    bind(Mockito.mock(SchoolYearDAOImpl.class)).to(SchoolYearDAO.class);
    bind(Mockito.mock(CompanyDAOImpl.class)).to(CompanyDAO.class);
    bind(Mockito.mock(InternshipDAOImpl.class)).to(InternshipDAO.class);
    bind(Mockito.mock(InternshipSupervisorDAOImpl.class)).to(InternshipSupervisorDAO.class);

    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(ContactUCCImpl.class).to(ContactUCC.class).in(Singleton.class);
    bind(CompanyUCCImpl.class).to(CompanyUCC.class).in(Singleton.class);
    bind(InternshipUCCImpl.class).to(InternshipUCC.class).in(Singleton.class);
    bind(InternshipSupervisorUCCImpl.class).to(InternshipSupervisorUCC.class).in(Singleton.class);
    bind(SchoolYearUCCImpl.class).to(SchoolYearUCC.class).in(Singleton.class);
  }
}