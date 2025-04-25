import LoginPage from '../Pages/guest/LoginPage';
import registerPage from '../Pages/guest/RegisterPage';
import PersonalDataPage from "../Pages/user/PersonalDataPage";
import ContactPage from "../Pages/student/ContactPage";
import ContactDetailsPage from '../Pages/student/ContactDetailsPage';
import ContactAddPage from '../Pages/student/ContactAddPage';
import UserSearchPage from "../Pages/administrative/UserSearchPage";
import DashboardPage from "../Pages/teacher/DashboardPage";
import InternshipPage from "../Pages/student/InternshipPage";
import CompanyDetailsPage from "../Pages/teacher/CompanyDetailsPage";
import CompanyAddPage from "../Pages/student/CompanyAddPage";
import InternshipSupervisorAddPage
  from "../Pages/student/InternshipSupervisorAddPage";
import userDataPage from "../Pages/administrative/UserDataPage";
import InternShipPage from "../Pages/administrative/internshipDataPage";

const routes = {
  '/': ContactPage,
  '/login': LoginPage,
  '/register': registerPage,
  '/contact': ContactDetailsPage,
  '/personalData': PersonalDataPage,
  '/addContact': ContactAddPage,
  '/internship': InternshipPage,
  '/users': UserSearchPage,
  '/userData': userDataPage,
  '/internshipData': InternShipPage,
  '/dashboard': DashboardPage,
  '/addCompany': CompanyAddPage,
  '/addInternshipSupervisor': InternshipSupervisorAddPage,
  '/company': CompanyDetailsPage
};

export default routes;
