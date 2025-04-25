import {getToken} from "../../../utils/user";
import Navigate from "../../Router/Navigate";
import {clearNavbar} from "../../Navbar/Navbar";
import logo from '../../../img/logo_PAE.png'

const CompanyAddPage = () => {
  const container = document.querySelector('.container');

  // if the user not come from /addContact
  if (container === null) {
    Navigate("/");
    return;
  }
  clearNavbar()
  formCreateCompany();

}

async function createCompanyRequest(company) {
  const options = {
    method: 'POST',
    body: JSON.stringify(company),
    headers: {
      'Content-Type': 'application/json',
      Authorization: getToken(),
    },
  };
  return fetch(`${process.env.API_BASE_URL}/companies`, options);
}

function designationDisplay() {
  const designation = document.querySelector('.designation');
  designation.style.display = 'none';

  const yesCheck = document.querySelector('#yes');
  yesCheck.addEventListener('input', () => {
    if (yesCheck.checked) {
      designation.style.display = 'block';
    }
  });

  const noCheck = document.querySelector('#no');
  noCheck.addEventListener('input', () => {
    if (noCheck.checked) {
      designation.style.display = 'none';
    }
  });
}

function formCreateCompany() {
  const container = document.querySelector('.container');

  container.innerHTML = `<div class="container">
      <div class="d-flex justify-content-center">
          <img src=${logo} width=350px height=80px class='mx-auto'>
        </div>
      <div class="row">
        <div>
          <button class="btn btn-primary btn-square bg-primary text-white back-btn">
              <span class="bi bi-arrow-left h1"></span>
          </button>
        </div>
      </div>
        <div class="row">
            <div class="col-3"></div>
            <div class="col-6">
                <form class="card text-dark  border-primary bg-white   shadow p-3" style="border-radius: 15px;">
                    <h2 class="text-internify text-decoration-underline ms-3 text-center">Ajouter une entreprise</h2>

                    <div class="card-body mt-2 mb-1">

                        <!-- Input tradeName-->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Nom commercial* :</label>
                            <div >
                                <input type="text" class="form-control " id="tradeName" placeholder="Entrez le nom commercial" required>
                            </div>
                        </div>

                        <!-- Input designation-->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Mon entreprise a-t-elle une appellation supplémentaire ? :</label>

                            <div class="form-check">
                              <input class="form-check-input-no" type="radio" name="flexRadioDefault" id="no" checked>
                              <label class="form-check-label" for="no">
                                Non
                              </label>

                              <input class="form-check-input-yes" type="radio" name="flexRadioDefault" id="yes" >
                              <label class="form-check-label" for="yes">
                                Oui
                              </label>
                            </div>
                        </div>

                         <!-- Input designation-->
                        <div class="row align-items-center mb-2 designation">
                            <label class="text-internify">Appellation :</label>
                            <div >
                                <input type="text" class="form-control " id="designation" placeholder="Entrez l'appellation">
                            </div>
                        </div>



                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Adresse* :</label>

                            <div class="input-group">
                                <!-- Input street-->
                                <input type="text" class="form-control col" id="street" placeholder="Entrez la rue" required>

                                <!-- Input boxnumber-->
                                <input type="text" class="form-control col" id="boxnumber" placeholder="Entrez le numero" required>
                            </div>

                            <div class="input-group mt-2">
                                <!-- Input postcode-->
                                <input type="text" class="form-control " id="postcode" placeholder="Entrez le code postal" required>

                                <!-- Input city-->
                                <input type="text" class="form-control " id="city" placeholder="Entrez la ville" required>
                            </div>
                        </div>

                        <!-- Input phoneNumber-->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Numéro de téléphone :</label>
                            <div >
                                <input type="text" class="form-control " id="phoneNumber" placeholder="Entrez le numéro de téléphone de l'entreprise">
                            </div>
                        </div>

                        <!-- Input email-->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Adresse email :</label>
                            <div >
                                <input type="text" class="form-control " id="email" placeholder="Entrez l'email">
                            </div>
                        </div>

                        <div class="mt-n1" id="error"></div>

                        <div class="mt-4">
                            <input type="submit" class="btn btn-primary" id="addCompany" value="Ajouter l’entreprise et la contacter">
                        </div>
                    </div>
                </form>
            </div>
            <div class="col-3"></div>
        </div>`;

  designationDisplay();

  const backBtn = document.querySelector('.back-btn');
  backBtn.addEventListener('click', (e) => {
    e.preventDefault();
    Navigate("/addContact");
  });

  const form = document.querySelector('form');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const tradeName = document.querySelector('#tradeName').value;
    const des = document.querySelector('#designation').value;
    const street = document.querySelector('#street').value;
    const boxNumber = document.querySelector('#boxnumber').value;
    const postCode = document.querySelector('#postcode').value;
    const city = document.querySelector('#city').value;
    const phoneNumber = document.querySelector('#phoneNumber').value;
    const email = document.querySelector('#email').value.toLowerCase();

    const error = document.querySelector('#error');

    const company = {
      tradeName,
      designation: des.length === 0 ? null : des,
      street,
      boxNumber,
      city,
      postCode,
      phoneNumberCompany: phoneNumber.length === 0 ? null : phoneNumber,
      emailCompany: email.length === 0 ? null : email,
    }

    const response = await createCompanyRequest(company);
    if (!response.ok) {
      error.style.display = "block";

      if(response.status === 504) {
        error.innerHTML = `
          <p class="text-danger">
           Serveur hors ligne
          </p>`;
      }else{
        error.innerHTML = `
          <p class="text-danger">
          ${await response.text()}
          </p>`;
      }

    } else {
      Navigate("/");
    }
  });
}

export default CompanyAddPage;