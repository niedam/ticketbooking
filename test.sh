#!/usr/bin/env bash

#Reset database- remove all reservations
curl -X DELETE http://localhost:8080/reset_reservations

echo ""

#Get value of date 28.05.2019 16:00 in miliseconds (long) - need as paremeter
echo -e "\e[31mGet value of date 28.05.2019 16:00 in miliseconds (long) - need as paremeter\e[0m "
curl --header "Content-Type: application/json"   --request POST  \
     --data '{"day":28, "month":5, "year":2019, "hours":16, "minutes":0}' http://localhost:8080/date

echo ""

sleep 1

#Get value of date 28.05.2019 18:30 in miliseconds (long) - need as paremeter
echo -e "\e[31mGet value of date 28.05.2019 18:30 in miliseconds (long) - need as paremeter\e[0m "
curl --header "Content-Type: application/json"   --request POST  \
     --data '{"day":28, "month":5, "year":2019, "hours":18, "minutes":30}' http://localhost:8080/date

echo ""
sleep 1

#List all screening between 29.06.2019 and 01.07.2019
echo -e "\e[31mList all screening on 28.05.2019 between 16:00 and 18:30 \e[0m "
curl --header "Content-Type: application/json"   --request POST   \
     --data '{"begin":1561730400000,"end":1561739400000}'  http://localhost:8080/screening

echo ""
sleep 1

#List all screening between 29.06.2019 and 01.07.2019
echo -e "\e[31mList all screening between 29.06.2019 and 01.07.2019\e[0m "
curl --header "Content-Type: application/json"   --request POST   \
     --data '{"begin":1561831200000,"end":1562004000000}'  http://localhost:8080/screening

echo ""
sleep 1

#List all screening between 10.05.2019 and 30.06.2019
echo -e "\e[31mList all screening between 10.05.2019 and 30.06.2019\e[0m "
curl --header "Content-Type: application/json"   --request POST   \
     --data '{"begin":1557519564837,"end":1561917600000}'  http://localhost:8080/screening

echo ""
sleep 1

#List all screening between 01.01.1970 and 02.01.1970
echo -e "\e[31mList all screening between 01.01.1970 and 02.01.1970\e[0m "
curl --header "Content-Type: application/json"   --request POST   \
     --data '{"begin":0,"end":85140000}'  http://localhost:8080/screening

echo ""
sleep 1

#Check available seats for "The Favourite" 29.06.2019 20:00
echo -e "\e[31mCheck available seats for "The Favourite" 29.06.2019 20:00\e[0m "
curl --request GET  http://localhost:8080/available_seat/?id=1

echo ""
sleep 1

#Make reservation on "The Favourite" 29.06.2019 20:00 Row 3, seat 1 and 2
echo -e "\e[31mMake reservation on "The Favourite" 29.06.2019 20:00 Row 3, seat 1 and 2\e[0m "
curl --header "Content-Type: application/json" --request POST \
     --data '{"name":"Krżyśtóf","surname":"Brzęczyszczykiewicz", "idScreening":1,
     "seats":[{"row": 3,"seat":1,"ticetType":"A"}, {"row": 3, "seat": 2, "ticketType": "S"}]}'  \
     http://localhost:8080/reservation


echo ""
sleep 1

#Check available seats for "The Cold War" 01.07.2019 20:00
echo -e "\e[31mCheck available seats for "The Cold War" 01.07.2019 20:00\e[0m "
curl --request GET  http://localhost:8080/available_seat/?id=3

echo ""
sleep 1

#Make reservation on "The Cold War" 01.07.2019 20:00 Row 3, seat 1 and 2
echo -e "\e[31mMake reservation on "The Cold War" 29.06.2019 20:00 Row 3, seat 1 and 2\e[0m "
curl --header "Content-Type: application/json" --request POST \
     --data '{"name":"Krżyśtóf","surname":"Brzęczyszczykiewicz", "idScreening":3,
     "seats":[{"row": 3,"seat":1,"ticketType":"S"}, {"row": 3, "seat": 2, "ticketType": "S"}]}'  \
     http://localhost:8080/reservation

echo ""
sleep 1

#Make reservation on "The Cold War" 01.07.2019 20:00 Row 1, seat 1 and Row 5, seat 1
echo -e "\e[31mMake reservation on "The Cold War" 29.06.2019 20:00 Row 1, seat 1 and Row 5, seat 1\e[0m "
curl --header "Content-Type: application/json" --request POST \
     --data '{"name":"Krżyśtóf","surname":"Brzęczyszczykiewicz", "idScreening":3,
     "seats":[{"row": 1,"seat":1,"ticketType":"S"}, {"row": 5, "seat": 1, "ticketType": "S"}]}'  \
     http://localhost:8080/reservation

