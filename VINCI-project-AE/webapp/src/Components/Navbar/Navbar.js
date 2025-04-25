import logo from '../../img/logo_PAE.png'
import avatarPicture from '../../img/avatarinternify.png';
import signOutIcon from '../../img/signout_icon.svg';
import Navigate from "../Router/Navigate";
import {
  getToken,
  getUser,
  isAdministrative,
  isStudent,
  isTeacher,
  roleEnToFr, setUser
} from "../../utils/user";
import {getInternship, setInternship} from "../../utils/internship";

const Navbar = () => {
  if (getToken() == null) {
    Navigate('/login');
  }

  const user = getUser();
  renderNavbarPage(user);
  renderNavbarButtons();

  disconnectListenner();
  logoListenner();
  profileListenner();
}

function renderNavbarPage(user) {

  const roleFr = roleEnToFr(user.role);

  const navbarWrapper = document.querySelector('#navbarWrapper');
  navbarWrapper.innerHTML = `
  
  <div class="d-flex justify-content-between align-items-center position-relative m-3 mt-3 mb-0">
    <img type="button" id="navbar-logo" class='mx-auto' src=${logo} draggable="false">
    <div class="navbar-profil position-absolute">
    
      <div class="row">
        <div class="col">
          <div class="d-flex flex-column">
            <!--adapt the role and the name according to the user-->
            <span id="navbar-role" class="mx-auto">${roleFr}</span>
            <span id="navbar-username">${user.lastNameUser} ${user.firstNameUser}</span>
          </div>
        </div>
        <div class="col">
          <img type="button" id="navbar-profil-avatar" src="${avatarPicture}" alt="Avatar" draggable="false">
        </div>
        <div class="col">
          <img type="button" class="navbar-profil-signout" src="${signOutIcon}" alt="Sign Out" draggable="false">
        </div>
      </div>
          
    </div>
  </div>
         
  <nav class="navbar navbar-expand-lg navbar-light">
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
  
      <ul id="navbar-role-buttons" class="navbar-nav ms-5"></ul>
  
    </div>
  </nav>
  `;
}

function renderNavbarButtons() {
  if (isStudent()) {
    renderStudentButtons();
  } else if (isTeacher()) {
    renderTeacherButtons();
  } else if (isAdministrative()) {
    renderAdminButtons();
  }
}

function renderStudentButtons() {
  const navbarWrapper = document.querySelector('#navbar-role-buttons');

  if (getInternship() == null) {
    navbarWrapper.innerHTML = `
      <li class="nav-item">
        <a class="nav-link" href="#" data-uri="/">Ma liste de contacts</a>
      </li>
    
      <li class="nav-item">
        <a class="nav-link" href="#" data-uri="/addContact">Ajouter un contact</a>
      </li>
      
      <li class="nav-item">
        <a class="nav-link" href="#" data-uri="/personalData">Mon profil</a>
      </li>
     `;
  } else {
    navbarWrapper.innerHTML = `
      <li class="nav-item">
        <a class="nav-link" href="#" data-uri="/internship">Mon stage</a>
      </li>
    
      <li class="nav-item">
        <a class="nav-link" href="#" data-uri="/">Ma liste de contacts</a>
      </li>
      
      <li class="nav-item">
        <a class="nav-link" href="#" data-uri="/personalData">Mon profil</a>
      </li>
     `;
  }

}

function renderTeacherButtons() {
  const navbarWrapper = document.querySelector('#navbar-role-buttons');

  navbarWrapper.innerHTML = `
  <li class="nav-item">
    <a class="nav-link" href="#" data-uri="/dashboard">Tableau de bord</a>
  </li>

  <li class="nav-item">
    <a class="nav-link" href="#" data-uri="/users">Rechercher un utilisateur</a>
  </li>
  
  <li class="nav-item">
    <a class="nav-link" href="#" data-uri="/personalData">Mon profil</a>
  </li>
  `;
}

function renderAdminButtons() {
  const navbarWrapper = document.querySelector('#navbar-role-buttons');

  navbarWrapper.innerHTML = `
  <li class="nav-item">
    <a class="nav-link" href="#" data-uri="/users">Rechercher un utilisateur</a>
  </li>
  
  <li class="nav-item">
    <a class="nav-link" href="#" data-uri="/personalData">Mon profil</a>
  </li>
  `;
}

function disconnectListenner() {
  const signOut = document.querySelector('.navbar-profil-signout');
  signOut.addEventListener('click', () => {
    localStorage.clear();
    sessionStorage.clear();
    setUser(null);
    setInternship(null);
    Navigate('/login');
  });
}

function logoListenner() {
  const logoButton = document.querySelector('#navbar-logo');
  logoButton.addEventListener('click', () => {
    Navigate('/');
  });
}

function profileListenner() {
  const profileButton = document.querySelector('#navbar-profil-avatar');

  profileButton.addEventListener('click', () => {
    Navigate('/personalData');
  });
}

const clearNavbar = () => {
  const navbarWrapper = document.querySelector('#navbarWrapper');
  navbarWrapper.innerHTML = `
  `;
}

export {Navbar, clearNavbar};
