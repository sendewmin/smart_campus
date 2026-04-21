# smart_campus
### Part 1 — JAX-RS Resource Lifecycle
*Question: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance
created per request or is it a singleton? How does this affect in-memory data management?*

JAX-RS creates a new resourceper request, so a seperate signlton is used to safely manage shared data across requests.

---

### Part 1 — HATEOAS
*Question: Why is Hypermedia (HATEOAS) considered a hallmark of advanced REST design?
How does it benefit client developers?*

It includes links in response which shortens the dependence on static docs and stops breakage when urls change.

---

### Part 2 — Full Objects vs IDs
*Question: When returning a list of rooms, what are the implications of returning only
IDs versus full room objects?*

returning only ids reduces bandwidth but increases extra requests. Full objects increases payload size but reduces round trips.

---

### Part 2 — DELETE Idempotency
*Question: Is your DELETE operation idempotent? What happens if the same DELETE request
is sent multiple times?*

It ia indepotent, the implemented API returns 204 on 1st deletion and 404 later. Diffrent status codes dont break idempotency.

---

### Part 3 — @Consumes Mismatch
*Question: What happens if a client sends data in a different format than what @Consumes
specifies?*

JAX-RS returns "415 unsupported media type" automatically.

---

### Part 3 — @QueryParam vs Path Param
*Question: Why is @QueryParam better than putting the filter in the URL path for
filtering collections?*

query params are smantically corret for optional fileters, can be combined easily. Path imply a distinct resurce heirarchy which is not proactical for filtering.

---

### Part 4 — Sub-Resource Locator Benefits
*Question: What are the architectural benefits of the Sub-Resource Locator pattern?*

The pattern delegates nested paths to dedicated classes, improving separation of concerns, testability, and maintainability.

---

### Part 5 — 422 vs 404
*Question: Why is 422 Unprocessable Entity more semantically accurate than 404 when a
referenced resource doesn't exist inside a valid JSON payload?*

404 means the url doesnt exist but 422 is more accurate because the request format it finr but the data has a semantic error.

---

### Part 5 — Stack Trace Security Risks
*Question: What are the cybersecurity risks of exposing Java stack traces to API
consumers?*

 reveals class names, library versions, file paths, and internal logic which can help attackers find weaknesses or make targeted attacks.

---

### Part 5 — Filters vs Manual Logging
*Question: Why use JAX-RS filters for logging instead of manually adding Logger
statements to every method?*

ilters handle cross-cutting concerns (like logging) without repeating code,keeping resource classes clean.

---

## Academic Integrity
This project was developed as part of the 5COSC022W Client-Server Architectures module
at the University of Westminster.