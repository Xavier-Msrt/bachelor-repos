import {fetchData} from "./utils";
import {getToken, isStudent} from "./user";
import Navigate from "../Components/Router/Navigate";

let internshipSave = null;

const setInternship = (internship) => {
    internshipSave = JSON.stringify(internship !== undefined ? internship : null);
}

const getInternship = () => JSON.parse(internshipSave);

const getAndSetInternship = async () => {
    if (!isStudent())
        return null;

  const internship = await fetchData('/internships');
  setInternship(internship);
  return internshipSave;
}

const createInternship = async (internship) => {
    const options = {
      method: 'POST',
      body: JSON.stringify({
          studentIdInternship: internship.studentIdInternship,
          companyIdInternship: internship.companyIdInternship,
          schoolYearIdInternship: internship.schoolYearIdInternship,
          internSupervisorId: internship.internSupervisorId,
          signatureDate: internship.signatureDate,
          internShipProject: internship.internShipProject,
          contactInternship: internship.contactInternship
      }),
      headers: {
        'Content-Type': 'application/json',
        Authorization: getToken(),
      },
    };

    const response = await fetch(`${process.env.API_BASE_URL}/internships`, options);
    if(response.ok) {
        await getAndSetInternship();
    }
    return response;
}

const changeInternshipProject = async (internship) => {
    const options = {
      method: 'POST',
      body: JSON.stringify({
          idInternship: internship.idInternship,
          studentIdInternship: internship.studentIdInternship,
          companyIdInternship: internship.companyIdInternship,
          schoolYearIdInternship: internship.schoolYearIdInternship,
          internSupervisorId: internship.internSupervisorId,
          signatureDate: internship.signatureDate,
          internShipProject: internship.internShipProject,
          versionInternship: internship.versionInternship
      }),
      headers: {
        'Content-Type': 'application/json',
        Authorization: getToken(),
      },
    };

    const response = await fetch(`${process.env.API_BASE_URL}/internships/update`, options);
    if (!response.ok) {
        Navigate('/');
        return;
    }

    await getAndSetInternship();
}

export {
    setInternship,
    getInternship,
    getAndSetInternship,
    createInternship,
    changeInternshipProject,
};
