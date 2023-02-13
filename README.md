# stackoverflow-users-app


This app using the stackoverflow.com api documented at: https://api.stackexchange.com/docs retrieves 
the list of stack overflow users meeting the following criteria:
- Are located in Romania or Moldova
- Have a reputation of min 223 points.
- Answered min 1 question
- Have the tags: "java",".net","docker" or "C#"

The list of retrieved users dumps in a list in STDOUT.

Each line contains:
- User name
- Location
- Answer count
- Question count
- Tags as a comma delimited string
- Link to profile
- Link to avatar

### Run project:
- Clone this project in your IDE and run it
- Open your web-browser and go to http://localhost:8080/users/required
