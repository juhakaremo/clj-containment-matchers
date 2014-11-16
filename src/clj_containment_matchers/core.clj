(ns clj-containment-matchers.core
  "Matchers for clojure collections. This library is created especially to ease the pain of matching nested data structures.

  (contains-exactly? [{:name \"John\" :id anything :children [{:name \"Paul\" :id anything}]}]
                     [{:name \"John\" :id 1 :children [{:name \"Paul\" :id 2}]}]) ;returns true

  "
  (:require [clj-containment-matchers.diff :refer [diff]]
            [clojure.pprint :refer [pprint]]))

(defn anything [_] true)

(defn contains-exactly?
  "Checks if expected and actual have exactly same.
  Returns boolean."
  ([actual expected]
    (let [[things-only-in-expected things-only-in-actual things-in-both] (diff actual expected)]
      (when things-only-in-expected
        (do
          (println "")
          (println "- - - Missing:")
          (pprint things-only-in-expected)))
      (when things-only-in-actual
        (do
          (println "")
          (println "- - - Unexpected content:")
          (pprint things-only-in-actual)))
      (when (or things-only-in-expected things-only-in-actual)
        (println "")
        (println "- - - Expected:")
        (println (pr-str expected))
        (println "")
        (println "- - - Actual:")
        (println (pr-str actual)))
      (and (empty? things-only-in-expected) (empty? things-only-in-actual)))))
