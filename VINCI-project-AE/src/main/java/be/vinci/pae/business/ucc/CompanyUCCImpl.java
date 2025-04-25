package be.vinci.pae.business.ucc;

import be.vinci.pae.business.BizFactory;
import be.vinci.pae.business.biz.Company;
import be.vinci.pae.business.biz.Contact;
import be.vinci.pae.business.dto.CompanyDTO;
import be.vinci.pae.business.dto.ContactDTO;
import be.vinci.pae.business.dto.SchoolYearDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.utils.Utils;
import be.vinci.pae.dal.DALBizService;
import be.vinci.pae.dal.dao.CompanyDAO;
import be.vinci.pae.dal.dao.ContactDAO;
import be.vinci.pae.dal.dao.SchoolYearDAO;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.ContractException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * The CompanyUCCImpl class is an implementation of the CompanyUCC interface and represents a
 * Company Use Case Controller (UCC) in the system. This interface is responsible for handling
 * contact-related operations.
 */
public class CompanyUCCImpl implements CompanyUCC {

  @Inject
  private CompanyDAO companyDAO;
  @Inject
  private ContactDAO contactDAO;
  @Inject
  private DALBizService dalBizServices;
  @Inject
  private BizFactory bizFactory;
  @Inject
  private Utils utils;
  @Inject
  private SchoolYearDAO schoolYearDAO;


  @Override
  public List<CompanyDTO> getAll() {
    return companyDAO.getAllCompanies();
  }

  @Override
  public List<CompanyDTO> getAllWithNumberInternshipWithoutYear() {
    return companyDAO.getAllWithNumberInternshipWithoutYear();
  }

  @Override
  public List<CompanyDTO> getAllWithNumberInternship(int yearId) {
    return companyDAO.getAllWithNumberInternship(yearId);
  }

  @Override
  public CompanyDTO getOneCompanyById(int id) {
    CompanyDTO company = companyDAO.getOneById(id);
    if (company == null) {
      throw new NotFoundException();
    }
    return company;
  }

  @Override
  public CompanyDTO insertNewCompany(UserDTO userDTO, CompanyDTO companyDTO) {
    CompanyDTO newCompany = null;

    // biz setup
    Company company = (Company) companyDTO;
    company.setupNewCompany();

    try {
      dalBizServices.startTransaction();

      // check company not exist
      CompanyDTO cmpDTO = companyDAO.getOneByTradeNameDesignation(companyDTO.getTradeName(),
          companyDTO.getDesignation());
      if (cmpDTO != null) {
        throw new ConflictException("COMPANY_EXISTS");
      }

      cmpDTO = companyDAO.getOneByEmail(companyDTO.getEmailCompany());
      if (companyDTO.getEmailCompany() != null && cmpDTO != null) {
        throw new ConflictException("COMPANY_EXISTS");
      }

      // insert
      newCompany = companyDAO.insertCompany(companyDTO);

      if (newCompany == null) {
        throw new FatalException("INTERNAL_SERVER_ERROR");
      }

      // get school year id
      int startYear = utils.currentSchoolYearStart();

      SchoolYearDTO schoolYearDTO = schoolYearDAO.getOneByStartingDate(startYear);
      if (schoolYearDTO == null) {
        throw new FatalException("INTERNAL_SERVER_ERROR");
      }

      ContactDTO contactDTO = bizFactory.getContact();
      Contact contact = (Contact) contactDTO;
      contact.setupNewContact(userDTO.getIdUser(), newCompany.getIdCompany());

      contact.setSchoolYearIdContact(schoolYearDTO.getIdSchoolYear());

      if (contactDAO.addNewContact(contactDTO) == null) {
        throw new FatalException("INTERNAL_SERVER_ERROR");
      }

      dalBizServices.commitTransaction();
    } catch (Exception e) {
      dalBizServices.rollbackTransaction();
      throw e;
    }
    return newCompany;
  }

  @Override
  public CompanyDTO blacklistOneCompany(CompanyDTO companyDTO) {

    CompanyDTO companyDTOUpdated = null;

    try {
      // check if company not already exist
      dalBizServices.startTransaction();

      CompanyDTO companyDTOBeforeUpdate = companyDAO.getOneById(companyDTO.getIdCompany());

      if (companyDTOBeforeUpdate == null) {
        throw new NotFoundException();
      }

      if (companyDTOBeforeUpdate.getIsBlacklisted()) {
        throw new ContractException("COMPANY_BLACKLIST");
      }

      companyDTOUpdated = companyDAO.blacklistCompany(companyDTO);

      if (companyDTOUpdated == null) {
        throw new ConflictException("CONFLICT_ERROR");
      }

      contactDAO.updateStateBlacklistFromCompany(companyDTOUpdated);

      dalBizServices.commitTransaction();
    } catch (Exception e) {
      dalBizServices.rollbackTransaction();
      throw e;
    }
    return companyDTOUpdated;
  }


}
