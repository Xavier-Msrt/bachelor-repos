/* eslint-disable no-use-before-define */
/* eslint-disable no-param-reassign */
import clearPage from '../../../utils/render';
import {Navbar} from '../../Navbar/Navbar';
import Navigate from '../../Router/Navigate';
import {
    checkDesignation,
    getContactLabel,
    getContactMeetingPlace,
    updateContact
} from '../../../utils/contact';
import {fetchData, uriExtract} from '../../../utils/utils';
import {startLoading, stopLoading} from '../../../utils/loading';
import {createInternship} from '../../../utils/internship';

const ContactDetailsPage = async () => {
    clearPage();
    Navbar();
    const params = uriExtract();
    const idCompany = params.get('id');
    const idSup = params.get('idSup');

    startLoading();

    let contact;
    try {
        contact = await fetchData(`/contacts/${idCompany}`);
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

    const main = document.querySelector('main');
    const state = await getContactLabel(contact.state);
    const designation = checkDesignation(contact.company.designation);

    main.innerHTML = `

    <div class="div-contact">
      <div class="card m-3" style="width: 100%; background-color: #d8e0e7;">
        <div class="card-body">
        <h3 class="card-title fw-bold mt-1">Informations du contact :</h3>
        <p class="fw-bold mb-n1">État du contact : </p>
        <p class="card-text">${state}</p>
        ${detailsInfo(contact)}
        <p class="fw-bold mb-n1">Nom de l'entreprise :</p>
        <p class="card-text">${contact.company.tradeName}</p>
        <p class="fw-bold mb-n1">Appellation supplémentaire :</p>
        <p class="card-text">${designation}</p>
        <p class="fw-bold mb-n1">Adresse :</p>
        <p class="card-text">${contact.company.street}</p>
        <p class="fw-bold mb-n1">Adresse email :</p>
<p class="card-text">${contact.company.emailCompany ? contact.company.emailCompany : "Aucune adresse email fournie"}</p>        <p class="fw-bold mb-n1">Numéro de téléphone :</p>
        <p class="card-text">${contact.company.phoneNumberCompany}</p>
        </div>
      </div>
      <div class="card m-3" style="width: 100%; background-color: #d8e0e7;">
      <div class="card-body" style="border:black 3px;">
      <div class="d-flex justify-content-around align-items-center" id="title-button">
                <h3 class="card-title fw-bold mt-1">Actions :</h3>
                <button class="btn-window btn-accepted btn btn-primary">Accepter</button>
                <button class="btn-window btn-refused btn btn-primary">Refuser</button>
      </div>
        <div>
        
        ${await stateDiv(contact.state.toLowerCase())}
        
        </div>
    </div>
    
  </div>
  `;
    buttonStop(contact.state.toLowerCase());
    if (contact.state.toLowerCase() === 'started' || contact.state.toLowerCase() === 'admitted') {
        stopTheFollowEvent(contact);
    }
    if (contact.state.toLowerCase() === 'started') {
        admittedTheFollowEvent(contact);
    }
    if (contact.state.toLowerCase() === 'admitted') {
        today();
        await optionInternshipSupervisor(idCompany, idSup);
        refusedTheFollowEvent(contact);
        acceptedTheFollowEvent(contact);
        supervisorCreateEvent(contact);
        windowsAcceptedDisplay();
        windowsRefusedDisplay();
    }
};

function buttonStop(state) {
    if (state === 'started' || state === 'admitted') {
        const div = document.querySelector('#title-button');
        div.innerHTML += `  
        <button class="btn btn-primary" id="supp-btn">Ne plus suivre ce contact</button>
        `;
    }
    if (state !== 'admitted') {
        const buttons = document.querySelectorAll('.btn-window');
        // eslint-disable-next-line no-return-assign
        buttons.forEach((e) => e.style.display = 'none');
    }
}

const detailsInfo = (contact) => {
    let infos = '';
    if (contact.meetingPlace !== null && contact.meetingPlace !== 'null') {
        infos += `
    <p className="fw-bold" style="font-weight: bold; margin: auto" >Lieu de rencontre : </p>
    <p className="card-text">${getContactMeetingPlace(contact.meetingPlace)}</p>`;
    }
    if (contact.reasonRefusal !== null && contact.reasonRefusal !== 'null') {
        infos += `
    <p className="fw-bold" style="font-weight: bold; margin: auto"> Raison du refus : </p>
    <p className="card-text">${contact.reasonRefusal}</p>
    `;
    }
    return infos;
};

const stateDiv = async (state) => {
    if (state === 'started') {
        return `        
                <p class="fw-bold mb-n1">Indiquer que le contact a été pris :</p>
                <form class="form-admitted">
                <select required class="form-select border-1 border-primary shadow mt-3" aria-label="Sélectionner le lieu de rencontre">
                  <option selected>Sélectionner le lieu de rencontre</option>
                  <option value="ONSITE">Dans l'entreprise</option>
                  <option value="REMOTE">À distance</option>
                </select>
                <div class="d-flex justify-content-center mt-3">
                  <input type="submit" class="btn btn-primary w-50 " id="validate" value="Valider">
                </div>
                </form>
    `;
    }
    if (state === 'admitted') {
        return `
    <br>
    <div class ="card-contact card me-2 shadow p-3 bg-body rounded" >
        <div>
        <form class="form-accepted">
        
        <p style = "font-weight: bold;">Indiquer que l'entreprise a accepté le stage</p>

        <div class="div-accepted">
        <div class= "div-accepted-pt1" style="display: flex;">
        <div style="width: 40%">
        <p style="margin-bottom: 1px">Indiquer le responsable de stage*</p>
        <select class="form-select border-1 border-primary shadow mt-1" aria-label="Sélectionner le responsable">
            <option>Sélectionner le responsable</option>
        </select>
        <br>
        <button class="btn-primary btn btn-supervisor">Je ne trouve pas mon responsable</button>
        </div>
        <div style="width: 50%; margin-left: 10%">
        <label>Indiquer la date de signature de la convention*</label>
        <input type="date" class="date">
        </div>
        </div>

        <div style="width :100%";>
        <br>
        <p style="margin-bottom: 1px">Indiquer le sujet de stage</p>
        <textarea style="width: 100%;" class="text-topic"  rows="3" placeholder="Indiquer le sujet de stage (optionnel)"></textarea>
        </div>
        </div>
          <div class="text-center" style="padding: 3%;">
          <input style="width:30%" class="btn btn-accpted-contact btn-primary" type="submit" value="Valider"></input>
          </div>
      </form>
      <form style="display: none" class="form-refusal">
        <p style = "font-weight: bold;">Indiquer que l'entreprise a refusé le stage :</p>
        <p>Indiquer la raison pour laquelle l'entreprise a refusé le stage :</p>
        <textarea required style="width: 100%;"  class="text"  rows="3"></textarea>
        <div class="text-center" style="padding: 3%;">
          <input style="width:30%" class="btn btn-refused-contact btn-primary" type="submit" value="Valider"></input>
        </div>
      </form>
      <div class="mt-n1" id="error"></div>
      </div>
       </div>
    `;
    }

    return `<div class="d-flex align-items-center justify-content-center" style="height: 45vh"><h4 style="color: #f80707 ;">Aucune action disponible</h4></div>`;
};

const windowsAcceptedDisplay = () => {
    const button = document.querySelector('.btn-accepted');
    const formAccept = document.querySelector('.form-accepted');
    const formRefused = document.querySelector('.form-refusal');
    button.addEventListener('click', (e) => {
        e.preventDefault();
        formAccept.style.display = 'block';
        formRefused.style.display = 'none';
    })
}

const windowsRefusedDisplay = () => {
    const button = document.querySelector('.btn-refused');
    const formAccept = document.querySelector('.form-accepted');
    const formRefused = document.querySelector('.form-refusal');
    button.addEventListener('click', (e) => {
        e.preventDefault();
        formAccept.style.display = 'none';
        formRefused.style.display = 'block';
    })
}


const today = () => {
    let today1 = new Date();
    const dd = String(today1.getDate()).padStart(2, '0');
    const mm = String(today1.getMonth() + 1).padStart(2, '0');
    const yyyy = today1.getFullYear();

    today1 = `${yyyy}-${mm}-${dd}`;

    document.querySelector('.date').value = today1;
};

const optionInternshipSupervisor = async (idCompany, idSup) => {
    let listIS;
    const select = document.querySelector('.form-select');

    try {
        listIS = await fetchData(`/internshipSupervisors/${idCompany}`);

        for (let i = 0; i < listIS.length; i += 1) {
            const option = document.createElement('option');
            option.textContent = `${listIS[i].firstNameSupervisor} ${listIS[i].lastNameSupervisor} `;
            option.value = listIS[i].idSupervisor;
            option.selected = listIS[i].idSupervisor === parseInt(idSup, 10);
            select.appendChild(option);
        }
    } catch (error) {
        document.querySelector('main').innerHTML = `
    <div class="container rounded bg-primary mt-5 p-3">
        <div class="d-flex justify-content-center align-items-center h3 text-light">${error.message}</div>
    </div>
    `;
        stopLoading();
    }
};

const supervisorCreateEvent = (contact) => {
    const button = document.querySelector('.btn-supervisor');
    button.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate(`/addInternshipSupervisor?companyId=${contact.companyIdContact}`);
    });
};

