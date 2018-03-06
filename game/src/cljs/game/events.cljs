(ns game.events
  (:require [re-frame.core :as re-frame]
            [game.db :as db]))

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
    ;; prompt -> keyword -> get curr step # -> get command via those two.
    (let [prompt-key (keyword prompt) ; "observe" -> :observe
          step-idx (db :current-step)
          curr-cmd (-> db :current-room :steps (get step-idx) :commands prompt-key)
          new-db (-> db
                     (assoc :prompt "")
                     (assoc :current-command curr-cmd))]

        (doseq [event (-> curr-cmd :events)] (re-frame/dispatch event)
          new-db))))

(re-frame/reg-event-db
  ::change-prompt
  (fn [db [_ prompt]]
    (assoc db :prompt prompt)))


(re-frame/reg-event-db
 :go-to-step
 (fn [db [_ step]]
   (assoc db :current-step step)))
