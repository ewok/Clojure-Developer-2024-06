(ns otus-10.frames
  (:require [otus-10.utils :as u]))

(defn valid-frame?
  "Checks first 4 symbols if they are fit frame ID pattern."
  [id]
  (and (string? id)
       (= 4 (count id))
       (some? (re-matches #"[A-Z]{3}[A-Z0-9]" id))))

(comment
  (valid-frame? "asdf")
  (valid-frame? "TALB")
  (valid-frame? "1ASD")
  (valid-frame? "1AS")
  (valid-frame? "RVA2"))

(defmulti decode-frame :id)

; "TALB — Album"
(defmethod decode-frame :TALB
  [data]
  (assoc data :body (apply str (:body data))))

; "TPE1 — Artist"
(defmethod decode-frame :TPE1
  [data]
  (assoc data :body (apply str (:body data))))

; ; "TIT2 — Title"
(defmethod decode-frame :TIT2
  [data]
  (assoc data :body (apply str (:body data))))

; "TYER — Year"
(defmethod decode-frame :TYER
  [data]
  (assoc data :body (Integer/parseInt (apply str (:body data)))))

; "TCON — Genre"
(defmethod decode-frame :TCON
  [data]
  (assoc data :body (apply str (:body data))))

(defmethod decode-frame :APIC [data] (assoc data :body "<Picture here>"))

;; Other
(defmethod decode-frame :TRCK
  [data]
  (assoc data :body (Integer/parseInt (apply str (:body data)))))

(defmethod decode-frame :default [data] data)

(defn frame-reader
  "Returns map with frames.
  :id :size :enc-byte :body

  Uses this pattern to gather data:
   Frame ID      $xx xx xx xx  (four characters)
   Size      4 * %0xxxxxxx
   Flags         $xx xx "
  ([data]
   (if (and (not-empty data) (valid-frame? (apply str (take 4 data))))
     (let [id (take 4 data)
           data-size (u/bytes->num (map int (take 4 (drop 4 data))))
           all-body (take data-size (drop 10 data))
           enc-byte (map int (take 1 all-body))
           body (drop 1 all-body)
           header-size (+ 10 data-size)]
       (lazy-seq (cons (decode-frame (zipmap [:id :size :enc-byte :body]
                                             [(keyword (apply str id))
                                              header-size enc-byte body]))
                       (frame-reader (drop header-size data)))))
     [])))
