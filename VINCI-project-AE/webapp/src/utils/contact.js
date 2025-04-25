import {getToken} from "./user";

const contactStateEnum = {
  STARTED: "Initié",
  ADMITTED: "Pris",
  ON_HOLD: "Suspendu",
  BLACKLISTED: "Blacklisté",
  INTERRUPTED: "Interrompu",
  ACCEPTED: "Accepté",
  TURNED_DOWN: "Refusé"
}

const contactMeetingPlace = {
  REMOTE: "À distance",
  ONSITE: "Dans l'entreprise"
}

const updateContact = async (contact) => {

  const options = {
    method: 'POST',
    body: JSON.stringify({
      versionContact: contact.versionContact,
      studentIdContact: contact.studentIdContact,
      companyIdContact: contact.companyIdContact,
      schoolYearIdContact: contact.schoolYearIdContact,
      state: contact.state,
      meetingPlace: contact.meetingPlace,
      reasonRefusal: contact.reasonRefusal,
    }),
    headers: {
      'Content-Type': 'application/json',
      Authorization: getToken(),
    },
  };

  const response = await fetch(
      `${process.env.API_BASE_URL}/contacts/${contact.companyIdContact}`, options);

  return response;
};

const getContactLabel = (state) => contactStateEnum[state];

const getContactMeetingPlace = (meeting) => contactMeetingPlace[meeting];

const checkDesignation = (designation) => {
  if (designation === null) {
    return 'Aucune appelation supplémentaire';
  }
  return designation;
}

export {
  getContactLabel,
  getContactMeetingPlace,
  updateContact,
  checkDesignation
};
