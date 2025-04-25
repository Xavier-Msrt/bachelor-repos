import {getToken, isStudent} from "../../../utils/user";
import Navigate from "../../Router/Navigate";
import clearPage from "../../../utils/render";
import {Navbar} from "../../Navbar/Navbar";
import {startLoading, stopLoading} from "../../../utils/loading";
import {fetchData} from "../../../utils/utils";
import {getContactLabel, getContactMeetingPlace} from "../../../utils/contact";

const CONTACTS_PER_PAGE = 8;
let currentContactPage = 1;
let currentCompany;

let contactsListFromAPI;

const CompanyDetailsPage = async () => {
  clearPage();
  Navbar();

  // Only teacher and administrative can access this page
  if (isStudent()) {
    Navigate('/');
    return;
  }

  const id = checkUri();

  if (id === null) {
    return;
  }

  currentContactPage = 1;
  currentCompany = null;

  startLoading();
  contactsListFromAPI = await fetchData(`/contacts/admin/${id}`);
  // if one contact is found, we can get the company from the first contact (one request less)
  await renderStrucCompany(contactsListFromAPI[0]?.company, id);

  loadContactPage();
  renderBlacklistForm();
  stopLoading();

}

function loadContactPage() {
  renderInternshipsList(contactsListFromAPI);
  if (contactsListFromAPI.length === 0) {
    return;
  }
  renderPageCounter(contactsListFromAPI);
  pageCounterListenner();

}

function checkUri() {
  const url = window.location.href;
  const params = new URLSearchParams(url.split('?')[1]);
  if (!params.has('id') || params.get('id') === '') {
    Navigate('/');
    return null;
  }
  return params.get('id');
}

async function renderStrucCompany(comp, id) {
  currentCompany = comp;

  // When the company list is empty
  if (comp === undefined) {
    currentCompany = await fetchData(`/companies/${id}`);
  }
  const main = document.querySelector('main');
  main.innerHTML = `
    <div class="container-fluid m-0">
        <h1 class="text-center">${currentCompany.tradeName}${currentCompany.designation != null ? ` - ${currentCompany.designation}` : ''}${currentCompany.isBlacklisted ? ' (blacklisté)' : ''}</h1>
        <br />
        <div class="row">
            <div class="company-internship col-9 ps-5">
                <div class="contact-table"></div>
            </div>
            <div class="company-blacklist col-3 pe-3"></div>
        </div>
        <div class="row fixed-bottom">
            <div class="col-9 ps-5">
                 <div class="contact-list-on-page d-flex justify-content-center"></div>
            </div>
            <div class="col-3 pe-5"></div>
        </div>     
    </div>`;
}

async function renderInternshipsList(contacts) {

  const startIndex = (currentContactPage - 1) * CONTACTS_PER_PAGE;
  const endIndex = startIndex + CONTACTS_PER_PAGE;
  const contactsOnThisPage = contacts.slice(startIndex, endIndex);

  const internship = document.querySelector('.contact-table');
  if (contactsOnThisPage.length === 0) {
    internship.innerHTML = `<h3 class="text-center">Aucun étudiant n'a postulé à cette entreprise</h3>`;
    return;
  }
  internship.innerHTML = `
        <div class="table-responsive">
              <table class="table table-striped table-bordered table-hover">
                <thead>
                  <tr>
                    <th>Nom de l'étudiant</th>
                    <th>Prénom de l'étudiant</th>
                    <th>Année Académique</th>
                    <th>Etat</th>
                    <th>Type de rencontre</th>
                    <th>Raison de refus</th>
                  </tr>
                </thead>
                <tbody></tbody>
              </table>
            </div>`;
  const tbody = document.querySelector('tbody');
  for (let i = 0; i < contactsOnThisPage.length; i += 1) {
    const contact = contactsOnThisPage[i];
    tbody.innerHTML += `
            <tr>
                <td>${contact.student.lastNameUser}</td>
                <td>${contact.student.firstNameUser}</td>
                <td>${contact.schoolYearContact.dateStart} - ${contact.schoolYearContact.dateEnd}</td>
                <td>${getContactLabel(contact.state)}</td>
                <td>${contact.meetingPlace != null ? getContactMeetingPlace(
        contact.meetingPlace) : '/'}</td>
                <td>${contact.reasonRefusal != null ? contact.reasonRefusal
        : '/'}</td>
            </tr>`;
  }
  ;

}

