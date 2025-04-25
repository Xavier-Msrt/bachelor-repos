# Backend application (JAVA)

## Description

This repository contains the source code of the backend business application developed by Group 23 as part
of the Enterprise Application Development course.

## Context

Project Explanation

* The application, which will be developed, installed and maintained by Group 23, will enable students to keep track of
  the companies they contact, and will also enable the teachers to keep track of internship searches and internships
  obtained, as well as finding all the contact details of internship managers in one place.

## Installation

### Prerequisites

#### API

Create a dev.properties

```properties
BaseUri=http://localhost:8080/
JWTSecret=YOUR_JWT_SECRET
JWTExpiry=HOURS_EXPIRE
DbUrl=JDBC_URL_WITH_DB_NAME
DbUser=DB_USERNAME
DbPassword=DB_PASSWORD
SqlFile=init.sql
```

## Endpoints

| URI                                      | Method HTTP  | Auths?                       | Operation                                                                |
|------------------------------------------|--------------|------------------------------|--------------------------------------------------------------------------|
| **Auths**                                |              |                              |                                                                          |    
| **/auths/login**                         | POST         | No                           | READ ONE : Logs in a user and returns his token.                         |
| **/auths/register**                      | POST         | No                           | CREATE ONE: Create a user and returns his token.                         |
|                                          |              |                              |                                                                          |
| **Users**                                |              |                              |                                                                          |
| **/users/me**                            | GET          | Yes                          | READ ONE: Get the auth user information.                                 |
| **/users/admin/count**                   | GET          | Yes (Teacher)                | READ ONE: Get the total number of users                                  |
| **/users/admin/count/{yearId}**          | GET          | Yes (Teacher)                | READ ONE: Get the total number of users for a specific year.             |
| **/users/updatePhoneNumber**             | POST         | Yes                          | UPDATE ONE: Update the user's phone number.                              |
| **/users/updatePassword**                | POST         | Yes                          | UPDATE ONE: Update the user's password.                                  |
| **/users/admin/{idUser}**                | GET          | Yes (Administrative/Teacher) | READ ONE: Get the auth teacher or administrative information.            |
| **/users/admin/usersList**               | GET          | Yes (Administrative/Teacher) | READ ALL: Get all user.                                                  |
|                                          |              |                              |                                                                          |
| **Contacts**                             |              |                              |                                                                          |                                                                                                            
| **/contacts**                            | GET          | Yes (Student)                | READ ALL: Get all contacts of the auth user.                             |
| **/contacts/{idCompany}**                | GET          | Yes (Student)                | READ ONE: Get a contact of the auth user.                                |
| **/contacts**                            | POST         | Yes (Student)                | CREATE ONE: Create a contact for the auth user.                          |
| **/contacts/{companyId}**                | POST         | Yes (Student)                | UPDATE ONE: Update a contact of the auth user.                           |
| **/contacts/admin/contactsList/{idUser}** | GET          | Yes (Administrative/Teacher) | READ ALL: Get all student's contacts.                                    |
| **/contacts/admin/{idCompany}**          | GET          | Yes (Administrative/Teacher) | READ ALL: Get all company's contacts .                                   |
|                                          |              |                              |                                                                          |
| **Companies**                            |              |                              |                                                                          |
| **/companies**                           | GET          | Yes                          | READ ALL: Get all companies of the auth user.                            |
| **/companies**                           | POST         | Yes (Student)                | CREATE ONE: Create a company.                                            |
| **/companies/{companyId}**          | GET          | Yes (Administrative/Teacher) | READ ONE: Get a company.                                                 |
| **/companies/admin/blacklist**           | POST         | Yes (Teacher)                | UPDATE ONE: Update a company to blacklisted.                             |
| **/companies/admin**                     | GET          | Yes (Teacher)                | READ ALL: Get all companies with internships count                       |    
| **/companies/admin/{yearId}**            | GET          | Yes (Teacher)                | READ ALL: Get all companies with internships count from a specific year. |
|                                          |              |                              |                                                                          |
| **Internship**                           |
| **/internships**                         | GET          | Yes (Student)                | READ ONE: Get the user's internship.                                     |
| **/internships/admin/{idUser}**          | GET          | Yes (Administrative/Teacher) | READ ONE: Get the user's internship.                                     |
| **/internships/admin/count**             | GET          | Yes (Teacher)                | READ ONE: Get the total number of internship                             |
| **/internships/admin/count/{yearId}**    | GET          | Yes (Teacher)                | READ ONE: Get the total number of internship for a specific year.        |
| **/internships**                         | POST         | Yes (Student)                | CREATE ONE: Create an internship.                                        |
| **/internships/update**                  | POST         | Yes (Student)                | UPDATE ONE: Update an internship's project.                              |
|                                          |              |                              |                                                                          |
| **Internship Supervisor**                |              |                              |                                                                          |
| **/internshipSupervisors**               | POST         | Yes (Student)                | CREATE ONE: Create an supervisor.                                        |
| **/internshipSupervisors/{idCompany}**   | GET          | Yes (Student)                | GET ALL: Get all the supervisors form a company.                         |
|                                          |              |                              |                                                                          |
| **School Year**                          |              |                              |                                                                          |
| **/schoolYears**                         | GET          | Yes (Administrative/Teacher) | GET ALL: Get all the school years.                                       |

