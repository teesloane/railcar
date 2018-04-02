(ns game.db
  (:require [game.rooms.intro :refer [intro]]))


(def default-db
  {:started? false
   :current-room intro
   :current-command ""
   :current-step nil ;; set in ::initialize-db
   :current-text (-> intro :steps :missed-train :text) ;; ui only!
   :history []
   :prompt ""
   ;; -- rooms -- to be organized later.
   :intro intro
   :audio {:one-shot nil
           :loop-a nil
           :loop-b nil}
   })

