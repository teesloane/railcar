(ns game.rooms.event-builder
  "Events that build the DS's to be dispatched for re-frame.
  These are just maps describing what a call to dispatch will do eventually.
  These should only be used in the story files"
  (:require [game.events :as e])
  )


(defn go-to-step
  "Advance to a new step."
  ([next-step delay]
   {:event :go-to-step
    :val next-step
    :delay delay})

  ([next-step]
   {:event :go-to-step
    :val next-step}))



(defn set-curr-text
  [text]
  {:event ::e/set-current-text
   :val text})
