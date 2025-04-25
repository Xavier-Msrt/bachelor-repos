import clearPage from "../../../utils/render";
import {Navbar} from "../../Navbar/Navbar";
import {fetchData, getCurrentAcademicYear} from "../../../utils/utils";
import {isAdministrative, isTeacher, roleEnToFr} from "../../../utils/user";
import Navigate from "../../Router/Navigate";
import {startLoading, stopLoading} from "../../../utils/loading";

let currentUserPage = 1;
let defaultUsers = [];
let users = [];
let schoolYears = [];
let currentSchoolYearId;

const UserSearchPage = async () => {

  if (!isTeacher() && !isAdministrative()) {
    Navigate('/');
    return;
  }

  clearPage();
  Navbar();

  startLoading();
  currentSchoolYearId = await getCurrentAcademicYear();
  schoolYears = await fetchData('/schoolYears');
  await setAllUsers();
  stopLoading();

  renderUserSearchPage();

  renderSearchBar();
  loadUserPage();
};

function renderUserSearchPage() {
  const main = document.querySelector('main');

  main.innerHTML = `
    <div class="container my-lg-5">
        <div class="canvas"></div> 
        <div class="search-bar"></div>
        <div class="content"></div>
        <div class="pageNav d-flex justify-content-center fixed-bottom"><div>
    </div> `;
}

async function setAllUsers() {
  const comp = await fetchData(`/users/admin/usersList`);

  defaultUsers = comp.sort((a, b) => a.lastNameUser.localeCompare(b.lastNameUser) || a.firstNameUser.localeCompare(b.firstNameUser));
  users = defaultUsers.filter(user => user.schoolYearIdUser === currentSchoolYearId || user.role.toUpperCase() !== "STUDENT");
}

function loadUserPage() {
  const content = document.querySelector('.content');

  // if no user found
  if (users.length === 0) {
    content.innerHTML = `
      <div class="d-flex justify-content-center">
          <p class="user-notfound text-internify">Aucun utilisateur trouvé</p>
      </div>
    `;

    // Remove page counter
    document.querySelector('.pageNav').innerHTML = '';
    return;
  }

  // if user found
  content.innerHTML = `
    <div class="table-responsive">
      <table class="table table-striped table-bordered table-hover">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Rôle</th>
            <th>Étudiant</th>
            <th>Année académique du stage</th>
            <th>Stage trouvé</th>
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

  const startIndex = (currentUserPage - 1) * numberOfRows;
  const endIndex = startIndex + numberOfRows;
  const usersOnThisPage = users.slice(startIndex, endIndex);

  let rows = "";

  usersOnThisPage.forEach(user => {
    const isStudent = user.role.toUpperCase() === "STUDENT";
    const haveInternship = isStudent && user.internshipSignatureDate !== null ? `Oui` : `Non`;
    const date = haveInternship === 'Oui' ? new Date(user.internshipSignatureDate) : `${user.schoolYearUser.dateStart} - ${user.schoolYearUser.dateEnd}`;
    const internshipSY = getInternshipSY(date, haveInternship);

    rows += `
    <tr class="go-to-user cursor-clickable" data-userid="${user.idUser}">
        <td style="max-width: 100px">${user.lastNameUser}</td>
        <td style="max-width: 100px" >${user.firstNameUser}</td>
        <td style="max-width: 100px">${roleEnToFr(user.role)} </td>
        <td style="max-width: 100px">${isStudent ? 'Oui' : 'Non'}</td>
        <td>${isStudent ? internshipSY : '/'}</td>
        <td style="max-width: 100px">${isStudent ? haveInternship : '/'}</td>
    </tr>
      `;
  });

  tbody.innerHTML = rows;

  const lines = document.querySelectorAll('.go-to-user');
  lines.forEach(line => {
    line.addEventListener('click', () => {
      const idUser = line.getAttribute('data-userid');
      Navigate(`/userData?id=${idUser}`);
    });
  });

  renderPageCounter(numberOfRows);
  pageCounterListenner();
}

function renderPageCounter(numberOfRows) {
  const usersPageCounterDiv = document.querySelector('.pageNav');

  let pageCounterHTML = `
  <nav aria-label="navigation">
    <ul class="pagination">`

  // Check if current page is 1 to disable previous button
  if (currentUserPage === 1) {
    pageCounterHTML += `<li class="page-item disabled"><button id="user-page-previous" class="page-link text-internify">Précédent</button></li>`
  } else {
    pageCounterHTML += `<li class="page-item"><button id="user-page-previous" class="page-link text-internify">Précédent</button></li>`
  }

  let pageIndex = 1;
  for (let i = 0; i < users.length; i += numberOfRows) {

    if (currentUserPage === pageIndex) {
      pageCounterHTML += `<li class="page-item"><button class="page-link active text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`
    } else {
      pageCounterHTML += `<li class="page-item"><button class="page-number-button page-link text-internify" data-page="${pageIndex}">${pageIndex}</button></li>`
    }

    pageIndex += 1;
  }

  // Check if current page is equals to pageIndex (max page index) to disable next button
  // pageIndex-1 because pageIndex start at 1 and add 1 for the last page
  if (currentUserPage === pageIndex - 1) {
    pageCounterHTML += `<li class="page-item disabled"><button id="user-page-next" class="page-link text-internify">Suivant</button></li>`
  } else {
    pageCounterHTML += `<li class="page-item"><button id="user-page-next" class="page-link text-internify">Suivant</button></li>`
  }

  pageCounterHTML += `
   </ul>
  </nav>
  `

  usersPageCounterDiv.innerHTML = pageCounterHTML;
}

