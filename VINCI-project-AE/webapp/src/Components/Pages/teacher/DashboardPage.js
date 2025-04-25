import Chart from 'chart.js/auto';
import ChartDataLabels from 'chartjs-plugin-datalabels';
import clearPage from "../../../utils/render";
import {Navbar} from "../../Navbar/Navbar";
import Navigate from "../../Router/Navigate";
import {startLoading, stopLoading} from "../../../utils/loading";
import {isTeacher} from "../../../utils/user";
import {fetchData, getCurrentAcademicYear} from "../../../utils/utils";

let currentCompanyPage = 1;
let currentSchoolYearId = 0;
let companies = [];
let schoolYears = [];

const DashboardPage = async () => {

  if (!isTeacher()) {
    Navigate('/');
    return;
  }

  clearPage();
  Navbar();

  currentCompanyPage = 1;

  startLoading();
  schoolYears = await fetchData('/schoolYears');
  currentSchoolYearId = await getCurrentAcademicYear();
  await setAllCompanies();
  const withInternship = await fetchData(
      `/internships/admin/count/${currentSchoolYearId}`);
  const allStudents = await fetchData(
      `/users/admin/count/${currentSchoolYearId}`);
  stopLoading();
  renderDashboardPage();
  initGraph(withInternship, allStudents);
  loadCompanies();
  changeYearListenner();
};

async function setAllCompanies() {
  const comp = await fetchData(`/companies/admin/${currentSchoolYearId}`);
  console.log(comp)
  companies = comp.sort((a, b) => a.tradeName.localeCompare(b.tradeName))
}

async function setAllCompaniesWithoutYear() {
  const comp = await fetchData(`/companies/admin`);
  companies = comp.sort((a, b) => a.tradeName.localeCompare(b.tradeName))
}

function renderDashboardPage() {
  const main = document.querySelector('main');

  let selectHTML = '<select id="school-year-select" class="form-select mx-auto" aria-label="Année académique" style="width: 140px;">';
  selectHTML += `<option value="none" >Toutes les années</option>`;

  schoolYears.reverse().forEach(schoolYear => {
    if (schoolYear.idSchoolYear === currentSchoolYearId) {
      selectHTML += `<option selected value="${schoolYear.idSchoolYear}" selected>${schoolYear.dateStart} - ${schoolYear.dateEnd}</option>`;
    } else {
      selectHTML += `<option value="${schoolYear.idSchoolYear}">${schoolYear.dateStart} - ${schoolYear.dateEnd}</option>`;
    }
  });
  selectHTML += '</select>';

  main.innerHTML = `
    ${selectHTML}
    
    <div class="ms-5 me-5" style="height: 65vh;">
      <div class="row h-100 mt-4">
        <div id="dashboard-bar" class="col-sm-6">
          <!-- Content for the first column goes here -->
          <h2 class="text-center">Nombre d'étudiants</h2>
          <div id="graph">
            
          </div>
        </div>
        <div class="col-sm-6">
          <!-- Content for the second column goes here -->
          <h2 class="text-center mb-4">Entreprises</h2>
          <div id="dashboard-companies-table-div"></div>
        </div>
      </div>
      
      <div class="row fixed-bottom">
        <div class="col-sm-6"></div>
        <div class="col-sm-6 ps-0 pe-4">
          <div id="dashboard-companies-page-counter" class="d-flex justify-content-center"></div>
        </div>
      </div>
    </div>
  `
}

function initGraph(withInternship, allStudents) {
  const div = document.querySelector('#graph');
  if (allStudents === 0 && withInternship === 0) {
    div.innerHTML = `<div class="d-flex align-items-center justify-content-center h4">Données indisponibles</div>`;
    return;
  }
  div.innerHTML = `<canvas id="graph"></canvas>`;
  const canvas = document.querySelector('#graph canvas');
  const ctx = canvas.getContext('2d');
  const withoutInternship = allStudents - withInternship;
  Chart.register(ChartDataLabels);
  const colors = ['#004182', 'white', 'black'];
  let borderWidthProp;
  if (withoutInternship === 0) {
   borderWidthProp = 0;
  }
  if (withInternship === 0) {
    borderWidthProp = 1;
  }
  // eslint-disable-next-line no-new
  new Chart(ctx, {
    type: 'pie',
    data: {
      labels: ['Étudiants avec stage', 'Étudiants sans stage'],
      datasets: [{
        label: 'Nombre d\'étudiants',
        data: [withInternship, withoutInternship],
        backgroundColor: [colors[0], colors[1]],
        borderColor: colors[2],
        borderWidth: borderWidthProp
      }]
    },
    options: {
      plugins: {
        datalabels: {
          color: [colors[1], colors[0]], // Couleur du texte des données
          font: {
            weight: 'bold',
            size: '18'// Style de la police du texte des données
          },
          formatter: (value) => `${value}`
        }
      }
    }
  });
}

