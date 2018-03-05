(ns game.db
  (:require [game.rooms.intro :refer [intro]]))

(def default-db
   {:current-room intro
    :current-command ""
    :current-step 0
    :prompt ""

    ;; rooms -- to be organized later.
    :intro intro
    })
