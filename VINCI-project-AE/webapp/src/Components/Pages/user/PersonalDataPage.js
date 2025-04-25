import clearPage from "../../../utils/render";
import {startLoading, stopLoading} from "../../../utils/loading";
import {Navbar} from "../../Navbar/Navbar";
import {
  fetchData,
  millisecToDate,
} from "../../../utils/utils";
import {
  getToken,
  isStudent,
  roleEnToFr,
  setUser
} from "../../../utils/user";
import Navigate from "../../Router/Navigate";
import {getContactLabel, getContactMeetingPlace} from "../../../utils/contact";
import {getInternship, setInternship} from "../../../utils/internship";

let currentContactPage = 1;
let userData;
let contactsList;

const PersonalDataPage = async () => {
  clearPage();
  Navbar();

  currentContactPage = 1;
  contactsList = null;

  startLoading();
  userData = await fetchData("/users/me");
  setUser(userData);

  if (isStudent()) {
    contactsList = await fetchData('/contacts');
    setInternship(await fetchData('/internships'));
  }

  stopLoading();

  renderPersonalDataPage(userData, contactsList, getInternship());
};

function renderPersonalDataPage(user, contacts, internship) {
  const main = document.querySelector('main');

  let personalDataPageHTML = `
    <div class="container-fluid container-personal-data">
        <div class="row row-personal-data">
    `;

  // Size of the first column depends on the user role
  if (!isStudent()) {
    personalDataPageHTML += `<div class="col-md-12 bg-secondary rounded mt-3">`
  } else {
    personalDataPageHTML += `<div class="col-md-5 bg-secondary rounded mt-3">`
  }

  personalDataPageHTML += `<div id="personnalDataBody" class="h-100"> 
${getPersonalData(user)}</div>`;
  if (!isStudent()) {
    personalDataPageHTML += `
            </div>
        </div>
        `;

    main.innerHTML = personalDataPageHTML;
    listenerRedirectToUserModification(user);
    return;
  }

  if (internship === null) {
    personalDataPageHTML += `
                <!-- Deuxième colonne -->
                <div class="col-md-7 mt-3 ps-4">
                    
                    <div id="row-personal-data-company-contact-2" class="row row-personal-data-right-big bg-secondary rounded">
                        <h3 class="fw-bold mt-3 mb-0">Contacts pris</h3>
                        <div id="personal-data-contacts-table-div" class="p-0"></div>
                    </div>
                </div>
            </div>
        </div>
        `;
  } else {
    personalDataPageHTML += `
                <!-- Deuxième colonne -->
                <div class="col-md-7 mt-3 ps-4">
                
                    <div style="overflow: auto;" class="row bg-secondary row-personal-data-right-small rounded mb-2">
                        <div class="d-flex justify-content-between align-items-center">
                          <h3 class="fw-bold text-left mt-3">Informations du stage</h3>
                          <a id="personal-data-acess-internship" class="btn btn-primary mt-2">Accéder au stage</a>
                          <h3 class="fw-bold text-right mt-3">${internship.schoolYearInternship.dateStart} - ${internship.schoolYearInternship.dateEnd}</h3>
                        </div><br><hr>
                        <div style=" height: 20vh; column-count: 2; column-fill: auto;">
                          <h6 class="fw-bold mb-n1">Entreprise et appelation : </h6>
                          <p id="company">${internship.company.tradeName} ${internship.company.designation ? ` - ${internship.company.designation}` : ''}</p>

                          <h6 class="fw-bold mb-n1">Date de signature de la convention :</h6>
                          <p id="signatureDate">${millisecToDate(internship.signatureDate)}</p>
            
                          <h6 class="fw-bold mb-n1">Sujet du stage :</h6>
                          <p id="internShipProject">${internship.internShipProject === null ? "Aucun sujet de stage ajouté" : internship.internShipProject}</p>
                          
                          <h6 class="fw-bold mb-n1">Responsable du stage :</h6>
                          <p id="internshipSupervisor">${internship.internSupervisor.lastNameSupervisor} ${internship.internSupervisor.firstNameSupervisor}</p>
                        
                        </div>
                    </div>
                    
                    <div id="row-personal-data-company-contact-2" class="row row-personal-data-right-small bg-secondary rounded mt-2">
                        <h4 class="fw-bold mt-3 mb-0">Contacts pris</h4> <hr>
                        <div id="personal-data-contacts-table-div" class="p-0"></div>
                    </div>
                </div>
            </div>
        </div>
        `;
  }

  main.innerHTML = personalDataPageHTML;
  renderContactRowsOnPage(contacts);
  listenerRedirectToUserModification(user);
  accessToInternshipListenner();
}

