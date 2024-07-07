(ns otus-02.homework.palindrome
  (:require [clojure.string :as string]))

(def ^:private alpha (set "abcdefghijklmnopqrstuvwxyz"))

(defn is-palindrome
  [test-string]
  (->> test-string
       (string/lower-case)
       (filter #(contains? alpha %1))
       (#(= %1 (reverse %1)))))

(defn is-palindrome-0
  "Using vector instead of seq."
  [test-string]
  (->> test-string
       (string/lower-case)
       (vec)
       (filterv #(contains? alpha %1))
       (#(= %1 (reverse %1)))))

(defn is-palindrome-1
  "Using rseq instead of reverse."
  [test-string]
  (->> test-string
       (string/lower-case)
       (vec)
       (filterv #(contains? alpha %1))
       (#(= %1 (rseq %1)))))

(defn is-palindrome-2
  "More efficient. Using filter based on ascii code. 3.5X to speed from the first implementation."
  [test-string]
  (->> test-string
       (string/lower-case)
       (vec)
       (filterv #(let [i (int %1)] (and (> i 96) (< i 123))))
       (#(= %1 (rseq %1)))))

(comment
  (do (time (dotimes [_ 10000]
              (is-palindrome "civic")
              (is-palindrome "tattarrattat")
              (is-palindrome "taco cat")
              (is-palindrome "no lemon, no melon")
              (is-palindrome "Eva, can I see bees in a cave?")
              (is-palindrome "Was it a cat I saw?")
              (not (is-palindrome "civics"))
              (not (is-palindrome "They all have one thing"))
              (not (is-palindrome "knock on the door"))))
      (time (dotimes [_ 10000]
              (is-palindrome-0 "civic")
              (is-palindrome-0 "tattarrattat")
              (is-palindrome-0 "taco cat")
              (is-palindrome-0 "no lemon, no melon")
              (is-palindrome-0 "Eva, can I see bees in a cave?")
              (is-palindrome-0 "Was it a cat I saw?")
              (not (is-palindrome-0 "civics"))
              (not (is-palindrome-0 "They all have one thing"))
              (not (is-palindrome-0 "knock on the door"))))
      (time (dotimes [_ 10000]
              (is-palindrome-1 "civic")
              (is-palindrome-1 "tattarrattat")
              (is-palindrome-1 "taco cat")
              (is-palindrome-1 "no lemon, no melon")
              (is-palindrome-1 "Eva, can I see bees in a cave?")
              (is-palindrome-1 "Was it a cat I saw?")
              (not (is-palindrome-1 "civics"))
              (not (is-palindrome-1 "They all have one thing"))
              (not (is-palindrome-1 "knock on the door"))))
      (time (dotimes [_ 10000]
              (is-palindrome-2 "civic")
              (is-palindrome-2 "tattarrattat")
              (is-palindrome-2 "taco cat")
              (is-palindrome-2 "no lemon, no melon")
              (is-palindrome-2 "Eva, can I see bees in a cave?")
              (is-palindrome-2 "Was it a cat I saw?")
              (not (is-palindrome-2 "civics"))
              (not (is-palindrome-2 "They all have one thing"))
              (not (is-palindrome-2 "knock on the door"))))))

;; Difference
; (out) "Elapsed time: 234.478698 msecs"
; (out) "Elapsed time: 110.865521 msecs"
; (out) "Elapsed time: 91.522656 msecs"
; (out) "Elapsed time: 64.476771 msecs"
