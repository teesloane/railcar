(ns game.util
  (:require [re-frame.core]))


(defn sleep [ms f]
  (js/setTimeout f ms))

(defn get-curr-step
  "Reaches into the db and pulls out the current step for the room we are in.
  ex usage: `(u/get-curr-step db :text)`

  The above reaches into the room's data structure which has many steps.
  Here's what it might look like:

   :steps
   {:missed-train
    {:text 'You miss the subway. A new one will be coming shortly, though.'
     :commands {:observe [{:event :go-to-step :event-val :next-subway}]}} "

  [db & keys]
  (let [curr-step (db :current-step)]
    (get-in db (concat [:current-room :steps curr-step] keys))))


;; syntactic-sugar

(def <sub (comp deref re-frame.core/subscribe))

(def >evt re-frame.core/dispatch)