const stopTheFollowEvent = (contact) => {
    const button = document.querySelector('#supp-btn');
    button.addEventListener('click', async (e) => {
        e.preventDefault();
        contact.state = 'INTERRUPTED';
        await reviewRequest(contact);
    });
};

const refusedTheFollowEvent = (contact) => {
    const form = document.querySelector('.form-refusal');
    const text = document.querySelector('.text');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        contact.state = 'TURNED_DOWN';
        contact.reasonRefusal = text.value;
        await reviewRequest(contact);
    });
};

const admittedTheFollowEvent = (contact) => {
    const form = document.querySelector('.form-admitted');
    const meetingPlace = document.querySelector('.form-select');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        contact.state = 'ADMITTED';
        contact.meetingPlace = `${meetingPlace.value.toUpperCase()}`
        const response = await updateContact(contact);
        if (response.status === 200) {
            Navigate(`/contact?id=${contact.companyIdContact}`);
        }
    });
};

const acceptedTheFollowEvent = (contact) => {
    const form = document.querySelector('.form-accepted');
    const date = document.querySelector('.date');
    const supervisor = document.querySelector('.form-select');
    const topic = document.querySelector('.text-topic');
    const error = document.querySelector('#error');
    const student = contact.studentIdContact;
    const company = contact.companyIdContact;
    const schoolyear = contact.schoolYearIdContact;
    contact.state = 'ACCEPTED';

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const internship = {
            studentIdInternship: student,
            companyIdInternship: company,
            schoolYearIdInternship: schoolyear,
            internSupervisorId: parseInt(supervisor.value, 10),
            signatureDate: date.value,
            internShipProject: (topic.value === "") ? null : topic.value,
            contactInternship: contact
        };

        const response = await createInternship(internship);
        if (response.status === 200) {
            Navigate(`/internship`);
        } else {
            error.style.display = "block";
            error.innerHTML = `
                <p class="text-danger">
                 ${await response.text()}
                </p>
              `;
            setTimeout(() => {
                error.innerHTML = '';
            }, 4000);
        }
    });
};

const reviewRequest= async (contact) => {
    const error = document.querySelector('#error');
    const response = await updateContact(contact);
    if (response.status === 200) {
        Navigate(`/contact?id=${contact.companyIdContact}`);
    } else {
        error.style.display = "block";
        error.innerHTML = `
                <p class="text-danger">
                 ${await response.text()}
                </p>
              `;
        setTimeout(() => {
            error.innerHTML = '';
        }, 4000);
    }
}
export default ContactDetailsPage;
