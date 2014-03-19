(ns clj-containment-matchers.internal)

(defn- -get-anything-matchers [value-to-ignore result current-path current-node]
  (cond
    (map? current-node)
    (reduce
      (fn [res node] (-get-anything-matchers value-to-ignore res (conj current-path (key node)) (val node)))
      result
      current-node)

    (sequential? current-node)
    (let [extract-anything-path (fn [node index]
                                  (-get-anything-matchers value-to-ignore result (conj current-path index) node))]
                      (->> (mapcat extract-anything-path current-node (range (count current-node)))
                           (concat result)
                           (remove empty?)))

    (= value-to-ignore current-node) (conj result current-path)

    :else result))

(defn remove-path [node path-to-remove]
  (let [path (first path-to-remove)]
    (cond
      (= 1 (count path-to-remove))
      (if (number? path)
        (concat
          (take path node)
          (drop (inc path) node))
        (dissoc node path))

      (map? node) (let [subnodes-to-keep (dissoc node path)
                        subnode-to-check-for-removal (path node)
                        modified (remove-path subnode-to-check-for-removal (rest path-to-remove))]
                    (assoc subnodes-to-keep path modified))

      (sequential? node)
      (map (fn [value index] (if (= path index)
                               (remove-path value (rest path-to-remove))
                               value))
        node (range (count node)))

      :else node)))

(defn get-anything-matchers [value-to-ignore m] (-get-anything-matchers value-to-ignore [] [] m))

(defn remove-paths [coll paths-to-remove]
  (if (coll? coll) (reduce remove-path coll paths-to-remove)
    (throw (IllegalArgumentException. "remove-paths accepts only collections."))))