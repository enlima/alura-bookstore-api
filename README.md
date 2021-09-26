# bookstore-api
Bookstore management REST API using Spring Boot. #WIP

Developed for [Alura](https://www.alura.com.br/)'s Java Bootcamp 2021.


# Registering an Author

__Request__

```POST /authors```

```
{
    "name": "John Ronald Reuel Tolkien",
    "email": "tolkien@example.com",
    "birthdate": "1892-01-03",
    "miniResume": "An English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
}
```

__Response__

```HttpStatus: 200```

__Rules__

```
- All fields are mandatories; 
- Birthdate field must not be a future date;
- Mini Resume field has a 240 characters limit.
```

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
        "birthdate": "1892-01-03",
        "miniResume": "An English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
    },
    {
        "id": 2,
        "name": "Jane Austen",
        "email": "austen@example.com",
        "birthdate": "1775-12-16",
        "miniResume": "An English novelist known primarily for her six major novels, which interpret, critique and comment upon the British landed gentry at the end of the 18th century."
    }
]
```

# Registering a Book

__Request__

```POST /books```

```
{
    "title": "Pride and Prejudice",
    "releaseDate": "1813-01-28",
    "pages": 408,
    "idAuthor": 2
}
```

__Response__

```HttpStatus: 200```

__Rules__

```
- All fields are mandatories; 
- Title field has a 10 characters minimum size;
- Release Date field must not be a future date;
- Pages field must be equal or greater than 100;
- IdAuthor field must be from an already registered author.
```

# Get Books List

__Request__

```GET /books```

__Response__

```HttpStatus: 200```
```
[
    {
        "id": 1,
        "title": "Pride and Prejudice",
        "releaseDate": "1813-01-28",
        "pages": 408,
        "author": {
            "id": 2,
            "name": "Jane Austen"
        }
    },
    {
        "id": 2,
        "title": "The Hobbit",
        "releaseDate": "1937-09-21",
        "pages": 310,
        "author": {
            "id": 1,
            "name": "John Ronald Reuel Tolkien"
        }
    }
]
```
