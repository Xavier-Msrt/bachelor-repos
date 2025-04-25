import routes from './routes';
import Navigate from "./Navigate";
import {removePathPrefix, usePathPrefix} from '../../utils/path-prefix';
import {getAndSetUser} from "../../utils/user";
import {getAndSetInternship} from "../../utils/internship";

let lastLocation = window.location.pathname;

const Router = () => {
  onFrontendLoad();
  onNavBarClick();
  onHistoryChange();

  lastLocation = window.location.pathname;
};

function onNavBarClick() {
  const navbarWrapper = document.querySelector('#navbarWrapper');

  navbarWrapper.addEventListener('click', (e) => {
    e.preventDefault();
    const navBarItemClicked = e.target;
    const uri = navBarItemClicked?.dataset?.uri;
    if (uri) {
      window.history.pushState({}, '', usePathPrefix(uri));
      renderComponent(uri);
    }
  });
}

function onHistoryChange() {
  window.addEventListener('popstate', () => {
    const uri = removePathPrefix(window.location.pathname);
    renderComponent(uri);
    changeNavbarButtonStatus(uri);
  });
}

function onFrontendLoad() {

  window.addEventListener('load', async () => {
    // get user data
    const user = await getAndSetUser();
    // get internship data if user is student to display good button in navbar
    if (user != null) {
      await getAndSetInternship();
    }

    const uri = removePathPrefix(window.location.pathname);
    renderComponent(uri);
  });

}

function renderComponent(uri) {
  const componentToRender = routes[uri];

  if (componentToRender === undefined) {
    Navigate('/');
  } else {
    componentToRender();
    changeNavbarButtonStatus();
  }
}

function changeNavbarButtonStatus() {
  // Underline to know which menu bar you are in
  const navLinks = document.querySelectorAll('.nav-link');

  // Check if the current location is in the navbar
  let locationFind = false;

  navLinks.forEach(link => {
    link.classList.remove("navbar-active");
  });

  navLinks.forEach(link => {
    if (link.getAttribute('data-uri') === window.location.pathname) {
      link.classList.add('navbar-active');
      lastLocation = window.location.pathname;
      locationFind = true;
    }
  });

  // If the location is not in the navbar, we check if the last location is in the navbar and we underline it
  if (!locationFind) {
    navLinks.forEach(link => {
      if (link.getAttribute('data-uri') === lastLocation) {
        link.classList.add('navbar-active');
      }
    });
  }
}

export default Router;
