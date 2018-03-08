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

(defn get-step-key
  [db k]
  (let [current-step (db :current-step)]
    (get-in db [:current-room :steps current-step])
    (-> db :current-room :step)))

;; Looks hairy. Basically we are using the db's "current" keys, to drill
;; down into the content itself to shape it.


;; 1. resets prompt
;; 2a. load up command for current step -if any
;; 2b. Sort any events command might have (ie, "observe" might trigger a sound playinger after 4 s)
;; 2c. sort events by how much delay they have.
;; 2d. Dispatch all events for a command.
(re-frame/reg-event-db
 ::enter-prompt
 (fn [db [_ prompt]]
   (let [prompt-key    (keyword prompt) ; "observe" -> :observe
         curr-step     (db :current-step)
         ;; helper functions for this please.
         curr-cmd      (-> db :current-room :steps (get curr-step) :commands prompt-key) ;; todo - handle case where prompt input doesnt exist on story map.
         curr-step-txt (-> db :current-room :steps (get curr-step) :text)
         events        (get curr-cmd :events) ;; sort this by their delays, least to greatest.
         events-sorted (sort-by :delay events)
         ;; clear prompt; push last text into history; set new text.
         new-db        (-> db
                           (assoc :prompt "")
                           (update :history conj curr-step-txt)
                           (assoc :current-text ""))]

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
   (let [next-text (-> db :current-room :steps step :text)]
     (-> db
         (assoc :current-step step)
         (assoc :current-text next-text)))))
