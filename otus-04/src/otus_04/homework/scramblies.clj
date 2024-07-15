(ns otus-04.homework.scramblies
  (:require [clojure.test :refer [is]]
            [clojure.string :as string]))

;; Оригинальная задача:
;; https://www.codewars.com/kata/55c04b4cc56a697bb0000048


(defn scramble?
  "Функция возвращает true, если из букв в строке letters
  можно составить слово word."
  [^String letters ^String word]
  (letfn [(letters-freq [^String s] (reduce #(update %1 %2 (fnil inc 0)) {} s))
          (letters-init [^String s]
            (reduce #(update %1 %2 (constantly 0)) {} s))]
    (not-any? neg?
              (map val
                (merge-with -
                            (merge-with +
                                        (letters-init (string/join letters
                                                                   word))
                                        (letters-freq letters))
                            (letters-freq word))))))

(comment
  (is (scramble? "rkqodlw" "world")) ; true
  (is (not (scramble? "catacomb" "aaa"))) ; true
  (is (scramble? "cedewaraaossoqqyt" "codewars")) ; true
  (is (not (scramble? "katas" "steak")))) ; true