echo ""
sleep 1

#Make reservation on "The Cold War" 01.07.2019 20:00 Row 1, seat 1; Row 2, seat 1; Row 3 seat 3; Row 4, seat 1; Row 5 seat 5
echo -e "\e[31mMake reservation on "The Cold War" 29.06.2019 20:00 Row 1, seat 1; Row 2, seat 1; Row 3 seat 3; Row 4, seat 1; Row 5 seat 5\e[0m "
curl --header "Content-Type: application/json" --request POST \
     --data '{"name":"Czesław","surname":"Miłosz", "idScreening":3,
     "seats":[{"row": 1,"seat":1,"ticketType":"A"}, {"row": 5, "seat": 1, "ticketType": "S"},
     {"row": 2, "seat": 1, "ticketType": "S"}, {"row": 4, "seat": 1, "ticketType": "C"},
     {"row": 3, "seat": 3, "ticketType": "S"}]}'  \
     http://localhost:8080/reservation

echo ""
sleep 1

#Check available seats for "The Cold War" 01.07.2019 20:00
echo -e "\e[31mCheck available seats for "The Cold War" 01.07.2019 20:00\e[0m "
curl --request GET  http://localhost:8080/available_seat/?id=3

echo ""
sleep 1

#Make reservation on "The Cold War" 01.07.2019 20:00 Row 2, seat 2; Row 4, seat 2
echo -e "\e[31mMake reservation on "The Cold War" 29.06.2019 20:00 Row 2, seat 2; Row 4, seat 2\e[0m "
curl --header "Content-Type: application/json" --request POST \
     --data '{"name":"Adam","surname":"Rozenek", "idScreening":3,
     "seats":[{"row": 2,"seat":2,"ticketType":"A"}, {"row": 4, "seat": 2, "ticketType": "C"}]}'  \
     http://localhost:8080/reservation

echo ""
sleep 1

#Make reservation on "The Cold War" 01.07.2019 20:00 Row 2, seat 2
echo -e "\e[31mMake reservation on "The Cold War" 29.06.2019 20:00 Row 2, seat 2\e[0m "
curl --header "Content-Type: application/json" --request POST \
     --data '{"name":"Adam","surname":"Rozenek", "idScreening":3,
     "seats":[{"row": 2,"seat":2,"ticketType":"A"}]}'  \
     http://localhost:8080/reservation

echo ""
sleep 1

#Make reservation on "The Cold War" 01.07.2019 20:00 Row 4, seat 2 ('E' - bad ticket type)
echo -e "\e[31mMake reservation on "The Cold War" 29.06.2019 20:00 Row 4, seat 2 ('E' - bad ticket type)\e[0m "
curl --header "Content-Type: application/json" --request POST \
     --data '{"name":"Adam","surname":"Rozenek", "idScreening":3,
     "seats":[{"row": 4,"seat":2,"ticketType":"E"}]}'  \
     http://localhost:8080/reservation

echo ""
sleep 1

#Make reservation on "The Cold War" 01.07.2019 20:00 Row 4, seat 2
echo -e "\e[31mMake reservation on "The Cold War" 29.06.2019 20:00 Row 4, seat 2\e[0m "
curl --header "Content-Type: application/json" --request POST \
     --data '{"name":"Adam","surname":"Rozenek", "idScreening":3,
     "seats":[{"row": 4,"seat":2,"ticketType":"C"}]}'  \
     http://localhost:8080/reservation

echo ""
sleep 1

#Check available seats for "The Cold War" 01.07.2019 20:00
echo -e "\e[31mCheck available seats for "The Cold War" 01.07.2019 20:00\e[0m "
curl --request GET  http://localhost:8080/available_seat/?id=3

echo ""
sleep 1

#Make reservation on "Roma" 10.05.2019 22:19 Row 1, seat 1
echo -e "\e[31mMake reservation on "Roma" 10.05.2019 22:19 Row 1, seat 1\e[0m "
curl --header "Content-Type: application/json" --request POST \
     --data '{"name":"Spóźniony","surname":"Gość", "idScreening":4,
     "seats":[{"row": 1,"seat":1,"ticketType":"C"}]}'  \
     http://localhost:8080/reservation