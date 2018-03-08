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


;; TODO move this somewhere else
(defn get-curr-step
  [db & keys]
  (let [curr-step (db :current-step)]
    (get-in db (concat [:current-room :steps curr-step] keys))))

;; 1. resets prompt
;; 2. use the prompt value to get the `:commands` for the current-step
;; 3. move the current text into the `:history` vector.
;; 4. Dispatch all events for the prompts-command.
(re-frame/reg-event-db
 ::enter-prompt
 (fn [db [_ prompt]]
   (let [prompt-key    (keyword prompt) ; "observe" -> :observe
         curr-step-txt (get-curr-step db :text)
         cmd-events    (sort-by :delay (get-curr-step db :commands prompt-key))
         ;; clear prompt; push last text into history; set new text.
         new-db        (-> db
                           (assoc :prompt "")
                           (update :history conj curr-step-txt)
                           (assoc :current-text ""))]

     ;; Run every event in a command's data structure.
     (doseq [{:keys [event event-val delay]} cmd-events]
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
