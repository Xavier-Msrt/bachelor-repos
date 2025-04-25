import logo from '../../../img/logo_PAE.png'
import {getToken, setUser} from '../../../utils/user';
import Navigate from '../../Router/Navigate';
import {getAndSetInternship} from "../../../utils/internship";
import clearPage from "../../../utils/render";
import {clearNavbar} from "../../Navbar/Navbar";

const registerStructure = () => {
  const main = document.querySelector('main');
  main.innerHTML = `<div class="container mt-5">
        <div class="row">
            <div class="col"></div>
            <div class="col">
                <img src=${logo} width=350px height=80px class='mx-auto'>
                <form class="card text-dark  border-primary bg-white mb-3 mt-3  shadow p-3" style="border-radius: 15px;">
                    <h2 class="text-internify text-decoration-underline mt-3 ms-3 text-center">Se créer un compte</h2>
                    
                    <div class="card-body">
                    
                    <!-- Input LastName-->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Nom* :</label>
                            <div >
                                <input type="text" class="form-control " id="lastName" placeholder="Entrez votre nom" required>
                            </div>
                        </div>
                        
                      <!-- Input Fistname-->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Prénom* :</label>
                            <div class="mx-auto">
                                <input type="text" class="form-control" id="firstName" placeholder="Entrez votre prénom" required>
                            </div>
                        </div>
                        
                      <!-- Input PhoneNumber-->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Numéro de téléphone* :</label>
                            <div class="mx-auto">
                                <input type="tel" class="form-control" id="phoneNumber" placeholder="Entrez votre numéro de téléphone" required>
                            </div>
                        </div>
                        
                       <!-- Input Email-->
                        <div class="row align-items-center mb-2">
                            <label class="text-internify">Email* :</label>
                            <div class="mx-auto">
                              <input type="email" class="form-control" id="email" placeholder="Entrez votre email" required>
                            </div>
                        </div>  
                        
                        <!-- Input Role-->
                       <div id="role" class="col-md">
                           <div class="row align-items-center mb-2">
                                <label class="select-role">Rôle* :</label>
                                <div class="mx-auto">
                                   <select class="form-select" id="select-role" required>
                                    <option value="STUDENT" selected>Sélectionez un rôle</option>
                                    <option value="TEACHER">Professeur</option>
                                    <option value="ADMINISTRATIVE">Administratif</option>
                                  </select>
                                </div>
                            </div>  
                       </div>
                       
                        <!-- Input Password-->  
                        <div class="row align-items-center mb-2">
                            <div class="mx-auto">
                                <label class="text-internify">Mot de passe* :</label>
                                <input type="password" id="pwd" class="form-control" placeholder="Entrez votre mot de passe" required>
                            </div>
                        </div>
                        
                        <div class="row align-items-center mb-4">
                            <div class="mx-auto">
                                <label class="text-internify">Confirmez le mot de passe* :</label>
                                <input type="password" id="pwdConf" class="form-control" placeholder="Entrez à nouveau votre mot de passe" required>
                            </div>
                        </div>
                        <div class="mt-n1" id="error"></div>
              
                        <div>
                            <input type="submit" class="btn btn-primary" id="register" value="S'inscrire">
                        </div>
        
                        
                    </div>
                    
                    <div class=" mb-4 text-center">
                       Vous avez déjà un compte ? <a href="" id = "toLogin" class="text-internify">Se connecter</a>
                    </div>

                </form>
            </div>
            <div class="col"></div>
        </div>`;

  document.getElementById('role').style.display = 'none';
};

const registerListenner = () => {
  // register
  const register = document.querySelector('#register');
  register.addEventListener('click', async (e) => {
    e.preventDefault();

    const error = document.querySelector('#error');

    // get the value of the inputs
    const lastNameUser = document.querySelector('#lastName').value;
    const firstNameUser = document.querySelector('#firstName').value;
    const phoneNumberUser = document.querySelector('#phoneNumber').value;
    const emailUser = document.querySelector('#email').value;
    const role = document.querySelector('#select-role').value;
    const password = document.querySelector('#pwd').value;
    const pwdConf = document.querySelector('#pwdConf').value;

    if (password !== pwdConf) {
      error.innerHTML = `
            <p class="text-danger">
              Mots de passe différent !
            </p>`;
    }

    const options = {
      method: 'POST',
      body: JSON.stringify({
        lastNameUser,
        firstNameUser,
        phoneNumberUser,
        role,
        emailUser,
        password,
      }),
      headers: {
        'Content-Type': 'application/json',
      },
    };
    let user;
    const response = await fetch(`${process.env.API_BASE_URL}/auths/register`,
        options);
    if (response.ok) {
      user = await response.json();
      sessionStorage.setItem('token', user.token);
      setUser(user.user);
      const internship = await getAndSetInternship();
      if (internship != null) {
        Navigate('/internship');
        return;
      }
      Navigate('/');

    } else {
      user = null;
      error.style.display = 'block';
      if(response.status === 504) {
        error.innerHTML = `
          <p class="text-danger">
           Serveur hors ligne
          </p>`;
      }else {
        error.innerHTML = `
        <p class="text-danger">
         ${await response.text()}
        </p>`;
      }

    }
    error.style.display = 'block';

  });

  // go to login page
  const toLogin = document.querySelector('#toLogin');
  toLogin.addEventListener('click', (e) => {
    e.preventDefault();
    Navigate('/login');
  });

  // check if the email ends with @vinci.be
  const email = document.querySelector('#email');
  email.addEventListener('input', () => {
    if (email.value.endsWith('@vinci.be')) {
      document.getElementById('role').style.display = 'block';
    } else {
      document.getElementById('role').style.display = 'none';
    }
  });
};

const registerPage = () => {
  if (getToken() != null) {
    Navigate('/');
    return;
  }

  clearPage();
  clearNavbar();

  registerStructure();
  registerListenner();
};

export default registerPage;
