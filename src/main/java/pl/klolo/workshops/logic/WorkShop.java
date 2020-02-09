package pl.klolo.workshops.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.klolo.workshops.domain.*;
import pl.klolo.workshops.domain.Currency;
import pl.klolo.workshops.mock.HoldingMockGenerator;

import static java.util.Objects.*;

class WorkShop {

  /**
   * Lista holdingów wczytana z mocka.
   */
  private final List<Holding> holdings;

  // Predykat określający czy użytkownik jest kobietą
  private final Predicate<User> isWoman = user -> Sex.WOMAN.equals(user.getSex());

  WorkShop() {
    final HoldingMockGenerator holdingMockGenerator = new HoldingMockGenerator();
    holdings = holdingMockGenerator.generate();
  }

  /**
   * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma.
   */
  long getHoldingsWhereAreCompanies() {
    //return holdings.size();
    long numberOfHoldingsWithCompanies = 0L;
    for (Holding holding : holdings) {
      if (nonNull(! holding.getCompanies().isEmpty())) {
        numberOfHoldingsWithCompanies++;
      }
    }
    return numberOfHoldingsWithCompanies;
  }
  /**
   * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma. Napisz to za pomocą strumieni.
   */
  long getHoldingsWhereAreCompaniesAsStream() {

    return holdings
            .stream()
            .filter(holding -> nonNull(holding.getCompanies()))
            .filter(holding -> holding.getCompanies().isEmpty())
            .count();
  }

  /**
   * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy.
   */
  List<String> getHoldingNames() {
    List<String> holdingNames = new ArrayList<>();
    for (Holding holding : holdings) {
      String holdingName = holding.getName().toLowerCase();
      holdingNames.add(holdingName);
    }
    return holdingNames;
  }

  /**
   * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy. Napisz to za pomocą strumieni.
   */
  List<String> getHoldingNamesAsStream() {
    return holdings
            .stream()
            .map(holding -> holding.getName())
            .map(holdingName -> holdingName.toLowerCase())
            .collect(Collectors.toList());
  }

  /**
   * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane. String ma postać: (Coca-Cola, Nestle, Pepsico)
   */
  String getHoldingNamesAsString() {
    List <String> holdingNames = new ArrayList<>();
    for (Holding holding : holdings) {
      holdingNames.add(holding.getName());
    }
    Collections.sort(holdingNames);
    String concatenatedNames = "(";
    for (int i = 0; i < holdingNames.size(); i++) {
      concatenatedNames += holdingNames.get(i);
      if (!(i == holdingNames.size() - 1)){
        concatenatedNames += ", ";
      }
    }
    concatenatedNames += ")";
    return concatenatedNames;
  }

  /**
   * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane. String ma postać: (Coca-Cola, Nestle, Pepsico). Napisz to za pomocą strumieni.
   */
  String getHoldingNamesAsStringAsStream() {
    return holdings
            .stream()
            .map(holding -> holding.getName())
            .sorted()
            .collect(Collectors.joining(", ", "(", ")"));
  }

  /**
   * Zwraca liczbę firm we wszystkich holdingach.
   */
  long getCompaniesAmount() {
    long acc = 0L;
    for (Holding holding : holdings)
      acc += holding.getCompanies().size();
    return acc;
  }

  /**
   * Zwraca liczbę firm we wszystkich holdingach. Napisz to za pomocą strumieni.
   */
  long getCompaniesAmountAsStream() {
    return holdings
            .stream()
            .flatMap(holding -> holding.getCompanies().stream())
            .count();
  }

