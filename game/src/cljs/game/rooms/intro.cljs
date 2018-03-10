(ns game.rooms.intro
  (:require [game.rooms.event-builder :as ev]
            [game.events :as e]))

(def intro
  {:complete? false
   :steps
   {

    :missed-train
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
     :events [(ev/go-to-step :two-stops 2000)]
     :commands {}}

    :two-stops
    {:text "After two stops, the lights go out. The subway is currently in a tunnel. You know that if the lights come back on, everyone inside will be gone - just like a movie -- just like the ghosts they were before you got on the subway car."
     :commands {:observe [(ev/go-to-step :match)]}}


    :match
    {:text "You hear a scratching sound, followed by a flare of light. Someone has lit a match and you watch as it recedes away from you, towards the front of the subway car. The flame slows its dance to a stop, and disappears for a moment before reappearing housed in a lantern. The light, now at least twice in size, bounces around the train."
     :commands {:observe [(ev/go-to-step :cave-train-describe)]}}


    :cave-train-describe
    {:text "the train is new, or, at least different. The train is a cavern. You see hte glimmer of subterranean rocks catching the refracted light from the lantern."
     :commands {:north [(ev/go-to-step :get-up-go-north)]}}

    :get-up-go-north
    {:text "You rise from your seat. It feels like you leave another world behind as you do. You walk towards the front of the subway car. Before long you bump into something."
     :commands {:observe [(ev/go-to-step :see-watch)]}}

    :see-watch
    {:text "In the dim light it appears you've bumped into a stalagmite. Or a subway seat. Whatever it is, seated on top you think you see a watch."
     :commands {:take-watch [(ev/go-to-step :take-watch)]}}


    :take-watch
    {:text "You grab the watch; it is bulky, and has three large buttons."
     :commands {:push-button-1 [(ev/set-curr-text "Nothing seems to happen.")]
                :push-button-2 [(ev/set-curr-text "Nothing seems to happen.")]
                :push-button-3 [(ev/set-curr-text "Everything explodes")]}}}})

