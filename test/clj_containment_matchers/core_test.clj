(ns clj-containment-matchers.core-test
  (:require [clojure.test :refer :all]
            [clj-containment-matchers.diff :refer [diff]]
            [clj-containment-matchers.clojure-test :refer [anything contains-exactly?]]))

(defmacro does-not-contain-exactly? [actual expected]
  `(let [[things-only-in-actual# things-only-in-expected# things-in-both#] (diff ~actual ~expected)]
     (is (or things-only-in-actual# things-only-in-expected#))))

(deftest contains-exactly?-function
  (testing "compares arrays"
    (contains-exactly? [1 2 3] [1 2 anything])
    (contains-exactly? ["1" "2" "3"] ["1" string? "3"])
    (contains-exactly? [1 2 3] [number? 2 3])
    (does-not-contain-exactly? [1 2 3] [1 3 3])
    (does-not-contain-exactly? [1 2 3] [1 1 3])
    (does-not-contain-exactly? [1 2 3] [2 2 3]))
  (testing "compares nested arrays"
    (contains-exactly? [1 [2 [3]]] [1 [2 [3]]])
    (contains-exactly? [1 [2 [3]]] [1 [2 [number?]]])
    (contains-exactly? ["1" ["2" ["3"]]] ["1" [string? ["3"]]])
    (does-not-contain-exactly? [1 [2 [3]]] [1 [3 [3]]])
    (does-not-contain-exactly? [1 [2 [3]]] [1 [2 [string?]]])
    (does-not-contain-exactly? ["1" ["2" ["3"]]] ["1" [number? ["3"]]]))
  (testing "compares maps"
     (contains-exactly? {:name "john" :id "1"} {:name "john" :id anything})
     (does-not-contain-exactly? {:name "john" :id 1} {:name "john" :id string?}))
  (testing "compares nested maps"
    (contains-exactly? {:name "john" :phone-numbers {"home" "123" "work" "456"}}
                       {:name "john" :phone-numbers {"home" "123" "work" string?}})
    (does-not-contain-exactly? {:name "john" :phone-numbers {"home" "123" "work" "456"}}
                               {:name "john" :phone-numbers {"home" "123" "work-2" "456"}}))
  (testing "compares sets"
    (contains-exactly? #{1 2 3} #{2 1 3})
    (does-not-contain-exactly?  #{1 2 3} #{1 2 4}))
  (testing "compares nested structures with different types of collections"
    (contains-exactly?
      [{:type "Test"
        :node {:data {:title "My test node"}
               :id 1}}]
      [{:type "Test"
        :node {:data {:title string?}
               :id (fn [x] (and (> x 0) (< x 10)))}}])
    (contains-exactly? {:id 1 :link []} {:id anything :link []})))
