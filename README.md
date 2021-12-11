# bookstore-api
Bookstore management REST API.

Project developed for the Java Bootcamp from [Alura](https://www.alura.com.br/).

### Technologies and Tools

![](https://img.shields.io/badge/Editor-IntelliJ_IDEA-informational?style=flat&logo=intellij-idea&logoColor=white&color=2b8ebc)

![](https://img.shields.io/badge/Code-Java-informational?style=flat&logo=java&logoColor=white&color=2b8ebc)
![](https://img.shields.io/badge/Code-Spring_Boot-informational?style=flat&logo=spring&logoColor=white&color=2b8ebc)
![](https://img.shields.io/badge/Code-JUnit-informational?style=flat&logo=junit5&logoColor=white&color=2b8ebc)

![](https://img.shields.io/badge/Tools-Maven-informational?style=flat&logo=apachemaven&logoColor=white&color=2b8ebc)
![](https://img.shields.io/badge/Tools-JWT-informational?style=flat&logo=jsonwebtokens&logoColor=white&color=2b8ebc)
![](https://img.shields.io/badge/Tools-MySQL-informational?style=flat&logo=mysql&logoColor=white&color=2b8ebc)
![](https://img.shields.io/badge/Tools-Flyway-informational?style=flat&logo=flyway&logoColor=white&color=2b8ebc)
![](https://img.shields.io/badge/Tools-Docker-informational?style=flat&logo=docker&logoColor=white&color=2b8ebc)
![](https://img.shields.io/badge/Tools-Swagger-informational?style=flat&logo=swagger&logoColor=white&color=2b8ebc)

![](https://img.shields.io/badge/Cloud-Heroku-informational?style=flat&logo=heroku&logoColor=white&color=2b8ebc)

## Simplified Documentation
```Expand items for details and examples```

### Authentication 

<details>
<summary><b>POST</b> /auth</summary>

_Request Example_

```json
{
  "login": "admin",
  "password": "999999"
}
```

_Response Example_

```Status: 200 OK```
```json
{
  "token": "xaxhxGxixixIxzx1xix9.exJxdxIxOxIxIx0.Nx2x3xhxSxoxXxWxDxYxdxWxdxWxNxBxxxUxBx2xIxg"
}
```
</details>

### Users

<details>
<summary><b>POST</b> /users</summary>

_Request Example_

```json
{
  "name": "Gimli",
  "login": "lockbearer",
  "email": "dwarf@mail.com",
  "profileId": 2
}
```

_Response Example_

```Status: 201 Created```
```json
{
  "id": 5,
  "name": "Gimli",
  "login": "lockbearer",
  "email": "dwarf@mail.com"
}
```
</details>

<details>
<summary><b>GET</b> /users</summary>

_Response Example_

```Status: 200 OK```
```json
{
  "content": [
    {
      "id": 1,
      "name": "Admin",
      "login": "admin",
      "email": "admin@alurabookstore.com"
    },
    {
      "id": 5,
      "name": "Gimli",
      "login": "lockbearer",
      "email": "dwarf@mail.com"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 2,
  "last": true,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "size": 10,
  "number": 0,
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```
</details>

<details>
<summary><b>PUT</b> /users</summary>

_Request Example_

```json
{
  "id": 5,
  "name": "Gimli",
  "login": "lockbearer",
  "email": "my_axe@mail.com",
  "profilesId": [1, 2]
}
```

_Response Example_

```Status: 200 OK```
```json
{
  "id": 5,
  "name": "Gimli",
  "login": "lockbearer",
  "email": "my_axe@mail.com",
  "profiles": [
    {
      "id": 1,
      "name": "ROLE_ADMIN",
      "authority": "ROLE_ADMIN"
    },
    {
      "id": 2,
      "name": "ROLE_COMMON",
      "authority": "ROLE_COMMON"
    }
  ]
}
```
</details>

<details>
<summary><b>GET</b> /users/{id}</summary>

_Response Example_

```Status: 200 OK```
```json
{
  "id": 5,
  "name": "Gimli",
  "login": "lockbearer",
  "email": "my_axe@mail.com",
  "profiles": [
    {
      "id": 1,
      "name": "ROLE_ADMIN",
      "authority": "ROLE_ADMIN"
    },
    {
      "id": 2,
      "name": "ROLE_COMMON",
      "authority": "ROLE_COMMON"
    }
  ]
}
```
</details>

<details>
<summary><b>DELETE</b> /users/{id}</summary>

_Response Example_

```Status: 204 No Content```
</details>

### Authors

<details>
<summary><b>POST</b> /authors</summary>

_Request Example_

```json
{
  "name": "John Ronald Reuel Tolkien",
  "email": "tolkien@example.com",
  "birthdate": "1892-01-03",
  "miniResume": "An English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
}
```

_Response Example_

```Status: 201 Created```
```json
{
  "id": 2,
  "name": "John Ronald Reuel Tolkien",
  "email": "tolkien@example.com",
  "birthdate": "1892-01-03",
  "miniResume": "An English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
}
```
</details>

<details>
<summary><b>GET</b> /authors</summary>

_Response Example_

```Status: 200 OK```
```json
{
  "content": [
    {
      "id": 2,
      "name": "John Ronald Reuel Tolkien",
      "email": "tolkien@example.com",
      "birthdate": "1892-01-03",
      "miniResume": "An English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
    },
    {
      "id": 3,
      "name": "Joaquim Maria Machado de Assis",
      "email": "machado@example.com",
      "birthdate": "1839-06-21",
      "miniResume": "A pioneer Brazilian novelist, poet, playwright and short story writer, widely regarded as the greatest writer of Brazilian literature."
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 20,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 2,
  "last": true,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "size": 20,
  "number": 0,
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```
</details>

<details>
<summary><b>PUT</b> /authors</summary>

_Request Example_

```json
{
  "id": 3,
  "name": "Machado de Assis",
  "birthdate": "1839-06-21",
  "email": "machado.assis@mail.com",
  "miniResume": "A pioneer Brazilian novelist, poet, playwright and short story writer, widely regarded as the greatest writer of Brazilian literature."
}
```

_Response Example_

```Status: 200 OK```
```json
{
  "id": 3,
  "name": "Machado de Assis",
  "email": "machado.assis@mail.com",
  "birthdate": "1839-06-21",
  "miniResume": "A pioneer Brazilian novelist, poet, playwright and short story writer, widely regarded as the greatest writer of Brazilian literature."
}
```
</details>

<details>
<summary><b>GET</b> /authors/{id}</summary>

_Response Example_

```Status: 200 OK```
```json
{
  "id": 3,
  "name": "Machado de Assis",
  "email": "machado.assis@mail.com",
  "birthdate": "1839-06-21",
  "miniResume": "A pioneer Brazilian novelist, poet, playwright and short story writer, widely regarded as the greatest writer of Brazilian literature."
}
```
</details>

<details>
<summary><b>DELETE</b> /authors/{id}</summary>

_Response Example_

```Status: 204 No Content```
</details>

### Books

<details>
<summary><b>POST</b> /books</summary>

_Request Example_

```json
{
  "title": "Pride and Prejudice",
  "publicationDate": "1813-01-28",
  "pages": 408,
  "authorId": 4
}
```

_Response Example_

```Status: 201 Created```
```json
{
  "id": 2,
  "title": "Pride and Prejudice",
  "publicationDate": "1813-01-28",
  "pages": 408,
  "author": {
    "id": 4,
    "name": "Jane Austen"
  }
}
```
</details>

<details>
<summary><b>GET</b> /books</summary>

_Response Example_

```Status: 200 OK```
```json
{
  "content": [
    {
      "id": 2,
      "title": "Pride and Prejudice",
      "publicationDate": "1813-01-28",
      "pages": 408,
      "author": {
        "id": 4,
        "name": "Jane Austen"
      }
    },
    {
      "id": 3,
      "title": "The Hobbit",
      "publicationDate": "1937-09-21",
      "pages": 333,
      "author": {
        "id": 2,
        "name": "John Ronald Reuel Tolkien"
      }
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 20,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 2,
  "last": true,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "size": 20,
  "number": 0,
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```
</details>

<details>
<summary><b>PUT</b> /books</summary>

_Request Example_

```json
{
  "id": 3,
  "title": "The Hobbit",
  "publicationDate": "1937-09-21",
  "pages": 310,
  "authorId": 2
}
```

_Response Example_

```Status: 200 OK```
```json
{
  "id": 3,
  "title": "The Hobbit",
  "publicationDate": "1937-09-21",
  "pages": 310,
  "author": {
    "id": 2,
    "name": "John Ronald Reuel Tolkien"
  }
}
```
</details>

<details>
<summary><b>GET</b> /books/{id}</summary>

_Response Example_

```Status: 200 OK```
```json
{
  "id": 3,
  "title": "The Hobbit",
  "publicationDate": "1937-09-21",
  "pages": 310,
  "author": {
    "id": 2,
    "name": "John Ronald Reuel Tolkien"
  }
}
```
</details>

<details>
<summary><b>DELETE</b> /books/{id}</summary>

_Response Example_

```Status: 204 No Content```
</details>

### Report

<details>
<summary><b>GET</b> /reports/bookstore</summary>

_Response Example_

```Status: 200 OK```
```json
[
  {
    "author": "John Ronald Reuel Tolkien",
    "totalBooks": 2,
    "percentage": 18.18
  },
  {
    "author": "Machado de Assis",
    "totalBooks": 5,
    "percentage": 45.45
  },
  {
    "author": "Jane Austen",
    "totalBooks": 3,
    "percentage": 27.27
  },
  {
    "author": "Graciliano Ramos de Oliveira",
    "totalBooks": 1,
    "percentage": 9.09
  }
]
```
</details>
