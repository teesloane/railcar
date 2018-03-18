(ns game.events
  (:require [game.db :as db]
            [game.util :as u :refer [>evt]]
            [clojure.string :as str]
            [re-frame.core :as re-frame]))


;; event management, "middleware", etc

(defn batch-events
  "takes a list of custom events formatted for re-frame and dispatches them.
  ex: {:event :re-frame-event :event-val :re-frame-event-val :delay 5000}
  Events can be delayed and will be sorted by soonest-to-latest in execution order."
  [events]
  (let [sorted-events (sort-by :delay events)]
    (doseq [{:keys [event val delay opts]} sorted-events]
      (cond
        delay       (u/sleep delay #(>evt [event val]))
        :else       (>evt [event val])))))


(defn has-required?
  "Moving to a next step may require having a special item.
  If the player does not have the item, run set-curr-text with the blocked text."
  [])


(defn handle-take
  "If there are items / the first word of the prompt is `take` then handle picking up an item if there is one.
  This sucks, is hacky, not a smart architecture blah blah blah"
  [prompt]
  (println "handle-take called" prompt)
  (let [split-prompt (-> prompt (str/split #" "))
        take? (= "take" (first split-prompt))
        item (second split-prompt)
        ]
    (when take? (>evt [::take-item (keyword item)]))))


;; -- General Events --

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   (>evt [:go-to-step :take-watch])
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

;; --  Prompt Events --

;; reset prompt -> use prompt val to get commands -. dispatch events for cmd.
;; a cmd could have MANY events -- like, go-to-step, and play-audio etc.
(re-frame/reg-event-db
 ::enter-prompt
 (fn [db [_ prompt]]
   (let [prompt-key    (keyword prompt) ; "observe" -> :observe
         curr-step-txt (u/get-curr-step db :text)
         cmd-events    (u/get-curr-step db :commands prompt-key)]

     ;; before running batch events, check if we can pick up an item
     ;; by parsing the prompt for the word "take". this is not ideal.
     (handle-take prompt)
     (batch-events cmd-events)
     (assoc db :prompt ""))))


(re-frame/reg-event-db
 ::change-prompt
 (fn [db [_ prompt]]
   (assoc db :prompt prompt)))


(re-frame/reg-event-db
 :go-to-step ;; needs to be a global keyword; is used by datastructures
 (fn [db [_ next-step]]
   (let [next-text     (-> db :current-room :steps next-step :text)
         curr-step-txt (u/get-curr-step db :text)]
     (re-frame/dispatch [::current-step-events])
     (-> db
         (assoc :current-step next-step)
         (update :history conj curr-step-txt)
         (assoc :current-text next-text)))))


(re-frame/reg-event-db
 ::set-current-text
 (fn [db [_ text]]
   (assoc db :current-text text)))

(re-frame/reg-event-db
 ::current-step-events
 (fn [db [_ _]]
   (batch-events (u/get-curr-step db :events))
   db))

;; inventory, etc.

(re-frame/reg-event-db
 ::take-item-old
 (fn [db [_ payload]]
   (println "take item called " payload)
   (let [item (payload :item)
         text (payload :text)]
     (assoc db :inventory item)
     )))


;; Look at current step,
;; check if it has items, if so, take item will put item in inventory.
(re-frame/reg-event-db
 ::take-item
 (fn [db [_ item]]
   (let [curr-step (u/get-curr-step db)
         ]
     (println "curr step is " curr-step)
     (println "item to try and get " item)
     ;; put item in inventory, and remove it from the story, reset the current step to reflect change.
     (-> db
         (assoc :inventory (-> curr-step :items item))
         (update-in [:intro :steps (db :current-step) :items])
         )
     (assoc db :inventory item)
     db
     )))

