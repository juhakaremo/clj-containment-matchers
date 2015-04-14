(defproject clj-containment-matchers "1.0.0"
  :description "Matchers for clojure collections. This library is created to ease the pain of matching nested data structures in production code and tests."
  :url "https://github.com/juhakaremo/clj-containment-matchers"
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :source-paths ["src"]
  :test-paths ["test"]
  :profiles {:dev {:dependencies [[org.clojure/tools.trace "0.7.8"]]}}
  :scm {:name "git"
        :url "https://github.com/juhakaremo/clj-containment-matchers"}
  :plugins [[lein-ancient "0.6.6"]]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"})