function pageCounterListenner() {
  const previousButton = document.querySelector('#user-page-previous');
  const nextButton = document.querySelector('#user-page-next');
  const numberButtons = document.querySelectorAll('.page-number-button');

  previousButton.addEventListener('click', (e) => {
    e.preventDefault();
    currentUserPage -= 1;
    loadUserPage();

  });

  nextButton.addEventListener('click', (e) => {
    e.preventDefault();
    currentUserPage += 1
    loadUserPage();
  });

  numberButtons.forEach(button => {
    button.addEventListener('click', (e) => {
      e.preventDefault();
      currentUserPage = parseInt(button.getAttribute('data-page'), 10);
      loadUserPage();
    });
  });
}

function renderSearchBar() {
  const searchBar = document.querySelector('.search-bar');

  let selectHTML = '<select id="school-year-select" class="form-select mx-2 d-flex" aria-label="Année académique" style="width: 140px;">';
  selectHTML += `<option value="all" >Toutes les années</option>`;
  schoolYears.reverse().forEach(schoolYear => {
    if (schoolYear.idSchoolYear === currentSchoolYearId) {
      selectHTML += `<option value="${schoolYear.idSchoolYear}" selected>${schoolYear.dateStart} - ${schoolYear.dateEnd}</option>`;
    } else {
      selectHTML += `<option value="${schoolYear.idSchoolYear}">${schoolYear.dateStart} - ${schoolYear.dateEnd}</option>`;
    }
  });
  selectHTML += '</select>';

  searchBar.innerHTML = `
    <div class="form-outline mb-4 mx-auto d-flex flex-row">
       <input type="search" class="form-control w-25" id="datatable-search-input" placeholder="Rechercher un utilisateur">
       ${selectHTML}
        <div class="form-check form-switch mt-2 mx-2">
         <label class="form-check-label" for="student-filter-checkbox">Afficher étudiants uniquement</label>
         <input class="form-check-input" type="checkbox" id="student-filter-checkbox">
       </div>
    </div>
  `;

  filtersListener();
}

function getInternshipSY(date, haveInternship) {
  let year;
  if (haveInternship === 'Oui') {
    if(date.getMonth() >= 0 && date.getMonth() <= 7){// January to August (0-7 in getMonth())
      year = date.getFullYear() - 1;
    } else { // September to December
      year = date.getFullYear();
    }
  return `${year} - ${year + 1}`;
  }
  return date;
}

const filtersHandler = () => {
  const searchValue = document.getElementById('datatable-search-input').value.toLowerCase();
  const onlyStudents = document.querySelector('#student-filter-checkbox').checked;
  const selectedYear = document.getElementById('school-year-select').value;

  // Filtrer les utilisateurs en fonction des valeurs récupérées
  users = defaultUsers.filter(user => {
    const isStudent = user.role.toUpperCase() === "STUDENT";
    const isTeacherOrAdmin = user.role.toUpperCase() === "TEACHER" || user.role.toUpperCase() === "ADMINISTRATIVE";
    const matchesSearch = user.lastNameUser.toLowerCase().includes(searchValue) || user.firstNameUser.toLowerCase().includes(searchValue);
    const matchesYear = selectedYear === "all" || user.schoolYearIdUser === parseInt(selectedYear, 10) || isTeacherOrAdmin;
    const matchesRole = !onlyStudents || isStudent;

    return matchesSearch && matchesYear && matchesRole;
  });

  currentUserPage = 1;
  loadUserPage();
};

function filtersListener() {
  document.getElementById('datatable-search-input').addEventListener('input', filtersHandler);
  document.querySelector('#student-filter-checkbox').addEventListener('change', filtersHandler);
  document.getElementById('school-year-select').addEventListener('change', filtersHandler);
}

export default UserSearchPage;