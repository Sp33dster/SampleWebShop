# SampleWebShop

Instrukcja uruchomienia

Utworzenie bazy danych
Dane niezbędne do utworzenia bazy danych znajdują się w definicji puli połączeń
ShopPoolJavaDB (src/main/setup/glassfish-resources.xml).

Utworzenie schematu bazy danych
Jednostki składowania aplikacji są skonfigurowane do utworzenia schematu bazy danych na
podstawie konfiguracji klas encyjnych. Uruchomienie aplikacji powinno więc doprowadzić do
utworzenia schematu.


Wczytanie danych początkowych
Do aplikacji jest dołączony plik SQL (DML) zawierający zestaw danych początkowych,
umożliwiających rozpoczęcie pracy z aplikacją. Lokalizacja tego pliku to
src/main/resources/KontaKategorieProdukty_WersjaWszystkie.sql. Należy wczytać zawartość pliku
do bazy danych SampleShop (wykonać plik w tej bazie).