function accessToInternshipListenner() {
  const btn = document.querySelector('#personal-data-acess-internship');
  if (btn === null) return;
  btn.addEventListener('click', async (e) => {
    e.preventDefault();
    Navigate('/internship');
  });
}


function listenerRedirectToUserModification(user) {
  const btn = document.querySelector('#btn-modify-user');
  btn.addEventListener('click', async (e) => {
    e.preventDefault();
    const div = document.querySelector('#personnalDataBody');
    div.innerHTML = getModificationForm(user);
    listenerRedirectToPasswordModification(user);
    listenerSubmitPersonalData(user);
    listenerOnCancelBtn();
  });
}

function listenerRedirectToPasswordModification(user) {
  const toPwdButton = document.querySelector('button[value="toPwd"]');

  toPwdButton.addEventListener('click', async (e) => {
    e.preventDefault();
    const div = document.querySelector('#personnalDataBody');
    div.innerHTML = getPwdForm();
    listenerRedirectToPersonalDataForm(user);
    listenerOnCancelBtn();
    listenerSubmitPassword(user);
  });
}

function listenerRedirectToPersonalDataForm(user) {
  const toPwdButton = document.querySelector('button[value="toPersonnalData"]');
  const divErrors = document.querySelector('#errors');
  divErrors.style.display = "none";
  toPwdButton.addEventListener('click', (e) => {
    e.preventDefault();
    const div = document.querySelector('#personnalDataBody');
    div.innerHTML = getModificationForm(user);
    listenerRedirectToPasswordModification(user);
    listenerSubmitPersonalData(user);
    listenerOnCancelBtn();
  });
}

function listenerOnCancelBtn() {
  const btnCancel = document.querySelector('button[value="btnCancel"]');
  btnCancel.addEventListener('click', (e) => {
    e.preventDefault();
    const div = document.querySelector('#personnalDataBody');
    div.innerHTML = getPersonalData(userData);
    listenerRedirectToUserModification(userData);
  });
}

function listenerSubmitPersonalData(user) {
  const form = document.querySelector('#userForm');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    await fetchUpdatePhoneNumber(user);
  })
}

async function fetchUpdatePhoneNumber(user) {
  const phoneNumberUser = document.querySelector("#phoneNumber").value;
  const {versionUser} = user;
  const {idUser} = user;
  const options = {
    method: 'POST',
    body: JSON.stringify({
      idUser,
      versionUser,
      phoneNumberUser
    }),
    headers: {
      'Content-Type': 'application/json',
      Authorization: getToken(),
    },
  };

  const div = document.querySelector('#personnalDataBody');
  div.innerHTML = getMiniLoader();

  const response = await fetch(
      `${process.env.API_BASE_URL}/users/updatePhoneNumber`, options);
  if (response.ok) {
    userData = await fetchData("/users/me");
    setUser(userData);
    div.innerHTML = getPersonalData(userData);
    listenerRedirectToUserModification(userData);
  } else {
    div.innerHTML = getModificationForm(user);
    listenerRedirectToPasswordModification(user);
    listenerSubmitPersonalData(user);
    listenerOnCancelBtn();
    const divErrors = document.querySelector('#errors');
    divErrors.style.display = "block";
    divErrors.innerText = "Erreur lors de la modification du numéro de téléphone";
  }

}

function getMiniLoader() {
  return ` 
     <div class="d-flex align-items-center justify-content-center" style="height: 100%;">
      <div class="mini-loader"></div>
    </div>`;
}


function getPersonalData(user) {
  return ` <div class="d-flex justify-content-between align-items-center">
    <h3 class="fw-bold text-left mt-3">Informations personelles</h3>
    <button class="btn btn-primary mt-3" id="btn-modify-user"> Modifier</button>
  </div>
  <hr/>
  <p class="fw-bold mb-n1">Adresse email :</p>
  <p id="email"><a href="mailto:${user.emailUser}">${user.emailUser}</a></p>
  <p class="fw-bold mb-n1">Numéro de téléphone :</p>
  <p id="phoneNumber">${user.phoneNumberUser}</p>
  <p class="fw-bold mb-n1">Date d'inscription :</p>
  <p id="registrationDate">${millisecToDate(user.registrationDate)}</p>
  <p class="fw-bold mb-n1">Année académique :</p>
  <p id="schoolYear">${user.schoolYearUser.dateStart} -
    ${user.schoolYearUser.dateEnd}</p>
  <p class="fw-bold mb-n1">Rôle :</p>
  <p id="role">${roleEnToFr(user.role)}</p>
</div>`;
}

