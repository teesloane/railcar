(ns game.rooms.intro)

;; NOTE: beware stack overflows when you hot reload the database :/

(def intro
  {:complete? false
   :steps {:missed-train ;; our current step, in a room.
           ;; Text is the piece of story that confronts the player.
           {:text "You miss the subway. A new one will be coming shortly, though."
            ;; Commands is a map of possible events the user can put into the prompt.
            ;; what the user types is currently used as a getter at these values.
            ;; ie. typing "observe" will convert the string to a keyword, and use it to fetch
            ;; the observe value from this map.
            :commands {:observe {:text "you look around."
                                 ;; an event is a thing to trigger when :observe is called,
                                 ;; where the key is a re-frame event to trigger and the val the event value.
                                 :events {:go-to-step :missed-boat}}}}
                                          ; :go-to-step2 :very-chill


           :missed-boat {:text "You miss A boat"
                         :commands {:observe {:text "it is very wet."
                                              :events {:go-to-step :next-thing!}}}}}})
