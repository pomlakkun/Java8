package com.java.example;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * Created by Stefan Hungerbuehler on 12.02.2017.
 * Taken from: http://winterbe.com/posts/2014/03/16/java-8-tutorial/
 */
public class Examples {
    public static void main(String[] args) {

        // 1. Default Methods for Interfaces
        System.out.println("+-- 1. Default Methods for Interfaces");

        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };

        System.out.println("calculate: " + formula.calculate(100));
        System.out.println("sqrt: " + formula.sqrt(16));

        // 2. Lambda expressions
        System.out.println("+-- 2. Lambda expressions");

        List<String> names = Arrays.asList("a", "b", "c", "d");
        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });
        // shorter version:
        Collections.sort(names, (String a, String b) -> b.compareTo(a));
        // shorter version:
        Collections.sort(names, (a, b) -> b.compareTo(a));

        // 3. Functional Interfaces
        System.out.println("+-- 3. Functional Interfaces");

        Converter<String, Integer> converter = from -> Integer.valueOf(from);
        System.out.println("converted: " + converter.convert("1234"));

        // 4. Method and Constructor References
        System.out.println("+-- 4. Method and Constructor References");

        Converter<String, Double> converter1 = Double::valueOf;
        System.out.println("converted: " + converter1.convert("1234.1234"));

        Something something = new Something();
        Converter<String, String> converter3 = something::startsWith;
        System.out.println("converted: " + converter3.convert("Java Developpers"));

        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.create("Kim", "Jong");
        System.out.println("person.firstName: " + person.firstName);
        System.out.println("person.lastName: " + person.lastName);

        // 5. Lambda Scopes
        System.out.println("+-- 5. Lambda Scopes");

        int num = 5;
        Converter<Integer, String> stringConverter = from -> String.valueOf(from + num);
        System.out.println("converted: " + stringConverter.convert(12));

        // 6. Built-in Functional Interfaces
        System.out.println("+-- 6. Built-in Functional Interfaces");
        //   a) Predicates (Predicates are boolean-valued functions of one argument. The interface contains various default methods for composing predicates to complex logical terms (and, or, negate))
        System.out.println("     a) Predicates");

        Predicate<String> predicate = (s) -> s.length() > 0;
        System.out.println("predicate test: " + predicate.test("foo"));
        System.out.println("predicate test negate: " + predicate.negate().test("foo"));
        System.out.println("predicate test negate negate: " + predicate.negate().negate().test("foo"));

        Predicate<Boolean> nonNull = Objects::nonNull;
        System.out.println("predicate nonNull test null: " + nonNull.test(null));
        System.out.println("predicate nonNull test true: " + nonNull.test(true));

        //   b) Functions (Functions accept one argument and produce a result. Default methods can be used to chain multiple functions together (compose, andThen))
        System.out.println("     b) Functions");

        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString =  toInteger.andThen(String::valueOf);
        System.out.println("function backToString: " + backToString.apply("123"));

        //   c) Suppliers (Suppliers produce a result of a given generic type. Unlike Functions, Suppliers don't accept arguments)
        System.out.println("     c) Suppliers");

        Supplier<Person> personSupplier = Person::new;
        Person newPerson = personSupplier.get();
        System.out.println("person firstname: " + newPerson.firstName);
        System.out.println("person lastname: " + newPerson.lastName);

        newPerson = personSupplier.get();
        System.out.println("person firstname: " + newPerson.firstName);
        System.out.println("person lastname: " + newPerson.lastName);

        //   d) Consumers (Consumers represents operations to be performed on a single input argument)
        System.out.println("     d) Consumers");
        Consumer<Person> greeter = p -> System.out.println("Hello " + p.firstName + " " + p.lastName);
        greeter.accept(new Person("Peter", "Muster"));

        //   e) Comparators (Comparators are well known from older versions of Java. Java 8 adds various default methods to the interface)
        System.out.println("     e) Comparators");
        Comparator<Person> firstNameComparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);
        Comparator<Person> lastNameComparator = (p1, p2) -> p1.lastName.compareTo(p2.lastName);

        Person alice = new Person("Alice", "Muster");
        Person peter = new Person("Peter", "Muster");
        System.out.println("Alice compare to Peter: " + firstNameComparator.compare(alice, peter));
        System.out.println("Muster compare to Muster: " + lastNameComparator.compare(alice, peter));

        //   f) Optionals (Optionals are not functional interfaces, instead it's a nifty utility to prevent NullPointerException)
        // Optional is a simple container for a value which may be null or non-null. Think of a method which may return a non-null result but sometimes return nothing. Instead of returning null you return an Optional in Java 8
        System.out.println("     f) Optionals");

        Optional<String> optional = Optional.of("Test");
        System.out.println("Optional 'Test' - is present: " + optional.isPresent());
        System.out.println("Optional 'Test' - get: " + optional.get());
        System.out.println("Optional 'Test' - or else: " + optional.orElse("fallbackstring"));

        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "T"
        optional.ifPresent((s) -> System.out.println(s.charAt(3)));     // "t"

        // 7. Streams (A java.util.Stream represents a sequence of elements on which one or more operations can be performed)
        System.out.println("+-- 7. Streams");

        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        //   a) Filter (Filter accepts a predicate to filter all elements of the stream)
        System.out.println("     a) Filter");
        stringCollection.stream().filter(s -> s.startsWith("a")).forEach(System.out::println);

        //   b) Sorted (Sorted is an intermediate operation which returns a sorted view of the stream)
        System.out.println("     b) Sorted");
        stringCollection.stream().sorted().filter(s->s.startsWith("a")).forEach(System.out::println);

        //   c) Map (The intermediate operation map converts each element into another object via the given function)
        System.out.println("     c) Map");
        stringCollection.stream().map(String::toUpperCase).sorted((a,b)->a.compareTo(b)).forEach(System.out::println);

        //   d) Match (Various matching operations can be used to check whether a certain predicate matches the stream)
        System.out.println("     d) Match");
        boolean anyStartsWithA = stringCollection.stream().anyMatch(s->s.startsWith("a"));
        System.out.println("startsWithA: " + anyStartsWithA);      // true

        //   e) Count (Count is a terminal operation returning the number of elements in the stream as a long)
        System.out.println("     e) Count");
        long startsWithB = stringCollection.stream().filter(s->s.startsWith("b")).count();
        System.out.println("startsWithB: " + startsWithB);      // 3

        //   f) Reduce (This terminal operation performs a reduction on the elements of the stream with the given function)
        System.out.println("     f) Reduce");
        Optional<String> reduced = stringCollection.stream().sorted().reduce((s1, s2) -> s1 + "#" + s2);
        reduced.ifPresent(System.out::println);

        // 8. Parallel Streams (Operations on parallel streams are performed concurrent on multiple threads)
        System.out.println("+-- 8. Parallel Streams");

        // First we create a large list of unique elements
        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        //   a) Sequential Sort
        System.out.println("     a) Sequential Sort");
        long t0 = System.nanoTime();

        long count = values.stream().sorted().count();
        System.out.println("count: "  + count);

        long t1 = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("sequential sort took: %d ms", millis)); // sequential sort took: 899 ms

        //   b) Parallel Sort
        System.out.println("     b) Parallel Sort");

        t0 = System.nanoTime();

        count = values.parallelStream().sorted().count();
        System.out.println(count);

        t1 = System.nanoTime();

        millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("parallel sort took: %d ms", millis)); // parallel sort took: 472 ms

        // 9. Map
        System.out.println("+-- 9. Map");
        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }

        map.forEach((id, val) -> System.out.println(val));

        // compute code on the map by utilizing functions
        map.computeIfPresent(3, (numb, val) -> val + numb);
        System.out.println("map.get(3): " + map.get(3));      // val33

        // 10. Date API
        System.out.println("+-- 10. Date API");

        //   a) Clock (Clock provides access to the current date and time)
        System.out.println("     a) Clock");

        Clock clock = Clock.systemDefaultZone();
        long milliSeconds = clock.millis();
        System.out.println("milliSeconds: " + milliSeconds);

        // Instants can be used to create legacy java.util.Date objects
        Instant instant = clock.instant();
        Date legacyDate = Date.from(instant);
        System.out.println("legacyDate: " + legacyDate);

        //   b) Timezones (Timezones define the offsets which are important to convert between instants and local dates and times)
        System.out.println("     b) Timezones");

        // print all available zimezone ids
        System.out.println(ZoneId.getAvailableZoneIds());

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");
        System.out.println("zone1 Rules: " + zone1.getRules());
        System.out.println("zone2 Rules: " + zone2.getRules());

        //   c) LocalTime (LocalTime represents a time without a timezone, e.g. 10pm or 17:30:15)
        System.out.println("     c) LocalTime");

        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone2);
        System.out.println("now1.isBefore(now2): " + now1.isBefore(now2));  // false

        long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
        long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

        System.out.println("hoursBetween: " + hoursBetween);       // -3
        System.out.println("minutesBetween: " + minutesBetween);     // -239

        // LocalTime comes with various factory method to simplify the creation of new instances, including parsing of time strings.
        LocalTime late = LocalTime.of(23, 59, 59);
        System.out.println("late: " + late);       // 23:59:59

        DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMAN);

        LocalTime leetTime = LocalTime.parse("06:13", germanFormatter);
        System.out.println("leetTime: " + leetTime);   // 06:13

        //   d) LocalDate (LocalDate represents a distinct date, e.g. 2014-03-11)
        System.out.println("     d) LocalDate");

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        LocalDate yesterday = tomorrow.minusDays(2);

        LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
        DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
        System.out.println("dayOfWeek: " + dayOfWeek);    // FRIDAY

        germanFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);

        LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
        System.out.println(xmas);   // 2014-12-24

        //   e) LocalDateTime (LocalDateTime represents a date-time)
        System.out.println("     e) LocalDateTime");
        LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);
        dayOfWeek = sylvester.getDayOfWeek();
        System.out.println("dayOfWeek: " + dayOfWeek);      // WEDNESDAY

        Month month = sylvester.getMonth();
        System.out.println("month: " + month);          // DECEMBER

        long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
        System.out.println("minuteOfDay: " + minuteOfDay);    // 1439

        // Formatting
        instant = sylvester.atZone(ZoneId.systemDefault()).toInstant();

        legacyDate = Date.from(instant);
        System.out.println("legacyDate: " + legacyDate);     // Wed Dec 31 23:59:59 CET 2014

        // 11. Annotations
        System.out.println("+-- 11. Annotations");
        // nothing here

        // 12. Hidden Features
        System.out.println("+-- 12. Hidden Features");
        // how many cores has my pc|notebook|tablet
        System.out.println("Number of cores on my system: " + Util.numberOfCores() + " cores!!!");

        // print a list comma separated in uppercase
        List<String> nameList = Arrays.asList("Tom", "Jerry", "Jane", "Jack");
        System.out.println(nameList.stream().map(String::toUpperCase).collect(Collectors.joining(", ")));

        // compute sum of double
        double[] numbers = {1, 24, 45, 62, 85, 8, 91, 3, 5, 56, 9};
        double total = DoubleStream.of(numbers).sum();
        System.out.println("total is: " + total);

        // or another way
        total = Arrays.stream(numbers).sum();
        System.out.println("total is: " + total);
    }
}
