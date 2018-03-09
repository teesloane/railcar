(ns game.db
  (:require [game.rooms.intro :refer [intro]]))


(def default-db
  {:current-room intro
   :current-command ""
   :current-step nil ;; set in ::initialize-db
   :current-text (-> intro :steps :missed-train :text) ;; ui only!
   :history []
   :prompt ""
   ;; -- rooms -- to be organized later.
   :intro intro})

