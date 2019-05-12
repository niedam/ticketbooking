# ticketbooking

Projekt wykonany na potrzeby rekrutacji do Touk.

Aby zbudować i uruchomić projekt należy wpisać do wiersza poleceń:
```
./runapp.sh
```

Aby uruchomić demo należy wpisać do wiersza poleceń:
```
./test.sh
```

Wersja Javy: 11.0.2


### Endpointy

* `/date` - przelicza datę na milisekundy, przydatne przy wyznaczania przedziału, w którym szukamy seansów.
Przyjmuje dane w postaci JSON poprzez POST:

  ```{"day": <dzien miesiaca>, "month": <numer miesiaca>, "year": <rok>, "hours": <godzina>, "minutes": <minuta>}```
  
* `/screening` - wyświetla seanse w podanym przedziale czasu (zapisanego w milisekundach- patrz `/date`)
Przyjmuje jako JSON poprzez POST:

  ```{"begin": <początek przedziału>, "end": <koniec przedziału>}```

* `/available_seat/?id=<id seansu>` - wyświetla miejsca, które są dostępne na dany seans. Metoda GET

* `/reservation` - przyjmuje rezerwacje klienta poprzez POST. Przyjmuje jako JSON:

  ```{"name": <imię klienta>, "surname": <nazwisko klienta>, "idScreening": <id seansu>, "seats":<lista rezerwowanych miejsc>}```
  
  gdzie `<lista rezerwowanych miejsc>` składa się z obiektów:
  
  ```{"row": <numer rzędu>, "seat": <numer miejsca w rzędzie>, "ticketType": <rodzaj biletu "A"/"S"/"C">}```
  
  A - bilet dla osoby dorosłej;
  S - bilet studencki;
  C - bilet dla dziecka 
  
  
* `/reset_reservations` (jedynie do testów!) - ścieżka czyści bazę danych z przyjętych zamówień. Metoda DELETE.
 
