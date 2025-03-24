Documentation de l'API de Conversion de Devises
Ce document décrit comment utiliser l'API de conversion de devises.

Base URL
La base URL de l'API est construite à partir de la propriété api.prefix de votre application Spring Boot.  Dans le code fourni, les endpoints commencent par ${api.prefix}/currencies.

Endpoint : Convertir un montant d'une devise à une autre
URL : ${api.prefix}/currencies

Méthode : POST

Description : Convertit un montant d'une devise source vers une devise de destination.

Request
Content Type : application/json

Body :

{
  "amount": number,
  "sourceCurrency": string,
  "desiredCurrency": string
}

* `amount` : Le montant à convertir. Doit être un nombre.
* `sourceCurrency` : La devise source (code ISO 4217). Doit être une chaîne de caractères.
* `desiredCurrency` : La devise de destination (code ISO 4217). Doit être une chaîne de caractères.

Response
Status Codes :

200 OK : Conversion réussie.

400 Bad Request : Devise invalide.

503 Service Unavailable : Le service de conversion externe est indisponible.

Body :

En cas de succès (200 OK) :

{
  "message": "Conversion successful",
  "data": {
    "base_code": string,
    "target_code": string,
    "conversion_rate": number,
    "conversion_result": number
  }
}

    * `message` : Un message indiquant le succès de l'opération.
    * `data` : Un objet contenant les détails de la conversion :
        * `base_code` : La devise source.
        * `target_code` : La devise de destination.
        * `conversion_rate` : Le taux de change utilisé pour la conversion.
        * `conversion_result` : Le résultat de la conversion.

* **En cas d'erreur (400 ou 503) :**

{
  "message": string,
  "data": null
}

    * `message` : Un message d'erreur décrivant le problème.
    * `data`:  Sera null.

Exemple
Request :

POST /api/currencies
Content-Type: application/json

{
  "amount": 100,
  "sourceCurrency": "EUR",
  "desiredCurrency": "USD"
}

Response (200 OK) :

{
  "message": "Conversion successful",
  "data": {
    "base_code": "EUR",
    "target_code": "USD",
    "conversion_rate": 1.10,
    "conversion_result": 110.00
  }
}

Response (400 Bad Request) :

{
  "message": "Les devises fournies sont invalides",
  "data": null
}

Gestion des erreurs
L'API gère les erreurs potentielles et renvoie des réponses appropriées :

InvalidCurrencyException : Retourné avec un code 400 Bad Request si la combinaison de devises fournie n'est pas valide. Le message d'erreur précisera la nature du problème.

ApiConnectionException : Retourné avec un code 503 Service Unavailable si l'API de conversion externe est inaccessible ou renvoie une réponse inattendue.

Notes importantes
L'API nécessite une clé API valide pour fonctionner.  Elle est configurée via la propriété API_PUBLIC_KEY.

Les codes de devise doivent être au format ISO 4217 et en majuscules.

L'API s'appuie sur une API externe (exchangerate-api.com) pour effectuer la conversion. Assurez-vous que cette API est disponible.

Le préfixe d'API ${api.prefix} est configurable dans l'application Spring Boot.
