import clearPage from "../../../utils/render";
import {Navbar} from "../../Navbar/Navbar";
import {fetchData} from "../../../utils/utils";
import {getToken, isStudent} from "../../../utils/user";
import {startLoading, stopLoading} from "../../../utils/loading";
import Navigate from "../../Router/Navigate";
import {getInternship} from "../../../utils/internship";

let currentCompanyPage = 1;
let defaultCompanies = [];
let companies = [];

const ContactAddPage = async () => {

  if (!isStudent()) {
    Navigate('/');
    return;
  }

  if (getInternship() != null) {
    Navigate('/');
    return;
  }

  clearPage();
  Navbar();

  currentCompanyPage = 1;
  defaultCompanies = [];
  companies = [];

  renderContactAddPageBase();

  startLoading();
  await setAllContact();
  stopLoading();

  loadCompanyPage();
  renderSearchBar();
};

function renderContactAddPageBase() {
  const main = document.querySelector('main');
  main.innerHTML = `
  <div class="container my-lg-5">
      <div class="canvas"></div> 
      <div class="search-bar"></div>
      <div class="content"></div>
      <div class="pageNav d-flex justify-content-center fixed-bottom"><div>
  </div> `;
}

async function setAllContact() {
  const comp = await fetchData(`/companies`);

  companies = comp.sort((a, b) => a.tradeName.localeCompare(b.tradeName))
  defaultCompanies = companies;
}

function loadCompanyPage() {
  const content = document.querySelector('.content');

  // if no companies found
  if (companies.length === 0) {
    content.innerHTML = `
      <div class="d-flex justify-content-center">
          <button class="company-notfound page-link text-internify">Aucune entreprise trouvée</button>
      </div>
    `;

    // Remove page counter because no companies found
    document.querySelector('.pageNav').innerHTML = '';

    return;
  }

  // if companies found
  content.innerHTML = `
    <div class="table-responsive">
      <table class="table table-striped table-bordered table-hover text-center">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Appellation</th>
            <th>Adresse</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody></tbody>
      </table>
    </div>
  `;

  const tbody = document.querySelector('tbody');

  const tableRow = document.querySelector('thead tr');

  const divHeight = window.innerHeight;
  const rowHeight = tableRow.clientHeight;

  const numberOfRows = Math.floor((divHeight / 3) / rowHeight);

  const startIndex = (currentCompanyPage - 1) * numberOfRows;
  const endIndex = startIndex + numberOfRows;
  const companiesOnThisPage = companies.slice(startIndex, endIndex);

  let rows = "";

  companiesOnThisPage.forEach(company => {
    let actionOrError = `<td class="btn-${company.idCompany}"><button class="btn btn-primary company" data-id="${company.idCompany}">Initier le contact</button></td>`;

    if (company.error !== undefined) {
      if (company.error === 'Contact initié') {
        actionOrError = `<td class="btn-${company.idCompany}"><p class="text-success">${company.error}</p></td>`;
      } else {
        actionOrError = `<td class="btn-${company.idCompany}"><p class="text-danger">${company.error}</p></td>`;
      }
    }
    if (company.isBlacklisted === true) {
      actionOrError = '<td>Entreprise blacklistée</td>';
    }

    rows += `
      <tr>
          <td>${company.tradeName}</td>
          <td>${company.designation ? company.designation : '/'}</td>
          <td>${company.street} ${company.boxNumber}, ${company.postCode} ${company.city} </td>
          ${actionOrError}
       </tr>
    `;
  });

  tbody.innerHTML = rows;

  // Listenner for company initiation
  const buttons = document.querySelectorAll('.company');

  buttons.forEach(btn => {
    btn.addEventListener('click', async (e) => {
      e.preventDefault();
      const id = parseInt(btn.getAttribute('data-id'), 10);
      const options = {
        method: 'POST',
        body: JSON.stringify({
          companyId: id
        }),
        headers: {
          'Content-Type': 'application/json',
          Authorization: getToken(),
        },
      };

      const response = await fetch(`${process.env.API_BASE_URL}/contacts`,
          options);

      const btnDiv = document.querySelector(`.btn-${id}`);
      const company = defaultCompanies.find(c => c.idCompany === id);

      if (response.ok) {
        company.error = 'Contact initié';
      }

      if (response.status === 409) {
        company.error = 'Contact déjà initié';
      } else if (response.status === 412) {
        company.error = 'Entreprise blacklistée ou n\'exist plus';
      } else if (response.status === 500) {
        company.error = 'Erreur serveur';
      }

      if (company.error === 'Contact initié') {
        btnDiv.innerHTML = `<p class="text-success">${company.error}</p>`;
      } else {
        btnDiv.innerHTML = `<p class="text-danger">${company.error}</p>`;
      }

    });
  });

  renderPageCounter();
  pageCounterListenner();
}

function renderPageCounter() {
  const contactsPageCounterDiv = document.querySelector('.pageNav');

  let pageCounterHTML = `
  <nav aria-label="navigation">
    <ul class="pagination">`

  // Check if current page is 1 to disable previous button
  if (currentCompanyPage === 1) {
    pageCounterHTML += `<li class="page-item disabled"><button id="contact-page-previous" class="page-link text-internify">Précédent</button></li>`
  } else {
    pageCounterHTML += `<li class="page-item"><button id="contact-page-previous" class="page-link text-internify">Précédent</button></li>`
  }

  let pageIndex = 1;
  for (let i = 0; i < companies.length; i += 8) {

    if (currentCompanyPage === pageIndex) {
      pageCounterHTML += `<li class="page-item"><button class="page-link active text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`
    } else {
      pageCounterHTML += `<li class="page-item"><button class="page-number-button page-link text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`
    }

    pageIndex += 1;
  }

  // Check if current page is equals to pageIndex (max page index) to disable next button
  // pageIndex-1 because pageIndex start at 1 and add 1 for the last page
  if (currentCompanyPage === pageIndex - 1) {
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
    currentCompanyPage -= 1;
    loadCompanyPage();
  });

  nextButton.addEventListener('click', (e) => {
    e.preventDefault();
    currentCompanyPage += 1
    loadCompanyPage();
  });

  numberButtons.forEach(button => {
    button.addEventListener('click', (e) => {
      e.preventDefault();
      currentCompanyPage = parseInt(button.getAttribute('data-page'), 10);
      loadCompanyPage();
    });
  });
}

function renderSearchBar() {
  const searchBar = document.querySelector('.search-bar');

  searchBar.innerHTML = `
    <div class="mb-4 d-flex justify-content-center w-auto">
       <input type="search" class="form-control w-25 mx-4" id="datatable-search-input" placeholder="Entrer le nom de l’entreprise"> 
       <button class="btn btn-primary btn-create-company">Ajouter une entreprise</button>
    </div>
  `;

  listennerSearchBar();
  listennerCreateCompany();
}

function listennerSearchBar() {
  const inputElement = document.getElementById('datatable-search-input');

  inputElement.addEventListener('input', (e) => {

    const input = e.target.value;

    if (input === '') {
      companies = defaultCompanies;
      currentCompanyPage = 1;
      loadCompanyPage();
    } else {
      companies = defaultCompanies.filter(
          company => company.tradeName.toLowerCase().startsWith(
              input.toLowerCase()));
      currentCompanyPage = 1;
      loadCompanyPage();
    }

  });
}

function listennerCreateCompany() {
  const btnCreateCompany = document.querySelector('.btn-create-company');
  btnCreateCompany.addEventListener('click', (e) => {
    e.preventDefault();
    Navigate("/addCompany");
  });
}

export default ContactAddPage;






