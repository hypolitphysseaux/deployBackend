# TSIKT - Virtuálna bibliotéka - dokumentácia
### authors: `Ján Okál, Martin Petkeš`

Našou prioritou pri tvorbe virtuálnej bibliotéky ako nástroja, ktorý podľa zadania umožňuje čitateľom 
zdieľať a požičiavať si medzi sebou knihy bola hlavne **bezchybná funkčnosť** a celkový **dojem** pri interakcii. Od prvého momentu sme chceli
vytvoriť riešenie, ktoré bude perzistentné, pomocou technológií, ktoré sa reálne využívajú v **praxi** pri stredne veľkých, či väčších projektoch.
Preto sme sa rozhodli spraviť **webovú aplikáciu** - poskytnúť klientovi aj vizuálne príťažlivé elementy s ktorými sa dá
**jednoducho** a **efektívne** komunikovať so serverom. Keď sa smieme pozastaviť pri efektívnej komunikácií, ešte pred napísaním
prvého riadka kódu, sme sa rozhodli, že spravíme celý projekt pomocou [gitu](#git) v [Githube](#github). Pre zjednodušenie práce sme využili
[Github Desktop](#github-desktop). Súčasťou projektu je aj [MySql](#databáza) relačný, open-source databázový systém, ktorý je jedným
z **najpoužívanejších** vo svete. Keďže ide o komunikáciu typu **klient-server**, celý projekt sme si rozdelili na dve časti -
zjednodušený pohľad na ne sa dá predstaviť ako "to, čo klient vidí" (tzv. [**frontend**](#svelte-frontend)) a "to, čo sa deje na pozadí" (tzv. [**backend**](#java-backend)).
Treba povedať, keďže ide o webovú aplikáciu, komunikácia prebieha aj nad treťou vrstvou a využíva hypertextový prenosový protokol
(**HTTP**). V súčasnosti sa presadzuje komunikácia pomocou HTTPS (nadstavba HTTP nad TLS), ktorá zabezpečuje autentifikáciu a šifrovanú
komunikáciu. Toto zabezpečenie sme sa rozhodli neimplementovať, nakoľko to presahuje rámec tohto predmetu aj zadania.
Jednotlivé detaily, ako naša aplikácia funguje, nájdete nižšie. Okrem minimálnych požiadavok sme sa rozhodli //TODO

## Git vs Github
### `GIT`
Git je distribuovaný systém pre **správu verzií**, umožňuje viacerým vývojárom pracovať na jednom projekte **súčasne**. Každý má
svoju kópiu repozitára na ktorej môže pracovať offline a následne sa synchronizovať s ostatnými. Okrem toho, že je veľmi
**rýchly**, poskytuje **históriu zmien** kódu spolu s komentármi. Aj keď môžu niekedy nastať merge konflikty pri zlučovaní kódov
(hlavne pri väčších projektoch), prípadne náročnosť efektívneho sledovania zmien veľkých binárnych súborov - výhody jasne
**prevyšujú** nevýhody a git je skvelý nástroj, ktorý funguje perfektne, na čo je určený.

### `GITHUB`
Github je **webová platforma**, ktorá využiva git a poskytuje všetky jeho výhody spomenuté vyššie. Poskytuje vzdialený
repozitár a **dodatočné funkcie** pre správu projektov a spoluprácu medzi vývojármi, ktoré nie sú súčasťou gitu
(napríklad kontinuálnu integráciu (CI), kontinuálnu dodávku (CD), pull requesty a iné).

### `GITHUB DESKTOP`
Github Desktop je grafický klient pre Windows a MacOS, navrhnutý tak, aby pomocou grafického rozhrania zjednodušil
prácu s Gitom. Je blízko spätý s Githubom, čo umožňuje jednoduché publikovanie zmien a synchronizáciu medzi vzdialeným
a lokálnym repozitárom.

## Svelte frontend
Svelte je framework, ktorý enkapsuluje HTML, CSS a JavaScript do úhľadných komponentov
(súbory s príponou .svelte) vhodných na vývoj webových aplikácií.
...

## Java backend
Backend sme sa rozhodli spraviť vo frameworku Java Spring v kombinácii s Mavenom. Táto kombinácia nám značne
zjednodušila prepojenie a komunikáciu s databázou. Anotácie nad metódami a rôznymi triedami tiež prepájajú databázu
spolu s funkciami a ďalšími triedami, konštruktormi, prípadne atribútmi.
Backend máme rozdelený na rôzne **package** ako napríklad user alebo book.
### Entity class
Každá package má svoju **Entity class**, ktorá obsahuje atribúty, konštruktory, getre, setre
a anotácie, vďaka ktorým vytvoríme tabuľku v [databáze](#databáza) alebo označíme primary key.
### Service class
Trieda **Service class** obsahuje logiku spojenú s našou aplikáciou ako spracovanie, zaznamenanie alebo zmena dát.
Tiež slúži ako prostredník medzi triedou **Controller class** a **Repository interface**.
### Repository interface
Trieda **Repository Interface** obsahuje metódy, ktoré vykonávajú operácie v databáze a môžme ich ďalej používať
v **Service class**. Tento interface tiež rozširuje **JpaRepository**, ktorý obsahuje množinu CRUD(create,read,
update,delete) metód na použitie bez toho, aby sme ich špeciálne definovali.
### Controller class
Trieda **Controller class** spracováva prichádzajúce HTTP requesty, interaguje so **Service layer**, aby sa vykonala 
biznis logika. Následne táto trieda vracia HTTP odpoveď vo forme JSON formátu.

### `Package Book`
### Trieda Book
Obsahuje atribúty, getre a setre.
### Trieda BookService
Obsahuje atribúty **repozitárov** book, user a loanRequest, aby sme mali prístup k dátam v databáze v rôznych tabuľkách. 
Metódy: **getAllBooks** - z databázy získa všetky knihy, zabalí ich do listu, ktorý sa odošle v štruktúre
Response Entity spolu so stavom operácie.
**getBooks** - z databázy získa knihy podľa zadaného výrazu vo front-ende a vracia list v Response Entity.
**addNewBook** - do databázy sa pridá nová kniha s názvom, autorom, majiteľom a dátumom vytvorenia a na front-end sa
naspäť pošle Response Entity s HTTP statusom.
**deleteBook** - z front-endu príde email majiteľa knihy a primary key **id** knihy, podľa ktorého sa vymaže z databázy
daná kníha. Predtým sa ale vymažú všetky žiadosti o požičanie danej knihy.
### Interface BookRepository
Obsahuje metódy findBookByTitleContainingOrAuthorContaining, ktorá nájde knihu podľa zadaného substringu a
deleteBooksByOwner, ktorá odstráni z databázy majiteľove knihy.
### Trieda BookController
Obsahuje hlavičky metód z **BookService** spolu s potrebnými parametrami a anotácie s endpointom na front-end +
cross originom na port localhostu. Anotácia **Autowired** sama prepojí userService a vďaka nej môžeme vytvoriť inštanciu
beanu(nejakého objektu, kompnonetu).

### `Package User`
### Trieda User
Obsahuje atribúty, getre a setre.
### Trieda UserService
Opäť obsahuje atribúty **repozitárov** , aby sme mali prístup k dátam v databáze v rôznych tabuľkách.
Metódy: **getUsers** - z databázy získa všetkých užívateľov podľa zadaného stringu a vráti ich na front-end.
**registerNewUser** - do databázy zaregistruje nového používateľa, heslo sa zahashuje a vďaka anotácii @JsonIgnore sa 
tento údaj nikdy nevracia na front-end. Admin môže pridať nového admina, ale ináč sa zaregistruje bežný používateľ.
**loginUser** - S údajmi v databáze sa porovná email a hash hesla a prihlási používateľa do knižnice.
**removeUser** - z front-endu na pokyn admina príde email používateľa a následne sa vymažú všetky žiadosti o požičanie 
kníh od používateľa, na jeho knihy, ďalej knihy používateľa a potom sa vymaže samotný používateľ.
**changePassword** - používateľovi je umožnené si zmeniť heslo a v databáze sa uloží nový hash.
### Interface UserRepository
Obsahuje metódu findUserByEmailContainingOrMenoContainingOrPriezviskoContaining, kde sa vrátia užívatelia podľa zadaného
reťazca.
### Trieda UserController
Obsahuje hlavičky metód z **UserService** spolu s potrebnými parametrami a anotácie s endpointom na front-end +
cross originom na port localhostu.

### `Databáza`
Používame databázu MySQL a v Jave sme na ňu pripojení cez application.properties. Až neskôr sme začali používať relácie,
takže niektoré vzťahy nie sú ideálne vyladené a po snahe o spätnú zmenu s tým prišlo veľa bugov a problémov.
Každá **Entity class** má svoju tabuľku - tiež to naznačuje a inicializuje anotácia @Table.
Všetky tabuľky majú svoj **primary key**, aby sme mali jednoznačný identifikátor na prístup k riadkom databázy. 
### Tabuľka Book
**Id** - primary key, auto increment
**Author, title a status** - string
**Owner** - tu má byť foreign key na email používateľa, ale máme tu iba jeho mail. Uvedomujeme si túto chybu, ale spätne
to nebolo jednoduché opraviť
**Created_date** - dátum pridania knihy
### Tabuľka User
**Email** - primary key
**Is_admin** - boolean-otvorí sa front-end pre admina alebo klasického používateľa
**Meno, priezvisko, password_hash** - string
**Pocet_knih** - int
**Created_date** - dátum pridania užívateľa
### Tabuľka LoanRequest
**Request_id** - primary key
**Book_id, user_id** - foreign key na knihu a užívateľa
**status** - string
**Created_date** - dátum vytvorenia požiadania o knihu
### Tabuľka Message
**Message_id** - primary key
**Receiver_id, sender_id** - foreign key na užívateľa
**content, status** - string
**Created_date** - dátum vytvorenia správy

...

## Niečo extra
### LoanRequest
Pri žiadosti o požičanie knihy sa pošle notifikácia majiteľovi knihy, ktorý ju môže prijať alebo odmietnuť. Podľa toho
sa žiadateľovi pošle správa o prijatí/odmietnutí žiadosti. V reálnom čase sa notifikácie načítavajú a zobrazujú v účte 
prihláseného používateľa
### Message
Používatelia si medzi sebou môžu navzájom posielať správy v aplikácii. Správy prichádzajú aj automaticky po 
prijatí/odmietnutí žiadosti. Všetky správy sa uchovávajú v databáze, je možné ich vymazať, poslať hocikomju správu.
### Registration Email
Keď sa zaregistruje nový používateľ, tak mu na jeho emailovú adresu príde uvítací email od nášho **DeveloperTeamu**.
Cez application.properties sa vieme prihlásiť na náš mail aj smtp server, ktorý automaticky pošle HTML email za nás.
### Password Hasher
Pri registrácii v databáze ukladáme hash hesla a nie iba jednoduchý reťazec. Heslo je zahashované algoritmom SHA-256 a 
následne uložené do databázy. Pri zmene hesla alebo prihlásení používateľa sa porovnáva hash hesla.

#### `Bugy`
Keď viacero používateľov požiada o tú istú knihu, v zobrazení notifikácií sa po prvotnom prijatí jednej z nich
automaticky neodstráni ani jedna z nich, po druhotnom prijatí sa odstráni. Naopak pri prvotnom zamietnutí sa
správne prestane zobrazovať.

V databáze nemáme v tabuľke Book foreign key na majiteľa knihy.