(ns clj-containment-matchers.core-test
  (:require [clojure.test :refer :all]
            [clj-containment-matchers.core :refer [anything]]
            [clj-containment-matchers.internal :refer [remove-path]]))

(def get-anything-matchers (partial clj-containment-matchers.internal/get-anything-matchers anything))

(deftest get-anything-matcher-function
  (testing "handles arrays"
    (is (= (get-anything-matchers [1 2 anything 4]) [[2]])))
  (testing "handles maps"
    (is (= (get-anything-matchers {:name "john" :id anything}) [[:id]]))
    (is (= (get-anything-matchers {:id anything :link []}) [[:id]])))

  (testing "handles array inside map"
    (is (= (get-anything-matchers {:name "john" :list [1 2 anything 4]}) [[:list 2]])))
  (testing "handles array inside of array"
    (is (= (get-anything-matchers [1 2 ["a" anything] 4]) [[2 1]])))
  (testing "handles map inside of array"
    (is (= (get-anything-matchers [1 2 {:name "john" :id anything} 4]) [[2 :id]])))
  (testing "handles map inside of map"
    (is (= (get-anything-matchers {:name "john" :data {:wife "mary" :id anything}}) [[:data :id]])))

  (testing "handles map inside of map inside of map"
    (is (= (get-anything-matchers {:name "john" :data {:wife {:name "mary" :id anything}}}) [[:data :wife :id]])))
  (testing "handles map inside of map inside of array"
    (is (= (get-anything-matchers [{:a 1} {:wife {:name "mary" :id anything}}]) [[1 :wife :id]])))
  (testing "handles map inside of array inside of array"
    (is (= (get-anything-matchers ["a"
                                   [4 :data {:id anything :b "c"} 3]
                                   "b"]))))

  (testing "handles array inside of array inside of array"
    (is (= (get-anything-matchers ["a"
                                   [4 [anything "b" "c"] 3]
                                   "b"])
          [[1 1 0]])))
  (testing "handles array inside of array inside of map"
    (is (= (get-anything-matchers {:name "john" :list [4 [anything "b" "c"] 3]}) [[:list 1 0]])))
  (testing "handles array inside of map inside of map"
    (is (= (get-anything-matchers {:name "john" :data {:wife {:name "mary" :list [anything "b" "c"]}}}) [[:data :wife :list 0]])))

  (testing "handles multiple matchers on arrays"
    (is (= (get-anything-matchers [anything 2 anything 4]) [[0] [2]])))
  (testing "handles multiple matchers on maps"
    (is (= (get-anything-matchers {:age anything :name "john" :id anything}) [[:age] [:id]])))

  (testing "handles multiple matchers on array inside map"
    (is (= (get-anything-matchers {:name "john" :list [anything 2 anything 4]}) [[:list 0] [:list 2]])))
  (testing "handles multiple matchers on map inside array")
  (is (= (get-anything-matchers [1 2 {:age anything :name "john" :id anything} 4]) [[2 :age] [2 :id]])))

(deftest remove-path-function
  (testing "removes array path"
    (is (= (remove-path [1 2 3] [1]) [1 3])))
  (testing "removes map path"
    (is (= (remove-path {:id 1 :name "john"} [:id]) {:name "john"})))
  (testing "removes array inside map path"
    (is (= (remove-path {:name "john" :list [1 2 anything 4]} [:list 2]) {:name "john" :list [1 2 4]})))
  (testing "removes array inside of array path"
    (is (= (remove-path [1 2 ["a" anything] 4] [2 1]) [1 2 ["a"] 4])))
  (testing "removes map inside of array path"
    (is (= (remove-path [1 2 {:name "john" :id anything} 4] [2 :id]) [1 2 {:name "john"} 4])))
  (testing "removes map inside of map path"
    (is (= (remove-path {:name "john" :data {:wife "mary" :id anything}} [:data :id]) {:name "john" :data {:wife "mary"}}))))

(def contains-exactly? (fn [actual expected] (clj-containment-matchers.core/contains-exactly? actual expected anything)))

(deftest contains-exactly?-function
  (testing "handles maps"
    (is (= true (contains-exactly? {:name "john" :id "something random"} {:name "john" :id anything}))))
  (testing "handles arrays"
    (is (contains-exactly?
          [1 2 "unknown in advance" 4]
          [1 2 anything 4])))
  (is (not (contains-exactly?
             [1 2 "unknown in advance" 3]
             [1 2 anything 4])))
  (testing "node"
    (is (contains-exactly?
          [{:type "Test"
            :linked-from []
            :links-to []
            :node {:data {:title "Title"}
                   :id 1}}]
          [{:type "Test"
            :linked-from []
            :links-to []
            :node {:data {:title "Title"}
                   :id anything}}])
      (is (contains-exactly? {:id 1 :link []} {:id anything :link []} )))))
