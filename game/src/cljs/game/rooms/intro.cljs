(ns game.rooms.intro)

(def intro
  {:complete? false
   :steps
   {:missed-train {:text "You miss the subway. A new one will be coming shortly, though."
                   :commands {:observe {:events [{:event :go-to-step :event-val :notebook}]}}}

    :notebook {:text "You see a small notepad on the ground near the subway tunnel entrance."}

    :next-subway
    {:text "The next subway rolls into the station."
     :commands {:observe {:text "Two rivers of people flow in and out of the subway doors."}
                :board {:text "You board the subway and grab a seat; everyone around you is maybe a ghost today; you could have passed through them as you waited to board."
                        :events [{:event :go-to-step :event-val :two-stops :delay 8000}]}}}
    :two-stops
    {:text "After two stops, the lights go out. The subway is currently in a tunnel. You know that if the lights come back on, everyone inside will be gone - just like a movie -- just like the ghosts they were before you got on the subway car."}}})


