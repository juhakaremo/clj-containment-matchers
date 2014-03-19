(ns clj-containment-matchers.core
  "Matchers for clojure collections. This library is created especially to ease the pain of matching nested data structures.

  (contains-exactly? [{:name \"John\" :id anything :children [{:name \"Paul\" :id anything}]}]
                     [{:name \"John\" :id 1 :children [{:name \"Paul\" :id 2}]}]) ;returns true

  "
  (:require [clojure.data :refer [diff]]
           [clj-containment-matchers.internal :refer [get-anything-matchers remove-paths]]))

(def anything "clj-containment-matchers.core/anything-matcher")

(defn diff+
  "like clojure.data/diff but allows you to ignore values"
  ([expected-raw actual-raw] (diff+ expected-raw actual-raw anything))
  ([expected-raw actual-raw keyword-value-to-ignore]
    (if (and (coll? expected-raw) (coll? actual-raw))
      (let [ignored-paths (get-anything-matchers keyword-value-to-ignore expected-raw)
            actual (remove-paths actual-raw ignored-paths)
            expected (remove-paths expected-raw ignored-paths)
            [things-only-in-expected things-only-in-actual things-in-both] (diff expected actual)]
        [things-only-in-expected things-only-in-actual things-in-both])
      (throw (IllegalArgumentException. "diff+ accepts only collections. Consider using clojure.data/diff instead of diff+.")))))

(defn contains-exactly?
  "Checks if expected and actual have exactly same.
  Returns boolean."
  ([expected actual] (contains-exactly? expected actual anything))
  ([expected actual keyword-value-to-ignore]
    (let [[things-only-in-expected things-only-in-actual things-in-both] (diff+ expected actual keyword-value-to-ignore)]
      (if things-only-in-expected (println "\r- - - Missing:" things-only-in-expected))
      (if things-only-in-actual (println "\r- - - Unexpected content:" things-only-in-actual))
      (and (empty? things-only-in-expected) (empty? things-only-in-actual)))))

;todo
;write readme
;replace anything with matcher function
;contains
;containsExactlyInAnyOrder
;containsInAnyOrder
