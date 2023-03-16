# Sample to show external rules using Drools

### Run this Spring boot application

To get a suggested role for a specific applicant do a Get request to:
http://localhost:8080/applicant/suggestRole <br />
You need to specify an applicant on the payload as json:
```
{
    "name": "Peter",
    "age": 30,
    "currentSalary": 2000000,
    "experienceInYears": 20
}
```
The response for this request will be a suggested role based on the applicant 
information. <br />
To see the rule that suggests the role, do a Get request on: http://localhost:8080/applicant/suggestRole/rule <br />
<br />
If you want to play and change the rule (which is the power of using a BRMS), do a PUT request on http://localhost:8080/applicant/suggestRole/rule with the rule as text on the body of the request. <br />
<br />
If you want a ready Postman collection with the above requests, you can access it through the link: https://api.postman.com/collections/1568395-3360d712-32ef-494f-b00a-257441645f8f?access_key=PMAT-01GVNR4GTWX57XZNQ0FQ3QXTC1
---
