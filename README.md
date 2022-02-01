# CRAWLER  

### Simple REST-API for the grabbing sites` titles

# Usage

## Get list of titles

### Request
#### POST /titles  
```
curl http://localhost:8080/titles -H "Content-Type: application/json" -d '{ "urls": ["https://2gis.ru"]}'
```

### Response
```
HTTP/1.1 200 OK
Content-Type: application/json
Date: Tue, 01 Feb 2022 14:06:22 GMT
Content-Length: 58

{
    "titles": [
        {
            "url": "https://2gis.ru",
            "value": "Карта городов России: Москва, Санкт-Петербург, Новосибирск и другие города — 2ГИС"
        }
    ]
}
```
or if at least one request for URL finished exceptionally
```
curl http://localhost:8080/titles -H "Content-Type: application/json" -d '{ "urls": ["https://googasdfale", "https://2gis.ru"]}'
```

```
HTTP/1.1 200 OK
Content-Type: application/json
Date: Tue, 01 Feb 2022 23:54:13 GMT
Content-Length: 282

{
    "titles": [
        {
            "url": "https://2gis.ru",
            "title": "Карта городов России: Москва, Санкт-Петербург, Новосибирск и другие города — 2ГИС"
        }
    ],
    "errors": [
        {
            "url": "https://googasdfale",
            "error": "Unknown host: https://googasdfale"
        }
    ]
}
```