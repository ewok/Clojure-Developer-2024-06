(ns otus-10.homework
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :as io]
            [otus-10.id3 :as id3])
  (:gen-class))

(def cli-options
  [["-f" "--filename FILE" "Filename" :validate [#(.exists (io/as-file %))]]
   ["-a" "--show-album"] ["-A" "--show-artist"] ["-t" "--show-title"]
   ["-y" "--show-year"] ["-g" "--show-genre"] ["-h" "--help"]])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{{:keys [filename show-album]} :options, errors :errors}
          (parse-opts args cli-options)]
    (if errors
      (apply println errors)
      (do (println show-album) (println (id3/get-id3-header filename))))))