function getModificationForm(user) {
  return ` 
    <div id="popup"></div>
    <div class="d-flex justify-content-between align-items-center">
        <h3 class="fw-bold text-left mt-3">Modifier mes données</h3>
        <button class="btn btn-primary mt-3" value="btnCancel"> Annuler</button>
    </div>
    <hr/>
    <form id="userForm">
      <div class="form-group">
        <label for="email">Adresse email : (Non modifiable)</label>
        <p><a href="mailto:${user.emailUser}">${user.emailUser}</a></p>
            
        <label for="phoneNumber">Numéro de téléphone :</label>
        <input type="text" id="phoneNumber" placeholder="${user.phoneNumberUser}" class="form-control">
      </div>
      <div class="fw-bold text-danger mt-3" id="errors"></div>
      <br>
      <button type="submit" class="btn btn-primary" id="modify">Enregistrer les modifications</button>
      <button type="submit" class="btn btn-secondary swap-btn" value="toPwd">Modifier mon mot de passe</button>
  </form>
  
</div>`;
}

function getPwdForm() {
  return ` 
    <div id="popup"></div>
    <div class="d-flex justify-content-between align-items-center">
      <h3 class="fw-bold text-left mt-3">Modifier mon mot de passe</h3>
      <button class="btn btn-primary mt-3" value="btnCancel"> Annuler</button>
    </div>
    <hr/>
    <form id="userForm">
      <div class="form-group">      
        <label for="oldpwd">Ancien mot de passe :</label>
        <input type="password" id="oldpwd" class="form-control" required>
        
        <label for="newpwd">Nouveau mot de passe :</label>
        <input type="password" id="newpwd" class="form-control" value="" required>
        
        <label for="confirmedpwd">Confirmer le nouveau mot de passe :</label>
        <input type="password" id="confirmedpwd" class="form-control" value="" required>
      </div>
      <div class="fw-bold text-danger mt-3" id="errors"></div>
      <br>
      <button type="submit" class="btn btn-primary" id="modify">Enregistrer les modifications</button>
      <button type="submit" class="btn btn-secondary swap-btn" value="toPersonnalData" >Modifier mes données</button>
  </form>`;
}

function listenerSubmitPassword(user) {
  const form = document.querySelector('#userForm');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    await fetchUpdatePassword(user);
  });
}

async function fetchUpdatePassword(user) {
  const oldPassword = document.querySelector("#oldpwd").value;
  const newPassword = document.querySelector("#newpwd").value;
  const confirmedPwd = document.querySelector("#confirmedpwd").value;

  if (newPassword !== confirmedPwd) {
    const divErrors = document.querySelector('#errors');
    divErrors.style.display = "block";
    divErrors.innerText = "Les mots de passe ne correspondent pas";
    return;
  }
  if (oldPassword === newPassword) {
    const divErrors = document.querySelector('#errors');
    divErrors.style.display = "block";
    divErrors.innerText = "Le nouveau mot de passe doit être différent de l'ancien";
    return;
  }

  const user2 = {
    idUser: user.idUser,
  }


  const options = {
    method: 'POST',
    body: JSON.stringify({
          user: user2,
          password: oldPassword,
          newPassword,
        }
    ),
    headers: {
      'Content-Type': 'application/json',
      Authorization: getToken(),
    },
  };

  const div = document.querySelector('#personnalDataBody');
  div.innerHTML = getMiniLoader();

  const response = await fetch(
      `${process.env.API_BASE_URL}/users/updatePassword`, options);
  if (response.ok) {
    userData = await fetchData("/users/me");
    setUser(userData);
    div.innerHTML = getPersonalData(userData);
    listenerRedirectToUserModification(userData);
  } else {
    div.innerHTML = getPwdForm(user);
    listenerRedirectToPersonalDataForm(user);
    listenerSubmitPassword(user);
    listenerOnCancelBtn();
    const divErrors = document.querySelector('#errors');
    divErrors.style.display = "block";
    divErrors.innerText = "Erreur lors de la modification du mot de passe";
  }
}

function renderContactRowsOnPage(contacts) {
  const divContact = document.querySelector(
      '#personal-data-contacts-table-div');

  if (contacts.length === 0) {
    divContact.innerHTML = `<div class="d-flex align-items-center justify-content-center h4">Vous n'avez aucun contact</div>`;
    return;
  }

  divContact.innerHTML = `
    <div class="container p-0">
        <table class="table table-contacts table-striped-contacts table-row-nowrap table-hover">
            <thead>
                <tr>
                    <th class="personal-data-contact-font">Entreprise</th>
                    <th class="personal-data-contact-font">Appellation</th>
                    <th class="personal-data-contact-font">État</th>
                    <th class="personal-data-contact-font">Raison</th>
                    <th class="personal-data-contact-font">Rencontre</th>
                    <th class="personal-data-contact-font">Année académique</th>
                </tr>
            </thead>
            <tbody id="personal-data-contact-rows-tbody"></tbody>
        </table>
    </div>
    <div class="d-flex justify-content-center">
        <div class="contact-list-page-counter"></div>
    </div>
    `;

  setContactsRows(contacts);
}

