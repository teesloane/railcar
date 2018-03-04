(ns game.rooms.intro)

(def intro
  {:complete? false
   :missed-train {:text "You miss the subway. A new one will be coming shortly, though."
                  :actions { :observe "observe" }}})
