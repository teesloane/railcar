(ns game.rooms.event-builder
  "Events that build the DS's to be dispatched for re-frame.
  These are just maps describing what a call to dispatch will do eventually.
  These should only be used in the story files

  Uses namespaced keywords to avoid circular dependencies: :game.events/my-event
  requiring the namespace for it's keywords cases circular deps with the app db."
  )


(defn go-to-step
  "Advance to a new step."
  ([next-step delay opts]
   {:event :go-to-step
    :val next-step
    :delay delay
    :opts opts})

  ([next-step delay]
   {:event :go-to-step
    :val next-step
    :delay delay
    })

  ([next-step]
   {:event :go-to-step
    :val next-step}))



(defn set-curr-text
  [text]
  {:event :game.events/set-current-text
   :val text})


(defn take-item
  [item text]
  {:event :game.events/take-item
   :val {:item item
         :text text
         }})
