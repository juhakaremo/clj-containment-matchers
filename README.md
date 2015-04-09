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
(deftest my-test-with-matcher (is (contains-exactly? {:first-name "John" :last-name "Smith"} {:first-name "Johnny" :last-name "Smith"})))
```

you get following output:

```
- - - Missing:
{:first-name "John"}

- - - Unexpected content:
{:first-name "Johnny"}

- - - Expected:
{:last-name "Smith", :first-name "Johnny"}

- - - Actual:
{:last-name "Smith", :first-name "John"}

FAIL in (my-test-with-matcher) (form-init7375693162901327698.clj:1)
expected: (clj-containment-matchers.core/contains-exactly? {:first-name "John", :last-name "Smith"} {:first-name "Johnny", :last-name "Smith"})
  actual: (not (clj-containment-matchers.core/contains-exactly? {:last-name "Smith", :first-name "John"} {:last-name "Smith", :first-name "Johnny"}))
```

From the output it is much easier to check what field was missing or what field had incorrect value.

# Installation

Add the following to your `project.clj` `:dependencies`:

```clojure
[clj-containment-matchers "0.9.3"]
```

# Usage

```clojure
(require [clj-containment-matchers.core :refer :all])
```
Check whether two items contain exactly the same values

```clojure
(contains-exactly? actual expected)
```

Ignore some field from the match (e.g. timestamp)

```clojure
(contains-exactly? {:name "John" :timestamp 219898989} {:name "John" :timestamp anything})
```

#License

The use and distribution terms for this software are covered by Eclipse LicensedPublic License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
