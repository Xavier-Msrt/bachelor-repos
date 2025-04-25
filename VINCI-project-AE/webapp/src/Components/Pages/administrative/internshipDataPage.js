import clearPage from "../../../utils/render";
import {Navbar} from "../../Navbar/Navbar";
import {startLoading, stopLoading} from "../../../utils/loading";
import {fetchData, millisecToDate, uriExtract} from "../../../utils/utils";
import Navigate from "../../Router/Navigate";
import {isAdministrative, isTeacher} from "../../../utils/user";

const InternShipPage = async () => {

    if (!isAdministrative() && !isTeacher()) {
        Navigate('/');
        return;
    }

    clearPage();
    Navbar();

    startLoading();
    const params = uriExtract();
    const id= params.has('id') ? params.get('id') : null;
    let internship;
    let user;
    let contact;
    try {
        // id of user
        internship = await fetchData(`/internships/admin/${id}`);
        user = await fetchData(`/users/admin/${internship.studentIdInternship}`);
        const contactList = await fetchData(`/contacts/admin/contactsList/${internship.studentIdInternship}`);
        contact = contactList.find(ct => ct.companyIdContact === internship.companyIdInternship);
    } catch (error) {
        document.querySelector('main').innerHTML = `
        <div class="container rounded bg-primary mt-5 p-3">
            <div class="d-flex justify-content-center align-items-center h3 text-light">${error.message}</div>
        </div>
        `;
        stopLoading();
        return;
    }

    stopLoading();

    if ( internship === null) {
        document.querySelector('main').innerHTML = `
        <div class="container rounded bg-primary mt-5 p-3">
            <div class="d-flex justify-content-center align-items-center h3 text-light">Stage non trouvé</div>
        </div>
        `;
        return;
    }

    renderInternshipPage(internship, user, contact);
};

function renderInternshipPage(internship, user, contact) {
    const main = document.querySelector('main');

    let mainHTML = `
    <div class="container-fluid container-personal-data">
       <div class="row row-personal-data bg-secondary rounded mt-3">
          <div class="d-flex justify-content-between align-items-center">
             <h3 class="fw-bold text-left mt-3">Informations du stage</h3>
             <h3 class="fw-bold text-right mt-3">
                ${internship.schoolYearInternship.dateStart} - ${internship.schoolYearInternship.dateEnd}
             </h3>
          </div><hr>
          <div class="col-md-6">
            <h6 class="fw-bold mb-n1">Entreprise et appelation : </h6>
    `

    if (internship.company.designation === null) {
        mainHTML += `<p id="company">${internship.company.tradeName}</p>`
    } else {
        mainHTML += `<p id="company">${internship.company.tradeName} - ${internship.company.designation}</p>`;
    }

    mainHTML += `
        <h6 class="fw-bold mb-n1">Responsable du stage :</h6>
        <div>
           <p id="internshipSupervisor">
              ${internship.internSupervisor.lastNameSupervisor} 
              ${internship.internSupervisor.firstNameSupervisor} </br>
           <span style="font-size: small;font-style: italic;">Numéro de téléphone :</span><span style="font-size: small;"> ${internship.internSupervisor.phoneNumberSupervisor}</span></br>
           <span style="font-size: small;font-style: italic;">Adresse email :</span><span style="font-size: small"> 
           <a href="mailto:${internship.internSupervisor.emailSupervisor}"> ${internship.internSupervisor.emailSupervisor === null ? '</a> Pas communiquée' : `${internship.internSupervisor.emailSupervisor}</a>`}</span>
           </p>
        </div>
                                    
        <h6 class="fw-bold mb-n1">Lieu de rencontre avec l'entreprise :</h6>
        <p id="signatureDate">${contactMeetingPlaceToLabel(contact.meetingPlace)}</p>
        
        <h6 class="fw-bold mb-n1">Date de signature de la convention :</h6>
        <p id="signatureDate">${millisecToDate(internship.signatureDate)}</p>
            
        <h6 class="fw-bold mb-n1">Sujet du stage :</h6>
        <p id="internShipProject">${internship.internShipProject === null ? 'Pas encore ajouté' : internship.internShipProject}</p>
      </div>
      <div class="col-md-6">
        <h6 class="fw-bold mb-n1">Étudiant :</h6>
        <p>${user.lastNameUser} ${user.firstNameUser}</p>
           
        <h6 class="fw-bold mb-n1">Email :</h6>
        <p><a href="mailto:${user.emailUser}">${user.emailUser}</a></p>
        
        <h6 class="fw-bold mb-n1">Numéro de téléphone :</h6>
        <p>${user.phoneNumberUser}</p>
      </div>
    </div>
  </div>
  `;

    main.innerHTML = mainHTML;
}

function contactMeetingPlaceToLabel(contact) {
    switch (contact.toLowerCase()) {
        case 'onsite':
            return 'À distance';
        case 'remote':
            return 'Dans l\'entreprise';
        default:
            return 'Non défini';
    }
}

export default InternShipPage;
