(ns game.rooms.intro)

;; NOTE: beware stack overflows when you hot reload the database :/

(def intro
  {:complete? false
   :steps
   {:missed-train
    {:text "You miss the subway. A new one will be coming shortly, though."
     :commands {:observe {:text "You see a small notepad on the ground near the subway tunnel entrance."
                          :events [{:event :go-to-step :event-val :next-subway :delay 10000}
                                   {:event :go-to-step :event-val :missed-train :delay 20000}
                                   ]}}}


    :next-subway
    {:text "The next subway rolls into the station."
     :commands {:observe {:text "it is very wet."
                          :events {:go-to-step :next-thing!}}}}}})
