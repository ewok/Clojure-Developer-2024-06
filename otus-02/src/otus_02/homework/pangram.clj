(ns otus-02.homework.pangram
  (:require [clojure.string :as string]
            [clojure.set :refer [difference]]))

(def ^:private alpha (set "abcdefghijklmnopqrstuvwxyz"))

(defn is-pangram
  [test-string]
  (->> test-string
       (string/lower-case)
       (set)
       (difference alpha)
       (empty?)))
