Matchers for clojure collections. This library is created to ease the pain of matching nested data structures.

# Beef (=why)

A test like this written with clojure.test

```clojure
(deftest my-test (is (= {:first-name "John" :last-name "Smith"} {:first-name "Johnny" :last-name "Smith"})))
```

will produce following output:

```
FAIL in (my-test) (form-init7375693162901327698.clj:1)
expected: (= {:first-name "John", :last-name "Smith"} {:first-name "Johnny", :last-name "Smith"})
  actual: (not (= {:last-name "Smith", :first-name "John"} {:last-name "Smith", :first-name "Johnny"}))
```

With complex and possibly nested data structures it is hard to read what was actually wrong. With matcher like this

```clojure
(deftest my-test-with-matcher (is (equal? {:first-name "John" :last-name "Smith"} {:first-name "Johnny" :last-name "Smith"})))
```

you get following output:

```
FAIL in (my-test-with-matcher) (form-init7128815568440584833.clj:1)

-- missing:
{:first-name "Johnny"}
++ unexpected:
{:first-name "John"}
expected: {:first-name "Johnny", :last-name "Smith"}
  actual: {:last-name "Smith", :first-name "John"}
```

From the output it is easier to check what field was missing or what field had incorrect value.

# Installation

Add the following to your `project.clj` `:dependencies`:

```clojure
[clj-containment-matchers "1.0.1"]
```

# Usage

```clojure
(require [clj-containment-matchers.clojure-test :refer :all])
```
Check whether two items contain exactly the same values

```clojure
(is (equal? actual expected)

(is (not-equal? actual expected)
```

Ignore some field from the match (e.g. timestamp)

```clojure
(is (equal? {:name "John" :timestamp 219898989} {:name "John" :timestamp anything})
```

anything is a function that clj-containment-matchers provide but you can use any Clojure function like this:

```clojure
(is (equal? {:name "John" :age 21} {:name "John" :age number?})
```

#License

The use and distribution terms for this software are covered by Eclipse LicensedPublic License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
