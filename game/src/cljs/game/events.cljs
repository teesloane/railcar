(ns game.events
  (:require [game.db :as db]
            [game.util :as u :refer [>evt]]
            [re-frame.core :as re-frame]))

(def audio-files
  {:board-subway {:src (js/Audio. "audio/subway.wav")}
   :match-light  {:src (js/Audio. "audio/match-light.wav")}})


(defn batch-events
  "takes a list of custom events formatted for re-frame and dispatches them.
  ex: {:event :re-frame-event :val :re-frame-event-val :delay 5000}
  Events can be delayed and will be sorted by soonest-to-latest in execution order."
  [events]
  (let [sorted-events (sort-by :delay events)]
    (doseq [{:keys [event val delay opts]} sorted-events]
      (cond
        delay       (u/sleep delay #(>evt [event val]))
        :else       (>evt [event val])))))


;; -- General Events --

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   (>evt [:go-to-step :missed-train])
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

;; --  Prompt Events --

;; reset prompt -> use prompt val to get commands -. dispatch events for cmd.
(re-frame/reg-event-db
 ::enter-prompt
 (fn [db [_ prompt]]
   (let [curr-step-txt (u/get-curr-step db :text)
         cmd-events    (u/get-curr-step db :commands prompt)]
     (batch-events cmd-events)
     (assoc db :prompt ""))))


(re-frame/reg-event-db
 ::change-prompt
 (fn [db [_ prompt]]
   (assoc db :prompt prompt)))


(re-frame/reg-event-db
 :go-to-step
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


(re-frame/reg-event-db
 :play-audio
 (fn [db [_ audio]]
   (.play (-> audio-files audio :src))
   db))

