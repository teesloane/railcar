(ns game.db
  (:require [game.rooms.intro :refer [intro]]))

(def default-db
  {:current-room intro
   :current-command ""
   :current-step :missed-train ;; TODO - set this on scene load via an event.
   :prompt ""

   ;; rooms -- to be organized later.
   :intro intro})

