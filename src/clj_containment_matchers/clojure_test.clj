(ns clj-containment-matchers.clojure-test
  (:require [clj-containment-matchers.diff :refer [diff]]
            [clojure.pprint :refer [pprint]]
            [clojure.test :refer [do-report]]))

(defn anything [_] true)

(defn pretty [m]
  (with-out-str (pprint m)))

(defmethod clojure.test/report :fail-without-formatting [m]
  (clojure.test/with-test-out
    (clojure.test/inc-report-counter :fail)
    (println "\nFAIL in" (clojure.test/testing-vars-str m))
    (when (seq clojure.test/*testing-contexts*) (println (clojure.test/testing-contexts-str)))
    (when-let [message (:message m)] (println message))
    (println "expected:" (:expected m))
    (println "actual:" (:actual m))))

;This needs to be macro in order to have the correct line numbers in failure report
(defmacro contains-exactly?
  "Asserts that expected and actual have exactly same."
  [actual expected]
 `(let [[things-only-in-actual# things-only-in-expected# things-in-both#] (diff ~actual ~expected)
        missing-expected-error-msg# (when things-only-in-expected#
                                      (str "-- missing:" (clj-containment-matchers.clojure-test/pretty things-only-in-expected#)))
        unexpected-error-msg# (when things-only-in-actual#
                               (str "++ unexpected:" (clj-containment-matchers.clojure-test/pretty things-only-in-actual#)))
        error-msg# (str "\n" missing-expected-error-msg# unexpected-error-msg#)
        success?# (and (empty? things-only-in-expected#) (empty? things-only-in-actual#))
        [file# line#] (let [stacktrace# (.getStackTrace (RuntimeException.))
                            ^StackTraceElement s# (first stacktrace#)]
                        [(.getFileName s#)  (.getLineNumber s#)])]
    (do-report {:type (if success?# :pass :fail-without-formatting)
                :message error-msg#
                :expected (pretty '~expected)
                :actual (pretty ~actual)
                :file file#
                :line line#})))
