import clearPage from "../../../utils/render";
import {startLoading, stopLoading} from "../../../utils/loading";
import {Navbar} from "../../Navbar/Navbar";
import {fetchData, millisecToDate, uriExtract} from "../../../utils/utils";
import {
  isAdministrative,
  isTeacher,
  roleEnToFr,
} from "../../../utils/user";
import Navigate from "../../Router/Navigate";
import {getContactLabel, getContactMeetingPlace} from "../../../utils/contact";

let currentContactPage = 1;

const UserDataPage = async () => {

  if (!isTeacher() && !isAdministrative()) {
    Navigate('/');
    return;
  }

  clearPage();
  Navbar();

  startLoading();

  const params = uriExtract();
  const id= params.has('id') ? params.get('id') : null;
  let user = null;
  let internship = null;
  let contacts;

  if(id !== null){
    user = await fetchData(`/users/admin/${id}`);
    if (user.role.toUpperCase() === "STUDENT") {
      contacts = await fetchData(`/contacts/admin/contactsList/${id}`);
      internship = (await fetchData(`/internships/admin/${id}`));
    }
  }else{
    Navigate('/');
    stopLoading();
    return;
  }

  stopLoading();

  renderUserDataPage(user, contacts, internship);
};

function renderUserDataPage(user, contacts, internship) {
  const main = document.querySelector('main');
  const roleFr = roleEnToFr(user.role);
  const date = millisecToDate(user.registrationDate);

  let UserDataPageHTML = ` 
    <div class="container-fluid container-personal-data">
        <div class="row row-personal-data">
    `;

  // Size of the first column depends on the user role
  if (user.role.toUpperCase() !== "STUDENT") {
    UserDataPageHTML += `<div class="col-md-12 bg-secondary rounded mt-3">`
  } else {
    UserDataPageHTML += `<div class="col-md-5 bg-secondary rounded mt-3">`
  }

  UserDataPageHTML += `<div id="personnalDataBody"> 
${renderUserData(user, roleFr, date)}</div>`;
  if (user.role.toUpperCase() !== "STUDENT") {
    UserDataPageHTML += `
            </div>
        </div>
        `;

    main.innerHTML = UserDataPageHTML;
    return;
  }

  if (internship === null) {



    UserDataPageHTML += `
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
    UserDataPageHTML += `
                <!-- Deuxième colonne -->
                <div class="col-md-7 mt-3 ps-4">
                
                    <div style="overflow: auto;" class="row bg-secondary row-personal-data-right-small rounded mb-2">
                        <div class="d-flex justify-content-between align-items-center">
                          <h3 class="fw-bold text-left mt-3">Informations du stage</h3>
                          <a id="user-data-acess-internship" class="btn btn-primary mt-2">Accéder au stage</a>
                          <h3 class="fw-bold text-right mt-3">${internship.schoolYearInternship.dateStart} - ${internship.schoolYearInternship.dateEnd}</h3>
                        </div><hr>
                        <div style=" height: 20vh; column-count: 2; column-fill: auto;">
                          <h6 class="fw-bold mb-n1">Entreprise et appelation : </h6>
                          <p id="company">${internship.company.tradeName} ${internship.company.designation ? ` - ${internship.company.designation}` : ''}</p>

                          <h6 class="fw-bold mb-n1">Date de signature de la convention :</h6>
                          <p id="signatureDate">${millisecToDate(internship.signatureDate)}</p>
            
                          <h6 class="fw-bold mb-n1">Sujet du stage :</h6>
                          <p id="internShipProject">${internship.internShipProject === null ? "Pas encore ajouté" : internship.internShipProject}</p>
                          
                          <div>
                            <h6 class="fw-bold mb-n1">Responsable du stage :</h6>
                            <p id="internshipSupervisor">${internship.internSupervisor.lastNameSupervisor} ${internship.internSupervisor.firstNameSupervisor} </br>
                            </p>
                          </div> 
                          
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

  main.innerHTML = UserDataPageHTML;
  renderContactRowsOnPage(contacts);
  accessToInternshipListenner(user.idUser);
}

function renderUserData(user, roleFr, date) {
  return ` <div class="d-flex justify-content-between align-items-center">
    <h3 class="fw-bold text-left mt-3">Informations personelles de ${user.lastNameUser} ${user.firstNameUser}</h3>
  </div>
  <hr/>
  <p class="fw-bold mb-n1">Adresse email :</p>
  <p id="email"><a href="mailto:${user.emailUser}">${user.emailUser}</a></p>
  <p class="fw-bold mb-n1">Numéro de téléphone :</p>
  <p id="phoneNumber">${user.phoneNumberUser}</p>
  <p class="fw-bold mb-n1">Date d'inscription :</p>
  <p id="registrationDate">${date}</p>
  <p class="fw-bold mb-n1">Année académique :</p>
  <p id="schoolYear">${user.schoolYearUser.dateStart} - ${user.schoolYearUser.dateEnd}</p>
  <p class="fw-bold mb-n1">Rôle :</p>
  <p id="role">${roleFr}</p>
</div>`;
}

function renderContactRowsOnPage(contacts) {
  const divContact = document.querySelector(
      '#personal-data-contacts-table-div');

  if (contacts.length === 0) {
    divContact.innerHTML = `<div class="d-flex align-items-center justify-content-center h4"> L'étudiant n'a aucun contact </div>`;
    return;
  }

  divContact.innerHTML = `
    <div class="container p-0">
        <table class="table table-contacts table-striped-contacts table-row-nowrap">
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

function accessToInternshipListenner(idUser) {
  const btn = document.querySelector('#user-data-acess-internship');
  if (btn === null) return;
  btn.addEventListener('click', async (e) => {
    e.preventDefault();
    Navigate(`/internshipData?id=${idUser}`);
  });
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
        <tr class="personal-data-contact-row" data-companyid="${contact.companyIdContact}">
            <td class="personal-data-contact-font">${contact.company.tradeName}</td>
            <td class="personal-data-contact-font">${contact.company.designation ? contact.company.designation : '/'}</td>
            <td class="personal-data-contact-font">${getContactLabel(contact.state.toUpperCase())}</td>
            <td class="personal-data-contact-font" aria-hidden="true">${contact.reasonRefusal || '/'}</td>
            <td class="personal-data-contact-font">${contact.meetingPlace ? getContactMeetingPlace(contact.meetingPlace) : '/'}</td>
            <td class="personal-data-contact-font">${contact.schoolYearContact.dateStart} - ${contact.schoolYearContact.dateEnd}</td>
        </tr>
        `;
  });

  tbody.innerHTML = rows;

  renderPageCounter(contacts, numberOfRows);
  pageCounterListenner(contacts);
}

function renderPageCounter(contactsList, countContactsPerPage) {
  const contactsPageCounterDiv = document.querySelector('.contact-list-page-counter');

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
  for (let i = 0; i < contactsList.length; i += countContactsPerPage) {

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

export default UserDataPage;