function loadCompanies() {
  const divCompanies = document.querySelector('#dashboard-companies-table-div');

  if (companies.length === 0) {
    divCompanies.innerHTML = `<div class="d-flex align-items-center justify-content-center h4">Aucune entreprise</div>`;
    return;
  }

  divCompanies.innerHTML = `
    <div class="container p-0">
        <table class="table table-contacts table-striped-contacts table-row-nowrap table-hover border">
            <thead>
                <tr>
                    <th id="dashboard-companies-table-name" class="personal-data-contact-font cursor-clickable unselectable">Nom</th>
                    <th id="dashboard-companies-table-designation"  class="personal-data-contact-font cursor-clickable unselectable">Appellation</th>
                    <th id="dashboard-companies-table-phone"  class="personal-data-contact-font cursor-clickable unselectable">Numéro de téléphone</th>
                    <th id="dashboard-companies-table-internship"  class="personal-data-contact-font cursor-clickable unselectable">Nombre d'étudiants en stage</th>
                    <th id="dashboard-companies-table-blacklist"  class="personal-data-contact-font cursor-clickable unselectable">Blacklistée</th>
                </tr>
            </thead>
            <tbody id="dashboard-companies-rows-tbody"></tbody>
        </table>
    </div>
    <div class="d-flex justify-content-center">
        <div class="companies-list-page-counter"></div>
    </div>
    `;

  setCompaniesRows(companies);
}

function setCompaniesRows() {
  const tbody = document.querySelector('#dashboard-companies-rows-tbody');

  const tableRow = document.querySelector('thead tr');

  const divHeight = window.innerHeight;
  const rowHeight = tableRow.clientHeight;

  const numberOfRows = Math.floor((divHeight / 2.2) / rowHeight);

  const startIndex = (currentCompanyPage - 1) * numberOfRows;
  const endIndex = startIndex + numberOfRows;
  const companiesOnThisPage = companies.slice(startIndex, endIndex);

  let rows = "";

  companiesOnThisPage.forEach(company => {
    if (company.isBlacklisted) {
      rows += `<tr class="personal-data-contact-row cursor-clickable dashboard-blacklist" data-companyid="${company.idCompany}">`;
    } else {
      rows += `<tr class="personal-data-contact-row cursor-clickable" data-companyid="${company.idCompany}">`;
    }

    rows += `
            <td class="personal-data-contact-font">${company.tradeName}</td>
            <td class="personal-data-contact-font">${company.designation
        ? company.designation : '/'}</td>
            <td class="personal-data-contact-font">${company.phoneNumberCompany}</td>
            <td class="personal-data-contact-font">${company.internshipCount}</td> 
            <td class="personal-data-contact-font">${company.isBlacklisted
        ? "Oui" : "Non"}</td>
        </tr>
        `;
  });

  tbody.innerHTML = rows;

  renderPageCounterCompanies(companies, numberOfRows);
  companiesPageCounterListenner();
  orderByColumnListenner();
  goToCompanyListenner();
}

function renderPageCounterCompanies(companiesList, countCompaniesPerPage) {
  const companiesPageCounterDiv = document.querySelector(
      '#dashboard-companies-page-counter');

  let pageCounterHTML = `
      <nav aria-label="navigation">
        <ul class="pagination">`;

  // Check if current page is 1 to disable previous button
  if (currentCompanyPage === 1) {
    pageCounterHTML += `<li class="page-item disabled"><button id="company-page-previous" class="page-link text-internify">Précédent</button></li>`;
  } else {
    pageCounterHTML += `<li class="page-item"><button id="company-page-previous" class="page-link text-internify">Précédent</button></li>`
  }

  let pageIndex = 1;
  for (let i = 0; i < companiesList.length; i += countCompaniesPerPage) {

    // Check if current page to show the button active
    if (currentCompanyPage === pageIndex) {
      pageCounterHTML += `<li class="page-item"><button class="page-link active text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`;
    } else {
      pageCounterHTML += `<li class="page-item"><button class="page-number-button page-link text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`;
    }

    pageIndex += 1;
  }

  // Check if current page is equals to pageIndex (max page index) to disable next button
  // pageIndex-1 because pageIndex start at 1 and add 1 for the last page
  if (currentCompanyPage === pageIndex - 1) {
    pageCounterHTML += `<li class="page-item disabled"><button id="company-page-next" class="page-link text-internify">Suivant</button></li>`;
  } else {
    pageCounterHTML += `<li class="page-item"><button id="company-page-next" class="page-link text-internify">Suivant</button></li>`;
  }

  pageCounterHTML += `
   </ul>
  </nav>
  `

  companiesPageCounterDiv.innerHTML = pageCounterHTML;
}

function companiesPageCounterListenner() {
  const previousButton = document.querySelector('#company-page-previous');
  const nextButton = document.querySelector('#company-page-next');
  const numberButtons = document.querySelectorAll('.page-number-button');

  previousButton.addEventListener('click', (e) => {
    e.preventDefault();
    currentCompanyPage -= 1;
    loadCompanies();
  });

  nextButton.addEventListener('click', (e) => {
    e.preventDefault();
    currentCompanyPage += 1;
    loadCompanies();
  });

  numberButtons.forEach(button => {
    button.addEventListener('click', (e) => {
      e.preventDefault();
      const pageNumber = parseInt(button.getAttribute('data-page'), 10);
      if (pageNumber !== currentCompanyPage) {
        currentCompanyPage = pageNumber;
        loadCompanies();
      }
    });
  });
}