function renderBlacklistForm() {
  const blacklistDiv = document.querySelector('.company-blacklist');

  if (currentCompany.isBlacklisted) {
    blacklistDiv.innerHTML = `
      <p class="h3 text-center fw-bold">Cette entreprise est blacklistée</p>
      <br>
      <p class="h5 text-center fw-bold">Raison</p>
      <p class="h5 text-center">${currentCompany.blackListMotivation}</p>
    `;
    return;
  }

  blacklistDiv.innerHTML = `
    <form class="card text-dark border-primary bg-white mb-3 mt-3 shadow p-3" style="border-radius: 15px;">
      <h2 class="text-internify text-decoration-underline mt-3 ms-3 text-center">Blacklister l'entreprise</h2>
      <div class="card-body">
        <div class="row align-items-center pt-2 pb-3">
          <label class="text-internify fs-5">Écrivez la motivation : </label>
          <div class="mx-auto">
            <input class="form-control form-control-lg" id="companydetailled-blacklist-motivation" placeholder="Motivation du blacklist" onkeydown="return event.key != 'Enter';" required>
            <p id="company-details-page-error-blacklist" class="text-danger d-none">Veuillez remplir ce champ</p>
          </div>
        </div>     
        <div>
          <input type="submit" class="btn btn-primary" id="companydetailled-submit-blacklist" value="Blacklister">
        </div>
      </div>        
    </form> 
  `;

  blackListFormListenner();
}

function blackListFormListenner() {
  const submitBlacklist = document.querySelector('#companydetailled-submit-blacklist');

  submitBlacklist.addEventListener('click', async (e) => {
    e.preventDefault();

    const blacklistMotivationInput = document.querySelector('#companydetailled-blacklist-motivation');
    const blacklistMotivation = blacklistMotivationInput.value;

    if (!blacklistMotivation || blacklistMotivation.trim().length === 0) {
        const error = document.querySelector('#company-details-page-error-blacklist');
        error.classList.remove('d-none');
       return;
    }

    const options = {
      method: 'POST',
      body: JSON.stringify({
        idCompany: currentCompany.idCompany,
        blackListMotivation: blacklistMotivation,
        versionCompany : currentCompany.versionCompany,
      }),
      headers: {
        'Content-Type': 'application/json',
        Authorization: getToken(),
      },
    };

    startLoading();

    const response = await fetch(`${process.env.API_BASE_URL}/companies/admin/blacklist`, options);
    if (!response.ok) {
      throw new Error(
          `${response.status} : ${response.statusText}`);
    }

    currentCompany = await response.json();
    contactsListFromAPI = await fetchData(`/contacts/admin/${currentCompany.idCompany}`);

    loadContactPage();
    renderBlacklistForm();
    stopLoading();
  });
}

function renderPageCounter(contactsList) {
  const contactsPageCounterDiv = document.querySelector(
      '.contact-list-on-page');

  let pageCounterHTML = `
  <nav aria-label="navigation">
    <ul class="pagination">`

  // Check if current page is 1 to disable previous button
  if (currentContactPage === 1) {
    pageCounterHTML += `<li class="page-item disabled"><button id="contact-page-previous" class="page-link text-internify">Précédent</button></li>`
  } else {
    pageCounterHTML += `<li class="page-item"><button id="contact-page-previous" class="page-link text-internify">Précédent</button></li>`
  }

  let pageIndex = 1;
  for (let i = 0; i < contactsList.length; i += 8) {

    // Check if current page to show the button active
    if (currentContactPage === pageIndex) {
      pageCounterHTML += `<li class="page-item"><button class="page-link active text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`
    } else {
      pageCounterHTML += `<li class="page-item"><button class="page-number-button page-link text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`
    }

    pageIndex += 1;
  }

  // Check if current page is equals to pageIndex (max page index) to disable next button
  // pageIndex-1 because pageIndex start at 1 and add 1 for the last page
  if (currentContactPage === pageIndex - 1) {
    pageCounterHTML += `<li class="page-item disabled"><button id="contact-page-next" class="page-link text-internify">Suivant</button></li>`
  } else {
    pageCounterHTML += `<li class="page-item"><button id="contact-page-next" class="page-link text-internify">Suivant</button></li>`
  }

  pageCounterHTML += `
   </ul>
  </nav>
  `

  contactsPageCounterDiv.innerHTML = pageCounterHTML;
}

function pageCounterListenner() {
  const previousButton = document.querySelector('#contact-page-previous');
  const nextButton = document.querySelector('#contact-page-next');
  const numberButtons = document.querySelectorAll('.page-number-button');

  previousButton.addEventListener('click', (e) => {
    e.preventDefault();
    currentContactPage -= 1;
    loadContactPage();
  });

  nextButton.addEventListener('click', (e) => {
    e.preventDefault();
    currentContactPage += 1
    loadContactPage();
  });

  numberButtons.forEach(button => {
    button.addEventListener('click', (e) => {
      e.preventDefault();
      currentContactPage = parseInt(button.getAttribute('data-page'), 10);
      loadContactPage();
    });
  });
}

export default CompanyDetailsPage;
