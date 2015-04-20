(ns clj-containment-matchers.core-test
  (:require [clojure.test :refer :all]
            [clj-containment-matchers.diff :refer [diff]]
            [clj-containment-matchers.clojure-test :refer :all]))

(deftest contains-exactly?-function
  (testing "compares arrays"
    (is (equal? [1 2 3] [1 2 anything]))
    (is (equal? ["1" "2" "3"] ["1" string? "3"]))
    (is (equal? [1 2 3] [number? 2 3]))
    (is (not-equal? [1 2 3] [1 3 3]))
    (is (not-equal? [1 2 3] [1 1 3]))
    (is (not-equal? [1 2 3] [2 2 3])))
  (testing "compares nested arrays"
    (is (equal? [1 [2 [3]]] [1 [2 [3]]]))
    (is (equal? [1 [2 [3]]] [1 [2 [number?]]]))
    (is (equal? ["1" ["2" ["3"]]] ["1" [string? ["3"]]]))
    (is (not-equal? [1 [2 [3]]] [1 [3 [3]]]))
    (is (not-equal? [1 [2 [3]]] [1 [2 [string?]]]))
    (is (not-equal? ["1" ["2" ["3"]]] ["1" [number? ["3"]]])))
  (testing "compares maps"
     (is (equal? {:name "john" :id "1"} {:name "john" :id anything}))
     (is (not-equal? {:name "john" :id 1} {:name "john" :id string?})))
  (testing "compares nested maps"
    (is (equal? {:name "john" :phone-numbers {"home" "123" "work" "456"}}
                {:name "john" :phone-numbers {"home" "123" "work" string?}}))
    (is (not-equal? {:name "john" :phone-numbers {"home" "123" "work" "456"}}
                    {:name "john" :phone-numbers {"home" "123" "work-2" "456"}})))
  (testing "compares sets"
    (is (equal? #{1 2 3} #{2 1 3}))
    (is (not-equal? #{1 2 3} #{1 2 4})))
  (testing "compares nested structures with different types of collections"
    (is (equal?
      [{:type "Test"
        :node {:data {:title "My test node"}
               :id 1}}]
      [{:type "Test"
        :node {:data {:title string?}
               :id (fn [x] (and (> x 0) (< x 10)))}}]))
    (is (equal? {:id 1 :link []} {:id anything :link []}))))