  /**
   * Zwraca liczbę wszystkich pracowników we wszystkich firmach.
   */
  long getAllUserAmount() {
    long usersAmount = 0L;
    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies())
        usersAmount += company.getUsers().size();
    }
  return usersAmount;
  }

  /**
   * Zwraca liczbę wszystkich pracowników we wszystkich firmach. Napisz to za pomocą strumieni.
   */
  long getAllUserAmountAsStream() {
    return holdings
            .stream()
            .flatMap(holding -> holding.getCompanies().stream()
            .flatMap(company -> company.getUsers().stream()))
            .count();
  }
  long getAllUserAmountAsStream2() {
    return holdings
            .stream()
            .flatMap(holding -> holding.getCompanies().stream())
            .mapToInt(company -> company.getUsers().size())
            .sum();
  }

  /**
   * Zwraca listę wszystkich nazw firm w formie listy.
   */
  List<String> getAllCompaniesNames() {
    List<String> companiesNames = new ArrayList<>();
    for (Holding holding : holdings)
      for (Company company : holding.getCompanies())
        companiesNames.add(company.getName());
    return companiesNames;
  }

  /**
   * Zwraca listę wszystkich nazw firm w formie listy. Tworzenie strumienia firm umieść w osobnej metodzie którą później będziesz wykorzystywać. Napisz to za
   * pomocą strumieni.
   */
  List<String> getAllCompaniesNamesAsStream() {
    return holdings
            .stream()
            .flatMap(holding -> holding.getCompanies().stream())
            .map(company -> company.getName())
            .collect(Collectors.toList());
  }

  List<String> getAllCompaniesNamesAsStream2() {
    return getCompanyStream()
            .map(company -> company.getName())
            .collect(Collectors.toList());
  }

  /**
   * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList.
   */
  LinkedList<String> getAllCompaniesNamesAsLinkedList() {
    return null;
  }

  /**
   * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList. Obiektów nie przepisujemy po zakończeniu działania strumienia. Napisz to za
   * pomocą strumieni.
   */
  LinkedList<String> getAllCompaniesNamesAsLinkedListAsStream() {
    return null;
  }

  /**
   * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+"
   */
  String getAllCompaniesNamesAsString() {
    String acc = "";
    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        acc += ("+" + company.getName());
      }
    }
    return acc.substring(1);
  }

  /**
   * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+" Napisz to za pomocą strumieni.
   */
  String getAllCompaniesNamesAsStringAsStream() {
    return getCompanyStream()
            .map(company -> company.getName())
            .collect(Collectors.joining("+"));
  }

  /**
   * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+". Używamy collect i StringBuilder. Napisz to za pomocą
   * strumieni.
   * <p>
   * UWAGA: Zadanie z gwiazdką. Nie używamy zmiennych.
   */
  String getAllCompaniesNamesAsStringUsingStringBuilder() {
    return null;
  }

  /**
   * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach.
   */
  long getAllUserAccountsAmount() {
    long accountAmount = 0L;
    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies())
         for (User user : company.getUsers())
           accountAmount += user.getAccounts().size();
    }

    return accountAmount;
  }

  /**
   * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach. Napisz to za pomocą strumieni.
   */
  long getAllUserAccountsAmountAsStream() {
    return getCompanyStream()
            .flatMap(company -> company.getUsers().stream())
            .flatMap(user -> user.getAccounts().stream())
            .count();
  }
  long getAllUserAccountsAmountAsStream2() {
    return getUserStream()
            .flatMap(user -> user.getAccounts().stream())
            .count();
  }

  /**
   * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości występują bez powtórzeń i są posortowane.
   */
  String getAllCurrencies() {
    Set<String> currencies = new HashSet<>();
    for (Holding holding : holdings)
      for (Company company : holding.getCompanies())
        for (User user : company.getUsers())
          for (Account account : user.getAccounts())
            currencies.add(account.getCurrency().toString());

    List<String> currencyList = new ArrayList<>(currencies);
    Collections.sort(currencyList);
    return currencyList.toString().replaceAll("[\\[|\\]]", "");
  }

  /**
   * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości występują bez powtórzeń i są posortowane. Napisz to za pomocą strumieni.
   */
  String getAllCurrenciesAsStream() {
    return getAccoutStream()
            //.map(Account::getCurrency)
            .map(account -> account.getCurrency())
            .distinct()
            //.map(Enum::toString)
            .map(currency -> currency.toString())
            .sorted()
            .collect(Collectors.joining(", "));
  }

  /**
   * Metoda zwraca analogiczne dane jak getAllCurrencies, jednak na utworzonym zbiorze nie uruchamiaj metody stream, tylko skorzystaj z  Stream.generate.
   * Wspólny kod wynieś do osobnej metody.
   *
   * @see #getAllCurrencies()
   */
  String getAllCurrenciesUsingGenerate() {
    return null;
  }

  /**
   * Zwraca liczbę kobiet we wszystkich firmach.
   */
  long getWomanAmount() {
    long womenAmount = 0L;
    for (Holding holding : holdings)
      for (Company company : holding.getCompanies())
        for (User user : company.getUsers())
          //if(user.getSex().equals(Sex.WOMAN))
          //if(Sex.WOMAN.equals(user.getSex()))
          if(isWoman.test(user)) // korzysta z predykatu
            womenAmount++;
    return womenAmount;
  }

  /**
   * Zwraca liczbę kobiet we wszystkich firmach. Powtarzający się fragment kodu tworzący strumień uzytkowników umieść w osobnej metodzie. Predicate określający
   * czy mamy doczynienia z kobietą inech będzie polem statycznym w klasie. Napisz to za pomocą strumieni.
   */
  long getWomanAmountAsStream() {
    return getUserStream()
            //.map(user -> user.getSex()) ie jest potrzebne
            //.filter(sex -> Sex.WOMAN.equals(sex) // poprawne ale bez użycia predicatu
            .filter(isWoman) // z użyciem predicatu
            .count();
  }

  /**
   * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency. Ustaw precyzje na 3 miejsca po przecinku.
   */
  BigDecimal getAccountAmountInPLN(final Account account) {
    return account
            .getAmount()
            .multiply(BigDecimal.valueOf(account.getCurrency().rate))
            .setScale(3, RoundingMode.HALF_DOWN);
  }

  /**
   * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency. Napisz to za pomocą strumieni.
   */
  //Robienie streamu jest bezsensowne w typ przypadku, bo powyższe rozwiązanie jest wystarczające.
  BigDecimal getAccountAmountInPLNAsStream(final Account account) {
    return Optional
            .ofNullable(account)
            .map(acc -> acc
                    .getAmount()
                    .multiply(BigDecimal.valueOf(account.getCurrency().rate))
                    .setScale(3, RoundingMode.HALF_DOWN))
            //.get()
            .orElse(BigDecimal.ZERO);
  }

  /**
   * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency  i sumuje ją.
   */
  BigDecimal getTotalCashInPLN(final List<Account> accounts) {
    BigDecimal balanceInPLN = BigDecimal.ZERO;
    for (Account account : accounts)
      /*balanceInPLN = account
              .getAmount()
              .multiply(BigDecimal.valueOf(account.getCurrency().rate))
              .setScale(3, RoundingMode.HALF_DOWN)
              .add(balanceInPLN);*/
      //poniżej ładniejsza wersja
      balanceInPLN = balanceInPLN
              .add(getAccountAmountInPLN(account));

    return balanceInPLN;
  }

  /**
   * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency  i sumuje ją. Napisz to za pomocą strumieni.
   */
  BigDecimal getTotalCashInPLNAsStream(final List<Account> accounts) {
    return accounts
            .stream()
            //.map(account -> account.getAmount())
            .map(this::getAccountAmountInPLNAsStream)
            .reduce((bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2))
            .get();
  }

  /**
   * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek.
   */
  Set<String> getUsersForPredicate(final Predicate<User> userPredicate) {
    Set<String> usersNames = new HashSet<>();
    for (Holding holding : holdings)
      for (Company company : holding.getCompanies())
        for (User user : company.getUsers())
          if(userPredicate.test(user)) // korzysta z predykatu
            usersNames.add(user.getFirstName());
    return usersNames;
  }

  /**
   * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek. Napisz to za pomocą strumieni.
   */
  Set<String> getUsersForPredicateAsStream(final Predicate<User> userPredicate) {
    return getUserStream()
            .filter(userPredicate)
            .map(user -> user.getFirstName())
            .collect(Collectors.toSet());

  }

  /**
   * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn i zwraca ich imiona w formie listy.
   */
  List<String> getOldWoman(final int age) {
    List<User> users = new ArrayList<>();
    for (Holding holding : holdings)
      for (Company company : holding.getCompanies())
        for (User user : company.getUsers())
          if (user.getAge() > age) {
            users.add(user);
          }
    System.out.println(users);
          List<String> olderWomanNames = new ArrayList<>();
          for (User user : users)
            if (isWoman.test(user))
              olderWomanNames.add(user.getFirstName());

    //users.removeIf(u -> !isWoman.test(u)); dobre, jeśli nie trzeba wyświetlić imion
    return olderWomanNames;
  }

  /**
   * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn i zwraca ich imiona w formie listy. Napisz
   * to za pomocą strumieni.
   */
  List<String> getOldWomanAsStream(final int age) {
    return getUserStream()
            .filter(user -> user.getAge() > age)
            .peek(user -> System.out.println(user))
            .filter(isWoman)
            .map(User::getFirstName)
            .collect(Collectors.toList());
  }

  /**
   * Dla każdej firmy uruchamia przekazaną metodę.
   */
  void executeForEachCompany(final Consumer<Company> consumer) {
    getCompanyStream().forEach(consumer);
  }

  /**
   * Wyszukuje najbogatsza kobietę i zwraca ja. Metoda musi uzwględniać to że rachunki są w różnych walutach.
   */
  Optional<User> getRichestWoman() {
    User richestWomen = null;
    BigDecimal richestWomanMany = BigDecimal.ZERO;
    for (Holding holding : holdings)
      for (Company company : holding.getCompanies())
        for (User user : company.getUsers())
          if (isWoman.test(user)) {
            BigDecimal totalCashInPLN = getTotalCashInPLN(user.getAccounts());
            if (totalCashInPLN.compareTo(richestWomanMany) > 0) {
              richestWomen = user;
              richestWomanMany = totalCashInPLN;
            }
          }
    return Optional.ofNullable(richestWomen);
  }

  /**
   * Wyszukuje najbogatsza kobietę i zwraca ja. Metoda musi uzwględniać to że rachunki są w różnych walutach. Napisz to za pomocą strumieni.
   */
  Optional<User> getRichestWomanAsStream() {
    return getUserStream()
            .filter(isWoman)
            .max(Comparator.comparing(user -> getTotalCashInPLNAsStream(user.getAccounts())));

  }

  /**
   * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia.
   */
  Set<String> getFirstNCompany(final int n) {
    Set<String> companyNames = new HashSet<>();
    for (Holding holding : holdings)
      for (Company company : holding.getCompanies())
        if (companyNames.size() < n )
          companyNames.add(company.getName());
    return companyNames;
  }

  /**
   * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia. Napisz to za pomocą strumieni.
   */
  Set<String> getFirstNCompanyAsStream(final int n) {
    return getCompanyStream()
            .limit(n)
            .map(company -> company.getName())
            .collect(Collectors.toSet());
  }

  /**
   * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metdę getAccountStream. Jeżeli nie udało się znaleźć najpopularnijeszego
   * rachunku metoda ma wyrzucić wyjątek IllegalStateException.
   */ //sprawdzić wszystkie rachunki i znaleźć najczęściej posiadany
  AccountType getMostPopularAccountType() {
    Map<AccountType, Integer> map = new EnumMap<>(AccountType.class);

    AccountType accountName = null;
    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies())
        for (User user : company.getUsers())
          for (Account account : user.getAccounts())
            map.compute(account.getType(), (accountType, integer) -> isNull(integer) ? 1 : integer + 1);
    }
    AccountType winner = null;
    Integer winnerCount = 0;
    for (Map.Entry<AccountType, Integer> entry : map.entrySet()) {
      if (winnerCount < entry.getValue()) {
        winner = entry.getKey();
        winnerCount = entry.getValue();
      }
    }
      if (nonNull(winner)) {
        return winner;
      }
      throw new IllegalArgumentException();
  }

  /**
   * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metdę getAccountStream. Jeżeli nie udało się znaleźć najpopularnijeszego
   * rachunku metoda ma wyrzucić wyjątek IllegalStateException. Pierwsza instrukcja metody to return. Napisz to za pomocą strumieni.
   */
  AccountType getMostPopularAccountTypeAsStream() {
    return getAccoutStream()
            .map(Account::getType)
            //poniej: grupujemy i ilość wystąpień wpadających do colectora
            .collect(Collectors.groupingBy(accountType -> accountType, Collectors.counting()))
            .entrySet()
            .stream()
            .max(Comparator.comparingLong(Map.Entry::getValue))
            .orElseThrow(IllegalArgumentException::new)
            .getKey();
  }

  /**
   * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca wyjątek IllegalArgumentException.
   */
  User getUser(final Predicate<User> predicate) {
    return null;
  }

  /**
   * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca wyjątek IllegalArgumentException. Napisz to
   * za pomocą strumieni.
   */
  User getUserAsStream(final Predicate<User> predicate) {
    return null;
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników.
   */
  Map<String, List<User>> getUserPerCompany() {
    Map<String, List<User>> mapOfUsers = new HashMap<>();
    for (Holding holding : holdings)
      for (Company company : holding.getCompanies())
        mapOfUsers.put(company.getName(), company.getUsers());
    return mapOfUsers;
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników. Napisz to za pomocą strumieni.
   */
  Map<String, List<User>> getUserPerCompanyAsStream() {
    return getCompanyStream()
            .collect(Collectors.toMap(Company::getName, Company::getUsers));
    //.collect(Collectors.toMap(company -> company.getName(), company -> company.getUsers()));
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako string składający się z imienia i nazwiska. Podpowiedź:
   * Możesz skorzystać z metody entrySet.
   */
  Map<String, List<String>> getUserPerCompanyAsString() {
    Map<String, List<String>> mapOfUsersFullName = new HashMap<>();

    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        List<String> fullUserName = new ArrayList<>();
        for (User user : company.getUsers()) {
          fullUserName.add(user.getFirstName() + " " + user.getLastName());
        }
        mapOfUsersFullName.put(company.getName(), fullUserName);
      }
    }
    /*for (Holding holding : holdings)
      for (Company company : holding.getCompanies())
        mapOfUsersFullName.put(company.getName(), fullUserName);*/

    return mapOfUsersFullName;
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako string składający się z imienia i nazwiska. Podpowiedź:
   * Możesz skorzystać z metody entrySet. Napisz to za pomocą strumieni.
   */
  Map<String, List<String>> getUserPerCompanyAsStringAsStream() {
    return null;
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty typu T, tworzonych za pomocą przekazanej
   * funkcji.
   */
  <T> Map<String, List<T>> getUserPerCompany(final Function<User, T> converter) {
    return null;
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty typu T, tworzonych za pomocą przekazanej funkcji.
   * Napisz to za pomocą strumieni.
   */
  <T> Map<String, List<T>> getUserPerCompanyAsStream(final Function<User, T> converter) {
    return null;
  }

  /**
   * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą. Osoby "innej" płci mają zostać zignorowane. Wartością
   * jest natomiast zbiór nazwisk tych osób.
   */
  Map<Boolean, Set<String>> getUserBySex() {
    return null;
  }

  /**
   * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą. Osoby "innej" płci mają zostać zignorowane. Wartością
   * jest natomiast zbiór nazwisk tych osób. Napisz to za pomocą strumieni.
   */
  Map<Boolean, Set<String>> getUserBySexAsStream() {
    return null;
  }

  /**
   * Zwraca mapę rachunków, gdzie kluczem jesy numer rachunku, a wartością ten rachunek.
   */
  Map<String, Account> createAccountsMap() {
    return null;
  }

  /**
   * Zwraca mapę rachunków, gdzie kluczem jesy numer rachunku, a wartością ten rachunek. Napisz to za pomocą strumieni.
   */
  Map<String, Account> createAccountsMapAsStream() {
    return null;
  }

  /**
   * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń.
   */
  String getUserNames() {
    return null;
  }

  /**
   * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń. Napisz to za pomocą strumieni.
   */
  String getUserNamesAsStream() {
    return null;
  }

  /**
   * zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10.
   */
  Set<User> getUsers() {
    return null;
  }

  /**
   * zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10. Napisz to za pomocą strumieni.
   */
  Set<User> getUsersAsStream() {
    return null;
  }

  /**
   * Zwraca użytkownika, który spełnia podany warunek.
   */
  Optional<User> findUser(final Predicate<User> userPredicate) {
    return null;
  }

  /**
   * Zwraca użytkownika, który spełnia podany warunek. Napisz to za pomocą strumieni.
   */
  Optional<User> findUserAsStream(final Predicate<User> userPredicate) {
    return null;
  }

  /**
   * Dla podanego użytkownika zwraca informacje o tym ile ma lat w formie: IMIE NAZWISKO ma lat X. Jeżeli użytkownik nie istnieje to zwraca text: Brak
   * użytkownika.
   * <p>
   * Uwaga: W prawdziwym kodzie nie przekazuj Optionali jako parametrów. Napisz to za pomocą strumieni.
   */
  String getAdultantStatusAsStream(final Optional<User> user) {
    return null;
  }

  /**
   * Metoda wypisuje na ekranie wszystkich użytkowników (imie, nazwisko) posortowanych od z do a. Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred
   * Pasibrzuch, Adam Wojcik
   */
  void showAllUser() {
    throw new IllegalArgumentException("not implemented yet");
  }

  /**
   * Metoda wypisuje na ekranie wszystkich użytkowników (imie, nazwisko) posortowanych od z do a. Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred
   * Pasibrzuch, Adam Wojcik. Napisz to za pomocą strumieni.
   */
  void showAllUserAsStream() {
    throw new IllegalArgumentException("not implemented yet");
  }

  /**
   * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu przeliczona na złotówki.
   */
  Map<AccountType, BigDecimal> getMoneyOnAccounts() {
    return null;
  }

  /**
   * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu przeliczona na złotówki. Napisz to za pomocą
   * strumieni. Ustaw precyzje na 0.
   */
  Map<AccountType, BigDecimal> getMoneyOnAccountsAsStream() {
    return null;
  }

  /**
   * Zwraca sumę kwadratów wieków wszystkich użytkowników.
   */
  int getAgeSquaresSum() {
    return -1;
  }

  /**
   * Zwraca sumę kwadratów wieków wszystkich użytkowników. Napisz to za pomocą strumieni.
   */
  int getAgeSquaresSumAsStream() {
    return -1;
  }

  /**
   * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się powtarzać, wszystkie zmienną muszą być
   * final. Jeżeli podano liczbę większą niż liczba użytkowników należy wyrzucić wyjątek (bez zmiany sygnatury metody).
   */
  List<User> getRandomUsers(final int n) {
    return null;
  }

  /**
   * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się powtarzać, wszystkie zmienną muszą być
   * final. Jeżeli podano liczbę większą niż liczba użytkowników należy wyrzucić wyjątek (bez zmiany sygnatury metody). Napisz to za pomocą strumieni.
   */
  List<User> getRandomUsersAsStream(final int n) {
    return null;
  }

  /**
   * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma pieniędzy
   * na rachunku danego typu przeliczona na złotkówki.
   */
  Map<AccountType, Map<User, BigDecimal>> getAccountUserMoneyInPLNMap() {
    return null;
  }

  /**
   * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma pieniędzy
   * na rachunku danego typu przeliczona na złotkówki.  Napisz to za pomocą strumieni.
   */
  Map<AccountType, Map<User, BigDecimal>> getAccountUserMoneyInPLNMapAsStream() {
    return null;
  }

  /**
   * Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana po
   * ilości środków na koncie w kolejności od największej do najmniejszej ich ilości liczonej w złotówkach.
   */

  Map<Permit, List<User>> getUsersByTheyPermitsSorted() {
    return null;
  }

  /**
   * Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana po
   * ilości środków na koncie w kolejności od największej do najmniejszej ich ilości liczonej w złotówkach. Napisz to za pomoca strumieni.
   */

  Map<Permit, List<User>> getUsersByTheyPermitsSortedAsStream() {
    return null;
  }

  /**
   * Podziel użytkowników na tych spełniających podany predykat i na tych niespełniających. Zwracanym typem powinna być mapa Boolean => spełnia/niespełnia,
   * List<Users>
   */
  Map<Boolean, List<User>> divideUsersByPredicate(final Predicate<User> predicate) {
    return null;
  }

  /**
   * Podziel użytkowników na tych spełniających podany predykat i na tych niespełniających. Zwracanym typem powinna być mapa Boolean => spełnia/niespełnia,
   * List<Users>. Wykonaj zadanie za pomoca strumieni.
   */
  Map<Boolean, List<User>> divideUsersByPredicateAsStream(final Predicate<User> predicate) {
    return null;
  }

  /**
   * Zwraca strumień wszystkich firm.
   */
  private Stream<Company> getCompanyStream() {
    return holdings
            .stream()
            .flatMap(holding -> holding.getCompanies().stream());
  }

  /**
   * Zwraca zbiór walut w jakich są rachunki.
   */
  private Set<Currency> getCurenciesSet() {
    return null;
  }

  /**
   * Tworzy strumień rachunków.
   */
  private Stream<Account> getAccoutStream() {
    return getUserStream()
            .flatMap(user -> user.getAccounts().stream());
  }

  /**
   * Tworzy strumień użytkowników.
   */
  private Stream<User> getUserStream() {
    return getCompanyStream()
            .flatMap(company -> company.getUsers().stream());
  }

}