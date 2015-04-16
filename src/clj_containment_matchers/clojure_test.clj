(ns clj-containment-matchers.clojure-test
  (:require [clj-containment-matchers.diff :refer [diff]]
            [clojure.pprint :refer [pprint]]
            [clojure.test :refer [do-report assert-expr]]))

(defn anything [_] true)

(defmethod assert-expr 'equal? [_ form]
  "Asserts that expected and actual are exactly same. Prints a diff when not."
  (let [actual (nth form 1)
        expected (nth form 2)]
 `(let [[things-only-in-actual# things-only-in-expected# things-in-both#] (diff ~actual ~expected)
        missing-expected-error-msg# (when things-only-in-expected#
                                      (str "-- missing:\n" things-only-in-expected#))
        unexpected-error-msg# (when things-only-in-actual#
                               (str "\n++ unexpected:\n" things-only-in-actual#))
        error-msg# (str "\n" missing-expected-error-msg# unexpected-error-msg#)
        success?# (and (empty? things-only-in-expected#) (empty? things-only-in-actual#))]
    (do-report {:type (if success?# :pass :fail)
                :message error-msg#
                :expected '~expected
                :actual ~actual}))))

(defmethod assert-expr 'not-equal? [_ form]
  "Asserts that expected and actual are not the same."
  (let [actual (nth form 1)
        expected (nth form 2)]
 `(let [[things-only-in-actual# things-only-in-expected# things-in-both#] (diff ~actual ~expected)
        success?# (not (and (empty? things-only-in-expected#)
                            (empty? things-only-in-actual#)))]
    (do-report {:type (if success?# :pass :fail)
                :message "Should not be equal"
                :expected '~expected
                :actual ~actual}))))
