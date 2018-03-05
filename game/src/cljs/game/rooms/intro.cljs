(ns game.rooms.intro)

(def intro
  {:complete? false
   :steps [{:text "You miss the subway. A new one will be coming shortly, though."
            :commands {:observe "you look around."}}]
   :missed-train {:text "hi"
                  :commands { :observe "You look around but nothing catches your eye." }}})
