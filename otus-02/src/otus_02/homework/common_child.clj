(ns otus-02.homework.common-child
  (:require [clojure.set :refer [intersection]]))

(defn common-chars
  [f s]
  (let [first (set f) second (set s)] (intersection first second)))

(defn all-subseq
  [s]
  (set (for [start (range (count s))
             end (range (inc start) (inc (count s)))]
         (take (- end start) (drop start s)))))

(defn common-child-length
  [f s]
  (let [common-set (common-chars f s)
        filtered-f (-> common-set
                       (filter f)
                       all-subseq)
        filtered-s (-> common-set
                       (filter s)
                       all-subseq)]
    ((fnil #(apply max %) [0])
      (seq (map count (intersection filtered-f filtered-s))))))


(comment
  (doseq [test [(= (common-child-length "SHINCHAN" "NOHARAAA") 3)
                (= (common-child-length "HARRY" "SALLY") 2)
                (= (common-child-length "AA" "BB") 0)
                (= (common-child-length "ABCDEF" "FBDAMN") 2)]]
    (prn test)))
