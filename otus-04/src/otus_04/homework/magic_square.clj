(ns otus-04.homework.magic-square
  (:require [clojure.test :refer [is]]
            [clojure.set :as set]))

;; Оригинальная задача:
;; https://www.codewars.com/kata/570b69d96731d4cf9c001597
;;
;; Подсказка: используйте "Siamese method"
;; https://en.wikipedia.org/wiki/Siamese_method

(defn gen-route
  "Returns lazy seq of route for Siamese method.
  n - square size. Can be only odd.
  Creates set to check if coord is already taken."
  ([n] (let [x (int (/ n 2))] (gen-route n nil nil x 0 #{})))
  ([n p-x p-y x y step-set]
   (let [i-gen-route (partial gen-route n x y)
         i-n (dec n)
         i-step-set (conj step-set [x y])]
     (if (set/subset? #{[x y]} step-set)
       (cond (>= p-y i-n) (i-gen-route p-x 0 step-set)
             :else (i-gen-route p-x (inc p-y) step-set))
       (lazy-seq (cons [x y]
                       (cond (>= (count step-set) (dec (* n n))) []
                             (and (< x i-n) (> y 0))
                               (i-gen-route (inc x) (dec y) i-step-set)
                             (and (= x i-n) (= y 0))
                               (i-gen-route 0 i-n i-step-set)
                             (= x i-n) (i-gen-route 0 (dec y) i-step-set)
                             (= y 0) (i-gen-route (inc x) i-n i-step-set))))))))

(defn magic-square
  "Функция возвращает вектор векторов целых чисел,
  описывающий магический квадрат размера n*n,
  где n - нечётное натуральное число.

  Магический квадрат должен быть заполнен так, что суммы всех вертикалей,
  горизонталей и диагоналей длиной в n должны быть одинаковы."
  [n]
  (let [matrix (make-array Integer/TYPE n n)]
    (doseq [[[x y] i] (map list (gen-route n) (range 1 (inc (* n n))))]
      (aset matrix (int y) (int x) (int i)))
    (mapv vec matrix)))



(comment
  ; 17 24  1  8 15
  ; 23  5  7 14 16
  ;  4  6 13 20 22
  ; 10 12 19 21  3
  ; 11 18 25  2  9
  (is (= (magic-square 5)
         [[17 24 1 8 15] [23 5 7 14 16] [4 6 13 20 22] [10 12 19 21 3]
          [11 18 25 2 9]])))
