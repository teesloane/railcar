(ns game.events
  (:require [game.db :as db]
            [game.util :as u :refer [>evt]]
            [re-frame.core :as re-frame]))

;; Create new audio objects.
(def audio-files
  {:board-subway {:src (js/Audio. "audio/subway.wav")
                  :fade-time 200
                  :loop false}
   :subway-arrive {:src (js/Audio. "audio/subway_arrive.wav")
                  :fade-time 200
                  :loop false}

   :match-light  {:src (js/Audio. "audio/match-light.wav")
                  :loop false}})


(defn fade-out-audio
  [file rate]
  (println "attempting fade out on " file)
  (def interval (js/setInterval (fn []
                    (let [current-volume (.-volume file)]
                      (println "current-volumen is " current-volume)
                      (if (<= current-volume 0.05)
                        (do
                          (println "clearing interfval")
                        (.pause file)
                        (js/clearInterval interval)
                        )
                        (aset file "volume" (- current-volume 0.04))))
                    ) rate)))


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


;; Sets an audio file in the database, then dispatches the event to handle playing it.
(re-frame/reg-event-db
 :play-audio
 (fn [db [_ audio]]
   (let [audio-file (-> audio-files audio)
         loop?      (-> audio-files audio :loop)]
     (if-not loop?
       (do (>evt [:play-one-shot audio-file]) db)
       (do (>evt [:play-loop audio-file] db)))
     )))

(re-frame/reg-event-db
 :stop-audio
 (fn [db [_ _]]
   (let [audio-map (-> db :audio :one-shot)
         audio-file (audio-map :src)
         fade-time (audio-map :fade-time)
         ]
     (when (> (.-currentTime audio-file) 0)
       (fade-out-audio audio-file fade-time))
     db
     )))

(re-frame/reg-event-db
 :play-loop
 (fn [db [_ audio]]
   ;; check if there is a loop playing, and if so, fade it out.
   (let [curr-audio (-> db :audio :loop-a)]
     (aset audio "loop" true)
     (.play audio)
     (assoc-in db [:audio :loop-a] audio)))
 )

(re-frame/reg-event-db
 :play-one-shot
 (fn [db [_ one-shot]]
   (.play (one-shot :src))
   (assoc-in db [:audio :one-shot] one-shot)))
