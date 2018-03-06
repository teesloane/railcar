(ns game.events
  (:require [game.db :as db]
            [game.util :as u]
            [re-frame.core :as re-frame]))

;; -- General Events --

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

;; --  Prompt Events --

;; Looks hairy. Basically we are using the db's "current" keys, to drill
;; down into the content itself to shape it.
(re-frame/reg-event-db
 ::enter-prompt
 (fn [db [_ prompt]]
   (let [prompt-key    (keyword prompt) ; "observe" -> :observe
         curr-step     (db :current-step)
         curr-cmd      (-> db :current-room :steps (get curr-step) :commands prompt-key)
         events        (curr-cmd :events) ;; sort this by their delays, least to greatest.
         events-sorted (sort-by :delay events)
         new-db        (-> db
                           (assoc :prompt "")
                           (assoc :current-command curr-cmd))]

     ;; Run every event in a command's data structure.
     (doseq [{:keys [event event-val delay]} events-sorted]
       (if delay
         (u/sleep delay #(re-frame/dispatch [event event-val]))
         (re-frame/dispatch [event event-val])))

     ;; return new db once all events are dispatched.
     new-db)))

(re-frame/reg-event-db
  ::change-prompt
  (fn [db [_ prompt]]
    (assoc db :prompt prompt)))


(re-frame/reg-event-db
 :go-to-step
 (fn [db [_ step]]
   (assoc db :current-step step)))
