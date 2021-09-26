# alura-bookstore-api
REST API project using Spring Boot to manage a bookstore. #WIP

# REST API
The REST API to the example app is described below.

# Registering an Author

__Request__

```POST /authors```

```
{
    "name": "John Ronald Reuel Tolkien",
    "email": "tolkien@example.com",
    "birthdate": "1973-09-02",
    "miniResume": "An English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
}
```

__Response__

```HttpStatus: 200```

__Rules__

```All fields are mandatories.```

# Get Authors List

__Request__

```GET /authors```

__Response__

```HttpStatus: 200```
```
[
    {
        "id": 1,
        "name": "John Ronald Reuel Tolkien",
        "email": "tolkien@example.com",
        "birthdate": "1973-09-02",
        "miniResume": "An English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
    }
]
```