function setContactsRows(contacts) {
  const tbody = document.querySelector('#personal-data-contact-rows-tbody');

  const div = document.querySelector('#row-personal-data-company-contact-2');
  const tableRow = document.querySelector('thead tr');

  const divHeight = div.clientHeight;
  const rowHeight = tableRow.clientHeight;

  const numberOfRows = Math.floor((divHeight / 2) / rowHeight);

  const startIndex = (currentContactPage - 1) * numberOfRows;
  const endIndex = startIndex + numberOfRows;
  const contactsOnThisPage = contacts.slice(startIndex, endIndex);

  let rows = "";

  contactsOnThisPage.forEach(contact => {
    rows += `
        <tr class="personal-data-contact-row cursor-clickable" data-companyid="${
        contact.companyIdContact}">
            <td class="personal-data-contact-font">${contact.company.tradeName}</td>
            <td class="personal-data-contact-font">${contact.company.designation
        ? contact.company.designation : '/'}</td>
            <td class="personal-data-contact-font">${getContactLabel(
        contact.state.toUpperCase())}</td>
            <td class="personal-data-contact-font" aria-hidden="true">${contact.reasonRefusal
    || ''}</td>
            <td class="personal-data-contact-font">${contact.meetingPlace
        ? getContactMeetingPlace(contact.meetingPlace) : '/'}</td> 
            <td class="personal-data-contact-font">${contact.schoolYearContact.dateStart} - ${contact.schoolYearContact.dateEnd}</td>
        </tr>
        `;
  });

  tbody.innerHTML = rows;

  renderPageCounter(contacts, numberOfRows);
  pageCounterListenner(contacts);
  goToContactListenner();
}

function renderPageCounter(contacts, countContactsPerPage) {
  const contactsPageCounterDiv = document.querySelector(
      '.contact-list-page-counter');

  let pageCounterHTML = `
      <nav aria-label="navigation">
        <ul class="pagination">`;

  // Check if current page is 1 to disable previous button
  if (currentContactPage === 1) {
    pageCounterHTML += `<li class="page-item disabled"><button id="contact-page-previous" class="page-link text-internify">Précédent</button></li>`;
  } else {
    pageCounterHTML += `<li class="page-item"><button id="contact-page-previous" class="page-link text-internify">Précédent</button></li>`
  }

  let pageIndex = 1;
  for (let i = 0; i < contacts.length; i += countContactsPerPage) {

    // Check if current page to show the button active
    if (currentContactPage === pageIndex) {
      pageCounterHTML += `<li class="page-item"><button class="page-link active text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`;
    } else {
      pageCounterHTML += `<li class="page-item"><button class="page-number-button page-link text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`;
    }

    pageIndex += 1;
  }

  // Check if current page is equals to pageIndex (max page index) to disable next button
  // pageIndex-1 because pageIndex start at 1 and add 1 for the last page
  if (currentContactPage === pageIndex - 1) {
    pageCounterHTML += `<li class="page-item disabled"><button id="contact-page-next" class="page-link text-internify">Suivant</button></li>`;
  } else {
    pageCounterHTML += `<li class="page-item"><button id="contact-page-next" class="page-link text-internify">Suivant</button></li>`;
  }

  pageCounterHTML += `
   </ul>
  </nav>
  `
  contactsPageCounterDiv.innerHTML = pageCounterHTML;
}

function pageCounterListenner(contacts) {
  const previousButton = document.querySelector('#contact-page-previous');
  const nextButton = document.querySelector('#contact-page-next');
  const numberButtons = document.querySelectorAll('.page-number-button');

  previousButton.addEventListener('click', (e) => {
    e.preventDefault();
    currentContactPage -= 1;
    setContactsRows(contacts);
  });

  nextButton.addEventListener('click', (e) => {
    e.preventDefault();
    currentContactPage += 1;
    setContactsRows(contacts);
  });

  numberButtons.forEach(button => {
    button.addEventListener('click', (e) => {
      e.preventDefault();
      const pageNumber = parseInt(button.getAttribute('data-page'), 10);
      if (pageNumber !== currentContactPage) {
        currentContactPage = pageNumber;
        setContactsRows(contacts);
      }
    });
  });
}

function goToContactListenner() {
  const contactRow = document.querySelectorAll('.personal-data-contact-row');

  contactRow.forEach(row => {
    row.addEventListener('click', (e) => {
      e.preventDefault();
      const companyId = parseInt(row.getAttribute('data-companyid'), 10);
      Navigate(`/contact?id=${companyId}`);
    });
  });
}

export default PersonalDataPage;
