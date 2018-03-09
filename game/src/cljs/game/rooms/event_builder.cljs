(ns game.rooms.event-builder)

(defn go-to-step
  "Advance to a new step."
  [next-step delay]
  {:event :go-to-step
   :event-val next-step
   :delay delay})