// variables to keep track of the order of the columns
let previousColumnSelected = "dashboard-companies-table-name";
let defaultOrder = true;

// sorts the companies array in ascending or descending order based on the clicked column header, toggling the order on each click
function orderByColumnListenner() {
  const nameColumn = document.querySelector('#dashboard-companies-table-name');
  const designationColumn = document.querySelector(
      '#dashboard-companies-table-designation');
  const phoneColumn = document.querySelector(
      '#dashboard-companies-table-phone');
  const internshipColumn = document.querySelector(
      '#dashboard-companies-table-internship');
  const blacklistColumn = document.querySelector(
      '#dashboard-companies-table-blacklist');

  nameColumn.addEventListener('click', () => {
    if (previousColumnSelected !== nameColumn.id) {
      companies = companies.sort(
          (a, b) => a.tradeName.localeCompare(b.tradeName));
      previousColumnSelected = nameColumn.id;
      defaultOrder = true;
    } else {
      companies = companies.sort(
          (a, b) => defaultOrder ? b.tradeName.localeCompare(a.tradeName)
              : a.tradeName.localeCompare(b.tradeName));
      defaultOrder = !defaultOrder;
    }
    loadCompanies();
  });

  designationColumn.addEventListener('click', () => {
    if (previousColumnSelected !== designationColumn.id) {
      companies = companies.sort((a, b) => {
        const aDesignation = a.designation || '';
        const bDesignation = b.designation || '';
        return aDesignation.localeCompare(bDesignation);
      });
      previousColumnSelected = designationColumn.id;
      defaultOrder = true;
    } else {
      companies = companies.sort((a, b) => {
        const aDesignation = a.designation || '';
        const bDesignation = b.designation || '';
        return defaultOrder ? bDesignation.localeCompare(aDesignation)
            : aDesignation.localeCompare(bDesignation);
      });
      defaultOrder = !defaultOrder;
    }
    loadCompanies();
  });

  phoneColumn.addEventListener('click', () => {
    if (previousColumnSelected !== phoneColumn.id) {
      companies = companies.sort(
          (a, b) => a.phoneNumberCompany.localeCompare(b.phoneNumberCompany));
      previousColumnSelected = phoneColumn.id;
      defaultOrder = true;
    } else {
      companies = companies.sort(
          (a, b) => defaultOrder ? b.phoneNumberCompany.localeCompare(
              a.phoneNumberCompany) : a.phoneNumberCompany.localeCompare(
              b.phoneNumberCompany));
      defaultOrder = !defaultOrder;
    }
    loadCompanies();
  });

  internshipColumn.addEventListener('click', () => {
    if (previousColumnSelected !== internshipColumn.id) {
      companies = companies.sort(
          (a, b) => a.internshipCount - b.internshipCount);
      previousColumnSelected = internshipColumn.id;
      defaultOrder = true;
    } else {
      companies = companies.sort(
          (a, b) => defaultOrder ? b.internshipCount - a.internshipCount
              : a.internshipCount - b.internshipCount);
      defaultOrder = !defaultOrder;
    }
    loadCompanies();
  });

  blacklistColumn.addEventListener('click', () => {
    if (previousColumnSelected !== blacklistColumn.id) {
      companies = companies.sort((a, b) => a.isBlacklisted - b.isBlacklisted);
      previousColumnSelected = blacklistColumn.id;
      defaultOrder = true;
    } else {
      companies = companies.sort(
          (a, b) => defaultOrder ? b.isBlacklisted - a.isBlacklisted
              : a.isBlacklisted - b.isBlacklisted);
      defaultOrder = !defaultOrder;
    }
    loadCompanies();
  });
}

function goToCompanyListenner() {
  const companiesRow = document.querySelectorAll('.personal-data-contact-row');

  companiesRow.forEach(row => {
    row.addEventListener('click', (e) => {
      e.preventDefault();
      const companyId = parseInt(row.getAttribute('data-companyid'), 10);
      Navigate(`/company?id=${companyId}`);
    });
  });
}

function changeYearListenner() {
  const inputElement = document.querySelector('#school-year-select')
  inputElement.addEventListener('change', async (event) => {
    event.preventDefault();

    if (event.target.value === "none") {

      startLoading();

      await setAllCompaniesWithoutYear();
      const withInternship = await fetchData(`/internships/admin/count`);
      const withoutInternship = await fetchData(`/users/admin/count`);
      initGraph(withInternship, withoutInternship);

      stopLoading();

    } else {
      currentSchoolYearId = parseInt(event.target.value, 10);

      startLoading();

      await setAllCompanies();
      const withInternship = await fetchData(
          `/internships/admin/count/${currentSchoolYearId}`);
      const withoutInternship = await fetchData(
          `/users/admin/count/${currentSchoolYearId}`);
      initGraph(withInternship, withoutInternship);

      stopLoading();
    }

    loadCompanies();
  });
}

export default DashboardPage;