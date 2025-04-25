import clearPage from '../../../utils/render';
import {Navbar} from "../../Navbar/Navbar";
import Navigate from "../../Router/Navigate";
import {checkDesignation, getContactLabel} from "../../../utils/contact";
import {startLoading, stopLoading} from "../../../utils/loading";
import {fetchData} from '../../../utils/utils';
import {isAdministrative, isTeacher} from "../../../utils/user";

const CONTACTS_PER_PAGE = 8;
let currentContactPage = 1;
let contactsListFromAPI;

const ContactPage = async () => {

  if (isTeacher()) {
    Navigate('/dashboard');
    return;
  }
  if (isAdministrative()) {
    Navigate('/users');
    return;
  }

  clearPage();
  Navbar();

  currentContactPage = 1;

  startLoading();
  contactsListFromAPI = await fetchData("/contacts");
  stopLoading();

  loadContactPage();
};

function loadContactPage() {
  renderContactPage(contactsListFromAPI);

  if (contactsListFromAPI.length === 0) {
    return;
  }

  renderPageCounter(contactsListFromAPI);
  pageCounterListenner();
  goToContactListenner();
}

function renderContactPage(contactsList) {
  const main = document.querySelector('main');
  main.innerHTML = `
    <div class="contact-list-on-page-div">
      <div class="contact-list-on-page"></div>
    </div>
    <div id="contact-page-no-contact-div" class="position-fixed w-100 h-100 d-flex justify-content-center align-items-center">
        <div id="contact-page-no-contact" class="h2"></div>
    </div>
    <div class="d-flex justify-content-center mt-1 fixed-bottom">
       <div class="contact-list-page-counter"></div>
    </div>
  `;

  displayCurrentContactPage(contactsList);

}

// Display current contact page
function displayCurrentContactPage(contactsList) {
  const contactsListDiv = document.querySelector('.contact-list-on-page');

  const startIndex = (currentContactPage - 1) * CONTACTS_PER_PAGE;
  const endIndex = startIndex + CONTACTS_PER_PAGE;
  const contactsOnThisPage = contactsList.slice(startIndex, endIndex);

  let contactHTML = '';

  if (contactsOnThisPage.length === 0) {
    document.querySelector(
        '#contact-page-no-contact').innerText = "Vous n'avez aucun contact";
  }

  for (let i = 0; i < contactsOnThisPage.length; i += 4) {
    contactHTML += '<div class="row mt-1 card-contact-row">';

    for (let j = i; j < i + 4 && j < contactsOnThisPage.length; j += 1) {
      const contact = contactsOnThisPage[j];

      // Get the label of the state of the contact in the contactStateEnum
      const stateLabel = getContactLabel(contact.state.toUpperCase());
      const designation = checkDesignation(contact.company.designation);
      contactHTML += `
        <div class="card-contact-col col-md-6 col-lg-3">
          <div class="card-contact card me-2 shadow">
            <div class="contact-body card-body">
              <h5 class="card-title text-internify text-decoration-underline">${contact.company.tradeName}</h5>
              <div class="card-contact-information">
                <p class="card-subtitle mb-1 fw-bold">${designation}</p>
                <p class="card-subtitle mb-1 text-muted">${contact.schoolYearContact.dateStart} - ${contact.schoolYearContact.dateEnd}</p>
                <p class="card-subtitle">État du contact : <span class="text-internify fw-bold">${stateLabel}</span></p>
              </div>
              <button class="btn btn-primary border-primary go-to-contact" data-companyid="${contact.company.idCompany}">Accéder au contact</button>
            </div>
          </div>
        </div>
      `;
    }

    contactHTML += '</div><br>';
  }

  contactsListDiv.innerHTML = contactHTML;
}

function renderPageCounter(contactsList) {
  const contactsPageCounterDiv = document.querySelector(
      '.contact-list-page-counter');

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

function goToContactListenner() {
  const contactButtons = document.querySelectorAll('.go-to-contact');

  contactButtons.forEach(button => {
    button.addEventListener('click', () => {
      const idCompany = button.getAttribute('data-companyid');
      Navigate(`/contact?id=${idCompany}`);
    });
  });
}

export default ContactPage;
