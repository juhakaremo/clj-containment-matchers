(ns clj-containment-matchers.diff
  (:require [clojure.set :as set]))

(declare diff)

(defn- value-mismatch [x y] [x y nil])

(defn- value-match [x y] [nil nil x])

(defn- diff-map [x y]
  (let [diff-by-key (fn [k]
                      (let [[x* y* xy] (diff (get x k) (get y k))]
                        [(when x* {k x*})
                         (when y* {k y*})
                         (when xy {k xy})]))
        ks (distinct (concat (keys x) (keys y)))
        diffs-by-keys (map diff-by-key ks)]
    (reduce
      (fn [res cur] (map merge res cur))
      [nil nil nil]
      diffs-by-keys)))

(defn- as-same-size [x y]
  (let [size (max (count x) (count y))
        add-to (fn [m] (map (constantly nil) (range (- size (count m)))))]
    [(concat x (add-to x)) (concat y (add-to y))]))

(defn- drop-ending-nils [x]
  (let [x-without-ending-nils (->> (reverse x)
                                   (drop-while nil?)
                                   (reverse))]
    (when-not (empty? x-without-ending-nils)
      x-without-ending-nils)))

(defn- diff-sequential [x y]
  (let [[padded-x padded-y] (as-same-size x y)]
    (->> (mapv diff padded-x padded-y)
         (apply map vector)
         (mapv drop-ending-nils))))

(defn- diff-set [x y]
  (let [xval (into #{} x)
        yval (into #{} y)]
    [(not-empty (set/difference xval yval))
     (not-empty (set/difference yval xval))
     (not-empty (set/intersection xval yval))]))

(defn- diff-matcher [x matcher-fn]
  (if (matcher-fn x)
    (value-match x matcher-fn)
    (value-mismatch x matcher-fn)))

(defn- value-diff [a b]
  (if (= a b)
    (value-match a b)
    (value-mismatch a b)))

(defn- collection-type [x]
  (when x
    (cond
      (instance? java.util.Set x) :set
      (instance? java.util.List x) :sequential
      (instance? java.util.Map x) :map
      (instance? clojure.lang.IFn x) :function
      :else :value)))

(defn diff [actual expected]
  (let [actual-type (collection-type actual)
        expected-type (collection-type expected)
        diff-fn (cond
                  (= actual expected) value-match
                  (and (nil? actual-type) (nil? expected-type)) value-match
                  (or (nil? actual-type) (nil? expected-type)) value-mismatch
                  (= :function expected-type) diff-matcher
                  (not= expected-type actual-type) value-mismatch
                  (= :set actual-type) diff-set
                  (= :map actual-type) diff-map
                  (= :sequential actual-type) diff-sequential
                  :else value-diff)]
    (diff-fn actual expected)))