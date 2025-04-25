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
import be.vinci.pae.utils.exceptions.CodeExceptionMapper;
import be.vinci.pae.utils.exceptions.CodeExceptionMapperImpl;
import be.vinci.pae.utils.loggers.MyLogger;
import be.vinci.pae.utils.loggers.MyLoggerImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * A class that provides a binding between interfaces and their implementation classes.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  /**
   * Configure the binder. Bind the interfaces to the implementation classes.
   */
  @Override
  protected void configure() {
    bind(BizFactoryImpl.class).to(BizFactory.class).in(Singleton.class);

    bind(DALServiceImpl.class).to(DALService.class).to(DALBizService.class).in(Singleton.class);
    bind(MyLoggerImpl.class).to(MyLogger.class).in(Singleton.class);

    bind(UtilsImpl.class).to(Utils.class).in(Singleton.class);
    bind(CodeExceptionMapperImpl.class).to(CodeExceptionMapper.class).in(Singleton.class);

    // DAO
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(ContactDAOImpl.class).to(ContactDAO.class).in(Singleton.class);
    bind(CompanyDAOImpl.class).to(CompanyDAO.class).in(Singleton.class);
    bind(SchoolYearDAOImpl.class).to(SchoolYearDAO.class).in(Singleton.class);
    bind(InternshipDAOImpl.class).to(InternshipDAO.class).in(Singleton.class);
    bind(InternshipSupervisorDAOImpl.class).to(InternshipSupervisorDAO.class).in(Singleton.class);
    bind(SchoolYearDAOImpl.class).to(SchoolYearDAO.class).in(Singleton.class);
    bind(InternshipSupervisorDAOImpl.class).to(InternshipSupervisorDAO.class).in(Singleton.class);

    // UCC
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(CompanyUCCImpl.class).to(CompanyUCC.class).in(Singleton.class);
    bind(ContactUCCImpl.class).to(ContactUCC.class).in(Singleton.class);
    bind(InternshipUCCImpl.class).to(InternshipUCC.class).in(Singleton.class);
    bind(SchoolYearUCCImpl.class).to(SchoolYearUCC.class).in(Singleton.class);
    bind(InternshipSupervisorUCCImpl.class).to(InternshipSupervisorUCC.class).in(Singleton.class);
  }
}
