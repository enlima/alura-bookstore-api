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

```Status: 201 Created```
```
{
    "id": 21,
    "name": "John Ronald Reuel Tolkien",
    "email": "tolkien@example.com",
    "birthdate": "1892-01-03",
    "miniResume": "An English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
}
```

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

```Status: 200 OK```
```
{
    "content": [
        {
            "id": 19,
            "name": "Joaquim Maria Machado de Assis",
            "email": "machado@example.com",
            "birthdate": "1839-06-21",
            "miniResume": "A pioneer Brazilian novelist, poet, playwright and short story writer, widely regarded as the greatest writer of Brazilian literature."
        },
        {
            "id": 20,
            "name": "Jane Austen",
            "email": "austen@example.com",
            "birthdate": "1775-12-16",
            "miniResume": "An English novelist known primarily for her six major novels, which interpret, critique and comment upon the British landed gentry at the end of the 18th century."
        },
        {
            "id": 21,
            "name": "John Ronald Reuel Tolkien",
            "email": "tolkien@example.com",
            "birthdate": "1892-01-03",
            "miniResume": "An English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings."
        }
    ],
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "offset": 0,
        "pageSize": 20,
        "pageNumber": 0,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 3,
    "size": 20,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
    "first": true,
    "numberOfElements": 3,
    "number": 0,
    "empty": false
}
```

# Registering a Book

__Request__

```POST /books```

```
{
    "title": "Pride and Prejudice",
    "publicationDate": "1813-01-28",
    "pages": 408,
    "authorId": 20
}
```

__Response__

```Status: 201 Created```
```
{
    "id": 25,
    "title": "Pride and Prejudice",
    "publicationDate": "1813-01-28",
    "pages": 408,
    "author": {
        "id": 20,
        "name": "Jane Austen"
    }
}
```

__Rules__

```
- All fields are mandatories; 
- Title field has a 10 characters minimum size;
- Publication Date field must not be a future date;
- Pages field must be equal or greater than 100;
- Author Id field must be from an already registered author.
```

# Get Books List

__Request__

```GET /books```

__Response__

```Status: 200 OK```
```
{
    "content": [
        {
            "id": 25,
            "title": "Pride and Prejudice",
            "publicationDate": "1813-01-28",
            "pages": 408,
            "author": {
                "id": 20,
                "name": "Jane Austen"
            }
        },
        {
            "id": 26,
            "title": "The Hobbit",
            "publicationDate": "1937-09-21",
            "pages": 310,
            "author": {
                "id": 21,
                "name": "John Ronald Reuel Tolkien"
            }
        },
        {
            "id": 27,
            "title": "O Alienista",
            "publicationDate": "1882-03-15",
            "pages": 104,
            "author": {
                "id": 19,
                "name": "Joaquim Maria Machado de Assis"
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
        "pageSize": 20,
        "pageNumber": 0,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 3,
    "size": 20,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
    "first": true,
    "numberOfElements": 3,
    "number": 0,
    "empty": false
}
```

# Get Bookstore Report (Author Stats)

__Request__

```GET /reports/bookstore```

__Response__

```Status: 200 OK```
```
[
    {
        "author": "Joaquim Maria Machado de Assis",
        "totalBooks": 5,
        "percentage": 45.45
    },
    {
        "author": "Jane Austen",
        "totalBooks": 3,
        "percentage": 27.27
    },
    {
        "author": "John Ronald Reuel Tolkien",
        "totalBooks": 2,
        "percentage": 18.18
    },
    {
        "author": "Graciliano Ramos de Oliveira",
        "totalBooks": 1,
        "percentage": 9.09
    }
]
```
