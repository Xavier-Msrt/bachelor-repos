import {getToken} from "./user";

const millisecToDate = (millisecs) => {
  const date = new Date(millisecs);
  return `${date.getDate()}/${date.getMonth()+1}/${date.getFullYear()}`
}

// Fetch data from API
async function fetchData(url) {
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: getToken(),
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}${url}`, options);
  if (!response.ok) {
    throw new Error(
        `${response.status} : ${response.statusText}`);
  }

  return response.status === 204 ? null : response.json();
}


function uriExtract() {
  const parsedUrl = new URL(window.location.href);
  return new URLSearchParams(parsedUrl.search);
}

function operationStatusSuccess(message) {
  const popup = document.querySelector('#popup');
  popup.innerHTML = `<div class="d-flex align-items-center bg-success rounded border border-success border-2 w-100 mt-3">
    <div id="message" class="mx-auto text-success fs-4">${message}<div>
  </div>`;
  popup.style.display = 'block';
}

function operationStatusFailure(message) {
  const popup = document.querySelector('#popup');
  popup.innerHTML = `<div class="d-flex align-items-center bg-danger rounded border border-danger border-2 w-100 mt-3">
    <div id="message" class="mx-auto text-danger fs-4">${message}<div>
  </div>`;
  popup.style.display = 'block';
}

async function getCurrentAcademicYear() {
  const schoolYears = await fetchData('/schoolYears');
  let id = 0;

  const date = new Date();

  const civilYear = date.getFullYear();
  const month = date.getMonth();
  let year = 0;

  if (month < 8) {

    year = civilYear - 1;
  } else {
    year += civilYear;
  }

  schoolYears.forEach((schoolYear) => {
    if (schoolYear.dateStart === year) {
      id = schoolYear.idSchoolYear;
    }
  });

  return id;

}

export {
  millisecToDate,
  fetchData,
  uriExtract,
  operationStatusSuccess,
  operationStatusFailure,
  getCurrentAcademicYear,
};