(ns game.db
  (:require [game.rooms.intro :refer [intro]]))

(def default-db
   {:current-room intro
    :prompt ""})
