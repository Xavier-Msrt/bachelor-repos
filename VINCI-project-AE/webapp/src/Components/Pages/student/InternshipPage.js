import clearPage from "../../../utils/render";
import {Navbar} from "../../Navbar/Navbar";
import {startLoading, stopLoading} from "../../../utils/loading";
import {fetchData, millisecToDate} from "../../../utils/utils";
import Navigate from "../../Router/Navigate";
import {isStudent} from "../../../utils/user";
import {
  changeInternshipProject,
  getInternship,
} from "../../../utils/internship";

const InternShipPage = async () => {

  if (!isStudent()) {
    Navigate('/');
    return;
  }

  if (getInternship() == null) {
    Navigate('/');
    return;
  }

  clearPage();
  Navbar();

  startLoading();
  const internship = getInternship();
  const contact = await fetchData(`/contacts/${internship.companyIdInternship}`);
  stopLoading();

  if ( internship === null) {
    Navigate(`/`);
    return;
  }

  renderInternshipPage(internship, contact);
  // eslint-disable-next-line no-use-before-define
  listenerSubmitChangeProject(internship);
};



function renderInternshipPage(internship, contact) {
  const main = document.querySelector('main');

  let mainHTML = `
    <div class="container-fluid container-personal-data">
       <div class="row row-personal-data">
          <div class="col-md-8 bg-secondary rounded mt-3">
            <div class="d-flex justify-content-between align-items-center">
              <h3 class="fw-bold text-left mt-3">Informations du stage</h3>
              <h3 class="fw-bold text-right mt-3">
                  ${internship.schoolYearInternship.dateStart} - ${internship.schoolYearInternship.dateEnd}
              </h3>
            </div><hr>
            <h6 class="fw-bold mb-n1">Entreprise et appelation : </h6>
  
    `
  if (internship.company.designation === null) {
    mainHTML += `<p id="company" class="fw-semibold">${internship.company.tradeName}</p>`
  } else {
    mainHTML += `<p id="company" class="fw-semibold">${internship.company.tradeName} - ${internship.company.designation}</p>`;
  }

  mainHTML += `
            <h6 class="fw-bold mb-n1">Responsable du stage :</h6>
            <div>
              <p id="internshipSupervisor"><span class="fw-semibold">
                  ${internship.internSupervisor.lastNameSupervisor} 
                  ${internship.internSupervisor.firstNameSupervisor}
                  </span>
                  <span>(<a href="mailto:${internship.internSupervisor.emailSupervisor}">${internship.internSupervisor.emailSupervisor === null ? '</a>Adresse email non communiquée)' : `${internship.internSupervisor.emailSupervisor}</a>)</span>`}</span>
                  <br><span>Numéro de téléphone : </span><span>${internship.internSupervisor.phoneNumberSupervisor}</span>
            </div>
       
            <h6 class="fw-bold mb-n1">Lieu de rencontre avec l'entreprise :</h6>
            <p id="signatureDate">${contactMeetingPlaceToLabel(contact.meetingPlace)}</p>
            
            <h6 class="fw-bold mb-n1">Date de signature de la convention :</h6>
            <p id="signatureDate">${millisecToDate(internship.signatureDate)}</p>
            
            <h6 class="fw-bold mb-n1">Sujet du stage :</h6>
            <p id="internShipProject">${internship.internShipProject === null ? 'Aucun sujet de stage ajouté' : internship.internShipProject}
            </p>
          </div>
          <div class="col-md-4 rounded mt-3">
          
            <form id="form-change" class="card text-dark  border-primary bg-white mb-2 shadow p-3" style="border-radius: 15px;">
              <h2 class="text-decoration-underline mt-3 ms-3 text-center"> 
                ${internship.internShipProject === null ? 'Ajouter un' : 'Modifier mon'} sujet de stage
              </h2>
              <div class="card-body">
                <div class="row align-items-center pt-2">
                  <div>
                    <textarea id="text-topic" placeholder="Ecrivez votre${internship.internShipProject === null ? ' ' : ' nouveau '}sujet de stage" class="form-control form-control-lg" rows="2" required></textarea>    
                  </div>
                  <div id="error-message" class="d-flex justify-content-center text-danger"></div>
                  <div class="d-flex justify-content-center">
                    <input type="submit" class="btn btn-primary mt-4" style="width: 40%" id="modify" value="${internship.internShipProject === null ? 'Ajouter' : 'Modifier'}"></input>
                  </div>
                </div>
              </div>
            </form>
          </div>
       </div>
    </div>
  `;

  main.innerHTML = mainHTML;
}
const listenerSubmitChangeProject = (internship) => {
  const form = document.querySelector('#form-change');
  const project = document.querySelector('#text-topic');
  const errorMessage = document.querySelector('#error-message');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const projectValue = project.value.trim();

    if (!projectValue) {
      errorMessage.textContent = 'Le sujet du stage ne peut pas être vide.';
      project.classList.add('border-danger');
      project.value = '';

      setTimeout(() => {
        errorMessage.textContent = '';
        project.classList.remove('border-danger');
      }, 10000);
      return;
    }

    // Clear the error message if the form is successfully submitted
    errorMessage.textContent = '';

    // eslint-disable-next-line no-param-reassign
    internship.internShipProject = projectValue;
    startLoading();
    await changeInternshipProject(internship);
    stopLoading();
    window.location.reload();
  });
}

function contactMeetingPlaceToLabel(contact) {
  switch (contact.toLowerCase()) {
    case 'onsite':
      return 'Dans l\'entreprise';
    case 'remote':
      return 'À distance';
    default:
      return 'Non défini';
  }
}

export default InternShipPage;
