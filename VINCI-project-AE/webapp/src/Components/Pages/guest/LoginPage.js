import clearPage from '../../../utils/render';
import actionHidePassword from '../../../img/action-hide-password.png';
import actionShowPassword from '../../../img/action-show-password.png';
import logo from '../../../img/logo_PAE.png'
import Navigate from "../../Router/Navigate";
import {clearNavbar} from "../../Navbar/Navbar";
import {getToken, setUser} from "../../../utils/user";
import {getAndSetInternship} from "../../../utils/internship";

const loginPage = async () => {
  if (getToken() != null) {
    Navigate('/');
    return;
  }

  clearPage();
  clearNavbar();
  renderLoginPage();
  await formListenner();

};

function renderLoginPage() {
  const main = document.querySelector('main');
  const mainHTML = ` 
    <div class="container mt-5" xmlns="http://www.w3.org/1999/html">
      <div class="row">
          <div class="col"></div>
          <div class="col">
                <img src=${logo} width=350px height=80px class='mx-auto'>
                <form class="card text-dark  border-primary bg-white mb-3 mt-3 shadow p-3" style="border-radius: 15px;">
                      <h2 class="text-internify text-decoration-underline mt-3 ms-3 text-center">Connexion</h2>
                      <div class="card-body">
                            <div class="row align-items-center pt-2 pb-3">
                                <label class="text-internify">Email*</label>
                                <div class="mx-auto">
                                  <input type="email" class="form-control form-control-lg" id="email" placeholder="Entrez votre email" required>
                                </div>
                            </div>  
                            <div class="row align-items-center pt-2 py-3">
                            <label class="text-internify">Mot de passe*</label>
                                <div class="mx-auto">
<div class="input-group">
      <input type="password" id="pwd" class="form-control form-control-lg" placeholder="Entrez votre mot de passe" required>
      <button class="btn btn-outline-secondary" type="button" id="togglePassword">
        <img src="${actionShowPassword}" alt="Show Password" width="20" height="20">
      </button>
    </div>                              </div>
                            </div>
                            <div class="mt-n1" id="error"></div>
                            <div class="form-check mb-3">
                                  <input class="form-check-input" type="checkbox" value="false" id="stay-connected">
                                  <label class="form-check-label" for="defaultCheck1">Rester connecté</label>
                            </div>
                            
                            <div>
                                  <input type="submit" class="btn btn-primary" id="connect" value="Se connecter">
                            </div>
                      </div>
                      <div class="mx-5 mb-4">
                          Pas encore inscrit ? <a href="" id = "toInscription" class="text-internify">Inscrivez-vous ici</a>  
                      </div>
                      
            </form> 
          </div>
          <div class="col"></div>
    </div>  
    `

  ;

  main.innerHTML = mainHTML;

  const togglePasswordButton = document.querySelector('#togglePassword');
  const passwordInput = document.querySelector('#pwd');

  togglePasswordButton.addEventListener('click', () => {
    if (passwordInput.type === 'password') {
      passwordInput.type = 'text';
      togglePasswordButton.querySelector('img').src = actionHidePassword; // Mettre l'image de l'oeil fermé
    } else {
      passwordInput.type = 'password';
      togglePasswordButton.querySelector('img').src = actionShowPassword; // Mettre l'image de l'oeil ouvert
    }
  });
  const error = document.querySelector('#error');
  error.style.display = "none";

  const check = document.querySelector("#stay-connected");
  check.addEventListener("click", () => {
    if (check.value === "false") {
      check.value = "true";
    } else {
      check.value = "false";
    }
  });

  const toInscription = document.querySelector('#toInscription');
  toInscription.addEventListener('click', (e) => {
    e.preventDefault();
    Navigate('/register');
  });
}

async function formListenner() {
  const form = document.querySelector('form');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const emailUser = document.querySelector('#email').value;
    const password = document.querySelector('#pwd').value;

    const options = {
      method: 'POST',
      body: JSON.stringify({
        emailUser,
        password,
      }),
      headers: {
        'Content-Type': 'application/json'
      },
    };
    let user;
    const response = await fetch(`${process.env.API_BASE_URL}/auths/login`,
        options);


    if (response.ok) {
      user = await response.json();
      const checkbox = document.querySelector('#stay-connected');
      if (checkbox.value === "true") {
        localStorage.setItem("token", user.token);
      } else {
        sessionStorage.setItem("token", user.token);
      }
      setUser(user.user);
      const internship = await getAndSetInternship();
      if (internship != null) {
        Navigate('/internship');
        return;
      }
      Navigate('/');

    } else { // when fail
      user = null;
      const error = document.querySelector('#error');
      error.style.display = "block";
      if(response.status === 504) {
        error.innerHTML = `
        <p class="text-danger">
         Serveur hors ligne
        </p>
      `;
      }else{

        error.innerHTML = `
        <p class="text-danger">
          ${ await response.text()}
        </p>
      `;
      }

    }
  });
}

export default loginPage;