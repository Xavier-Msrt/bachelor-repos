import Navigate from "../Components/Router/Navigate";

const roles = {
  STUDENT: "Étudiant(e)",
  TEACHER: "Enseignant(e)",
  ADMINISTRATIVE: "Administratif"
}

let userSave = null;

// USER
const getToken = () => localStorage.getItem("token") || sessionStorage.getItem(
    "token");

const getAndSetUser = async () => {
  const token = getToken();

  if (token == null) {
    if (window.location.pathname !== '/register') {
      Navigate('/login');
    }
    return null;
  }

  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/users/me`,
      options);
  if (!response.ok) {
    localStorage.clear();
    sessionStorage.clear();
    Navigate('/login');
    return null;
  }

  const user = await response.json();
  userSave = JSON.stringify(user);
  return userSave;
}

const setUser = (user) => {
  userSave = JSON.stringify(user);
}
const getUser = () => JSON.parse(userSave);

// ROLE
const roleEnToFr = (role) => roles[role];
const isStudent = () => getUser().role.toUpperCase() === "STUDENT";
const isTeacher = () => getUser().role.toUpperCase() === "TEACHER";
const isAdministrative = () => getUser().role.toUpperCase()
    === "ADMINISTRATIVE";

export {
  getToken,
  setUser,
  getUser,
  getAndSetUser,
  roleEnToFr,
  isStudent,
  isTeacher,
  isAdministrative
};
