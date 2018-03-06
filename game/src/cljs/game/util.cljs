(ns game.util)

(defn sleep [ms f]
  (js/setTimeout f ms))
