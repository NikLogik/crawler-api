# CRAWLER  

***  
Simple REST-API for the grabbing sites` titles

# Usage
***

## Get list of titles
***  

### Request
#### POST /titles  
```
curl http://localhost:8080/titles -H "Content-Type: application/json" -d '{ "urls": ["https://google.com"]}'
```

### Response
```
HTTP/1.1 200 OK
Content-Type: application/json
Date: Tue, 01 Feb 2022 14:06:22 GMT
Content-Length: 58

{
    "titles": [
        {"url":"https://google.com","value":"Google"}
    ]
}
```
or if request has invalid URLs strings  
or requested URL return invalid response (not 2xxx)

```

```