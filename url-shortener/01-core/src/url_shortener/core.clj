(ns url-shortener.core
  (:require [clojure.string :as string]))

;; Consts
(def ^:const alphabet-size 62)

(def ^:const alphabet
  "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")

;; Logic
(defn- get-idx [i]
  (Math/floor (/ i alphabet-size)))

(defn- get-character-by-idx [i]
  (get alphabet (rem i alphabet-size)))

(defn int->id [id]
  (if (< id alphabet-size)
    (str (get-character-by-idx id))
    (let [codes (->> (iterate get-idx id)
                     (take-while pos?)
                     (map get-character-by-idx))]
      (string/join (reverse codes)))))

(comment
  (int->id 0)               ; => "0"
  (int->id alphabet-size)   ; => "10"
  (int->id 9999999999999)   ; => "2q3Rktod"
  (int->id Long/MAX_VALUE)) ; => "AzL8n0Y58W7"


(defn id->int [id]
  (reduce (fn [id ch]
            (+ (* id alphabet-size)
               (string/index-of alphabet ch)))
          0
          id))

(comment
  (id->int "0")        ; => 0
  (id->int "z")        ; => 61
  (id->int "clj")      ; => 149031
  (id->int "Clojure")) ; => 725410830262

