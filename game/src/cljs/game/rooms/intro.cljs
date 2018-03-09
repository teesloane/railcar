(ns game.rooms.intro
  (:require [game.rooms.event-builder :as ev]))

(def intro
  {:complete? false

   :steps
   {:missed-train
    {:text "You miss the subway. A new one will be coming shortly, though."
     :events [(ev/go-to-step :next-subway 1000)]}

    :next-subway
    {:text "The next subway rolls into the station."
     ;; This one doesn't work until we find a way to only execute this delay if
     ;; we are still on the same step.
     ;; :events [(ev/go-to-step :missed-train 25000 {:required-step :next-subway})]
     :commands {:board [{:event :go-to-step :event-val :board-subway}]}}

    :board-subway
    {:text "You board the subway and grab a seat; everyone around you is maybe a ghost today; you could have passed through them as you waited to board."
     :events [(ev/go-to-step :two-stops 8000)]
     :commands {}}

    :two-stops
    {:text "After two stops, the lights go out. The subway is currently in a tunnel. You know that if the lights come back on, everyone inside will be gone - just like a movie -- just like the ghosts they were before you got on the subway car."}}})



