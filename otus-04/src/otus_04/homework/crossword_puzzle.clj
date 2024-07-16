(ns otus-04.homework.crossword-puzzle
  (:require [clojure.string :as string]))

;; Оригинал:
;; https://www.hackerrank.com/challenges/crossword-puzzle/problem

(defn parse-input
  [input]
  (let [lines (string/split-lines input)
        grid-lines (butlast lines)
        words (last lines)
        words-list (string/split words #";")]
    [grid-lines words-list]))


(defn find-spaces
  [grid]
  (let [rows (map-indexed
               #(map-indexed (fn [j cell] (when (not= cell \+) [%1 j])) %2)
               grid)
        vertical (apply map list rows)]
    {:horizontal (->> rows
                      (apply concat)
                      (remove nil?)
                      (group-by first)),
     :vertical (->> vertical
                    (apply concat)
                    (remove nil?)
                    (group-by second))}))


(defn place-word
  [grid word coords]
  (reduce (fn [g [[i j] c]] (assoc-in g [i j] c))
    grid
    (map vector coords word)))

(defn fits?
  [grid word coords]
  (every? (fn [[idx [i j]]]
            (or (= (get-in grid [i j]) \-)
                (= (get-in grid [i j]) (get word idx))))
          (map-indexed list coords)))


(defn solve-grid
  [grid words]
  (if (empty? words)
    grid
    (let [word (first words)
          rest-words (rest words)
          {:keys [horizontal vertical]} (find-spaces grid)]
      (some identity
            (concat (for [[_ coords] horizontal
                          :when (= (count coords) (count word))
                          :when (fits? grid word coords)]
                      (solve-grid (place-word grid word coords) rest-words))
                    (for [[_ coords] vertical
                          :when (= (count coords) (count word))
                          :when (fits? grid word coords)]
                      (solve-grid (place-word grid word coords)
                                  rest-words)))))))

(defn solve
  "Возвращает решённый кроссворд. Аргумент является строкой вида

  +-++++++++
  +-++++++++
  +-++++++++
  +-----++++
  +-+++-++++
  +-+++-++++
  +++++-++++
  ++------++
  +++++-++++
  +++++-++++
  LONDON;DELHI;ICELAND;ANKARA

  Все строки вплоть до предпоследней описывают лист бумаги, а символами
  '-' отмечены клетки для вписывания букв. В последней строке перечислены
  слова, которые нужно 'вписать' в 'клетки'. Слова могут быть вписаны
  сверху-вниз или слева-направо."
  [input]
  (let [[grid words] (parse-input input)]
    (string/join "\n" (map string/join (solve-grid (mapv vec grid) words)))))


(comment
  (solve
    "+-++++++++
+-++++++++
+-++++++++
+-----++++
+-+++-++++
+-+++-++++
+++++-++++
++------++
+++++-++++
+++++-++++
LONDON;DELHI;ICELAND;ANKARA")
  (solve
    "++++++-+++
++------++
++++++-+++
++++++-+++
+++------+
++++++-+-+
++++++-+-+
++++++++-+
++++++++-+
++++++++-+
ICELAND;MEXICO;PANAMA;ALMATY"))
