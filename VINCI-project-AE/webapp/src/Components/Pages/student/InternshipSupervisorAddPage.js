import {getToken, isStudent} from "../../../utils/user";
import Navigate from "../../Router/Navigate";
import {uriExtract} from "../../../utils/utils";
import logo from '../../../img/logo_PAE.png'
import clearPage from "../../../utils/render";
import {clearNavbar} from "../../Navbar/Navbar";

let companyId;
const InternshipSupervisorAddPage = () => {
    clearPage();
    clearNavbar();
    // Only students can access this page
    if (isStudent()) {
        // Navbar();

        const params = uriExtract();
        if (!params.has('companyId')) {
            Navigate('/');
        }

        companyId = params.get('companyId');

        renderInternSuperVisor();
        listennerInternSuperVisor();
    } else {
        Navigate('/');
    }
}

function renderInternSuperVisor() {
    const main = document.querySelector('main');
    main.innerHTML = `<div class="container mt-5">
      <div class="row">
        <div class="d-flex justify-content-center">
          <img src=${logo} width=350px height=80px class='mx-auto'>
        </div>
        <div>
          <button class="btn btn-primary btn-square bg-primary text-white back-btn">
              <span class="bi bi-arrow-left h1"></span>
          </button>
        </div>
      </div>

        <div class="row">
            <div class="col"></div>
            <div class="col">
                <form class="card text-dark  border-primary bg-white mb-3 mt-3  shadow p-3" style="border-radius: 15px;">
                    <h2 class="text-internify text-decoration-underline mt-3 ms-3 text-center">Ajouter un responsable de stage</h2>
                    
                    <div class="card-body">
                    
                    <!-- Input lastName -->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Nom* :</label>
                            <div >
                                <input type="text" class="form-control " id="lastName" placeholder="Entrez le nom du reponsable" required>
                            </div>
                        </div>
                        
                        
                    <!-- Input firstName -->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Prénom* :</label>
                            <div >
                                <input type="text" class="form-control " id="firstName" placeholder="Entrez le prénom du reponsable" required>
                            </div>
                        </div>
                        
                        
                    <!-- Input phoneNumber -->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Numéro de téléphone* :</label>
                            <div >
                                <input type="text" class="form-control " id="phoneNumber" placeholder="Entrez son numéro de téléphone" required>
                            </div>
                        </div>
                        
                        
                    <!-- Input email -->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Email* :</label>
                            <div >
                                <input type="text" class="form-control " id="email" placeholder="Entrez son email" required>
                            </div>
                        </div>    
                    
                    <!-- Input correctInfo -->
                        <div class="form-check mb-4">
                          <input class="form-check-input" type="checkbox" value="" id="checkbox-correctinfo">
                          <label class="form-check-label fs-6" for="checkbox-correctinfo">
                            En cochant cette case, je certifie que toutes ces informations sont correctes.
                          </label>
                        </div>
                        
                        <div class="mt-n1" id="error"></div>

                        
                    <!-- Button addSuperVisor -->
                        <div>
                            <input type="submit" class="btn btn-primary" id="addSuperVisor" value="Ajouter le responsable de stage">
                        </div>       
                    </div>
                </form>
            </div>
            <div class="col"></div>
        </div>`;
}

function listennerInternSuperVisor() {
    const backBtn = document.querySelector('.back-btn');
    backBtn.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate(`/contact?id=${companyId}`);
    });

    const addSuperVisor = document.getElementById('addSuperVisor');
    addSuperVisor.addEventListener('click', async (e) => {
        e.preventDefault();
        const correctInfo = document.querySelector('#checkbox-correctinfo').checked;

        const error = document.getElementById('error');
        if (!correctInfo) {
            error.innerHTML = `
            <p class="text-danger">
              Veuillez certifier que les informations sont correctes
            </p>`;
            setTimeout(() => {
                error.innerHTML = '';
            }, 5000);
        } else {
            const lastNameSupervisor = document.querySelector('#lastName').value;
            const firstNameSupervisor = document.querySelector('#firstName').value;
            const phoneNumberSupervisor = document.querySelector(
                '#phoneNumber').value;
            const emailSupervisor = document.querySelector('#email').value;

            const response = await fetch(
                `${process.env.API_BASE_URL}/internshipSupervisors`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: getToken()
                    },
                    body: JSON.stringify({
                        lastNameSupervisor,
                        firstNameSupervisor,
                        phoneNumberSupervisor,
                        emailSupervisor,
                        companyIdSupervisor: companyId
                    })
                });
            if (!response.ok) {
                error.style.display = 'block';
                if (response.status === 504) {
                    error.innerHTML = `
                  <p class="text-danger">
                   Serveur hors ligne
                  </p> `;
                } else {
                    error.innerHTML = `
                  <p class="text-danger">
                   ${await response.text()}
                  </p>`;
                    setTimeout(() => {
                        error.innerHTML = '';
                    }, 5000);
                }
            } else {
                const supervisor = await response.json();
                Navigate(`/contact?id=${companyId}&idSup=${supervisor.idSupervisor}`)
            }
        }
    });
}

export default InternshipSupervisorAddPage;