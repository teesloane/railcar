(ns game.rooms.intro
  (:require [game.rooms.event-builder :as ev]))

(def intro
  {:complete? false
   :steps
   {

    :missed-train
    {:text "You miss the subway. A new one will be coming shortly, though."
     :events [(ev/go-to-step :next-subway 15000)
              {:event :play-audio :val :board-subway :delay 3000}
              ]}

    :next-subway
    {:text "The next subway rolls into the station."
     :commands {:board [(ev/go-to-step :board-subway)]}}

    :board-subway
    {:text "You board the subway and grab a seat; everyone around you is maybe a ghost today; you could have passed through them as you waited to board."
     :events [(ev/go-to-step :two-stops 10000)]
     :commands {}}

    :two-stops
    {:text "After two stops, the lights go out. The subway is currently in a tunnel. You know that if the lights come back on, everyone inside will be gone - just like a movie -- just like the ghosts they were before you got on the subway car."
     :commands {:observe [(ev/go-to-step :match)]}}


    :match
    {:text "You hear a scratching sound, followed by a flare of light. Someone has lit a match and you watch as it recedes away from you, towards the front of the subway car. The flame slows its dance to a stop, and disappears for a moment before reappearing housed in a lantern. The light, now at least twice in size, bounces around the train."
     :commands {:observe [(ev/go-to-step :cave-train-describe)]}}


    :cave-train-describe
    {:text "the train is new, or, at least different. The train is a cavern. You see the glimmer of subterranean rocks catching the refracted light from the lantern."
     :commands {:north [(ev/go-to-step :get-up-go-north)]}}

    :get-up-go-north
    {:text "You rise from your seat. It feels like you leave another world behind as you do. You walk towards the front of the subway car. Before long you bump into something."
     :commands {:observe [(ev/go-to-step :see-watch)]}}

    :see-watch
    {:text "In the dim light it appears you've bumped into a stalagmite. Or a subway seat. Whatever it is, seated on top you think you see a watch."
     :commands {:take-watch [(ev/go-to-step :take-watch)]}}

    :take-watch
    {:text "You grab the watch; it is bulky, and has three buttons."
     :commands {:push-button-1 [(ev/set-curr-text "The watch screen lights up. There is a 12 o'clock and no watch hands on the screen.")]
                :push-button-2 [(ev/set-curr-text "Nothing seems to happen.")]
                :push-button-3 [(ev/set-curr-text "A red light blinks on the watch intermittently.")]
                :go-north [(ev/go-to-step :first-door)]}}

    :first-door
    {:text "You reach the front of the car. There is a door leading to the next subway car."
     :commands {:open-door [(ev/go-to-step :first-door-open)]}}

    :first-door-open
    {:text "You open the door and see a ladder. You crane your neck and look up. There is another subway door at the end of the ladder with blue light emitting from it's window."
     :commands {:climb-ladder [(ev/go-to-step :climb-ladder)]}}

    :climb-ladder
    {:text "You climb the ladder, passing rows of subway seats embedded in the narrow passageway on your way up. You reach another subway door."
     :commands {:open-door [(ev/go-to-step :second-door-open)]}}

    :second-door-open
    {:text "You open the door and it swings open, just brushing your arm as you cling to the ladder. A breeze hits you. It smells like the sea and you feel more awake then you have in a long time.  Behind the door is a ledge. You climb onto it."
     :commands {:observe [(ev/go-to-step :bridge-observe-1)]}}

    :bridge-observe-1
    {:text "With your back turned on the cavern behind you, you face a long narrow stretch of water. The sun is shining across the water and something in you tells you that you could stay here for a long time and, wouldn't that be nice?"
     :commands {:go-north [(ev/go-to-step :bridge-walk-1)]}}

    :bridge-walk-1
    {:text "You walk along the bridge. As you go, you pass half-submerged subway seats bobbing up and down in the waves of the water. You stop halfway along the bridge."
     :commands {:observe [(ev/go-to-step :bridge-observe-2)]}}

    :bridge-observe-2
    {:text "You could stand here forever, for some time. For some time, forever, perhaps. The sun is washing over you and you consider the waves as they consider you, passing below your feet endlessly."
     :commands {:go-north [(ev/go-to-step :bridge-walk-2)]}}

    :bridge-walk-2
    {:text "You continue walking north. As you expected, another subway door emerges before you; or perhaps, you passed through something and emerged before it. Behind you now, you think of the moment recently passed; standing as if in the middle of everything."
     :commands {:open-door [(ev/go-to-step :third-door-open)]}}

    :third-door-open
    {:text "The sound of waves and the sea breeze diminish behind you before they are replaced by the sound of leaves rustling and birds singly softly."
     :commands {:observe [(ev/go-to-step :trail-observe-1)]}}

    :trail-observe-1
    {:text "You are on a dirt trail, it descends as it stretches out before you, converging at a point on the horizon. Sunlight breaks through the canopy at random intervals, spilling light on the trail intermittently."
     :commands {:go-north [(ev/go-to-step :trail-walk-1)]}}

    :trail-walk-1
    {:text "You walk north along the path, stopping at the first splash of sunlight on the trail. Your mind turns around and you remember waiting for the subway, and something about a trip to the sea. You think that your memory is like a contortionist, and you are correct -- it is."
     :commands {:go-north [(ev/go-to-step :trail-walk-2)]}}

    :trail-walk-2
    {:text "You continue along the path before stopping at another sunspot. Here you remember the time you spent walking trails when you were younger - trails just like this -- long, narrow, and with an intermittent canopy that would sometimes splash sun across you as you walked."
     :commands {:go-north [(ev/go-to-step :trail-walk-3)]}}

    :trail-walk-3
    {:text "You reach another sun spot and continue past it. Something in you is afraid of what you will think of if you were to pause and reflect; even if the sun would feel so good on your face."
     :commands {:go-north [(ev/go-to-step :trail-walk-4)]}}

    :trail-walk-4
    {:text "You avoid more sunspots but you know this is ok; you know a journey has to end. As you continue walking the trees shrink and the sun sets. Before long you've reached the final door of the subway. That was quick wasn't it? Everything is normal again, of course. Everything that changed, never changed. You scratch your arm a bit and sigh. It's your stop to get off, but you're at the front of the train, and the stairs out of the station are at the back of the train, where you started."
     :commands {:end [(ev/go-to-step :trail-walk-4)]}}}})
