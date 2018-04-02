(ns game.events
  (:require [game.db :as db]
            [game.util :as u :refer [>evt]]
            [re-frame.core :as re-frame]))

;; Create new audio objects.
(def audio-files
  {:subway-arrive     {:src (js/Audio. "audio/subway_arrive.mp3")
                       :volume 1
                       :fade-out-size 0.005
                       :fade-out-time 300
                       :loop false}
   :subway-loop       {:src (js/Audio. "audio/subway_loop.mp3")
                       :volume 0.9
                       :fade-in-time 100
                       :fade-in-rate 0.01
                       :fade-out-time 100
                       :fade-out-size 0.008
                       :loop true}
   :lake-waves-loop   {:src (js/Audio. "audio/lake_waves_loop.mp3")
                       :volume 1
                       :fade-in-time 100
                       :fade-in-rate 0.01
                       :fade-out-time 100
                       :fade-out-size 0.01
                       :loop true}
   :shutdown          {:src (js/Audio. "audio/shutdown.mp3")
                       :volume 0.8
                       :loop false}
   :cave              {:src (js/Audio. "audio/cave.mp3")
                       :volume 0.3
                       :fade-in-time 1000
                       :fade-in-rate 0.01
                       :fade-out-time 200
                       :fade-out-size 0.008
                       :loop true}
   :music             {:src (js/Audio. "audio/music.mp3")
                       :volume 1
                       :loop false
                       }
   :forest            {:src (js/Audio. "audio/forest_loop2.mp3")
                       :volume 0.6
                       :fade-in-time 1000
                       :fade-in-rate 0.01
                       :fade-out-time 500
                       :fade-out-size 0.01
                       :loop true}
   :match-light       {:src (js/Audio. "audio/match-light.mp3")
                       :volume 0.2
                       :loop false}})

(defn fade-out-audio
  "takes a file and fades it out at a certain rate.
  Must pass in the file's volume because it might be at 0 based on a fade in;
  we use the data structure for audio files to determine the volume min/maxes to cap at."
  ;; [file file-vol rate]

  [{:keys [file file-vol rate dec-size]
    :or {dec-size 0.02}
    :as opts}]
  (let [interval-id (atom 0)
        vol (atom file-vol)]
    (aset file "loop" false)
    (swap! interval-id #(js/setInterval (fn []
                                          (if (<= @vol 0)
                                            (do
                                              (.pause file)
                                              (js/clearInterval @interval-id)
                                              (reset! interval-id 0))
                                            (do
                                              (aset file "volume" (swap! vol (fn [e] (- e dec-size))))))) rate))))

(defn fade-in-audio
  [{:keys [file max-vol rate inc-size]
    :or {inc-size 0.02}
    :as opts}]
  (println "fading in at " inc-size "per " rate)
  (let [interval-id (atom 0)
        volume       (atom 0)]
    (set! (.-volume file) 0)
    (.play file)
    (swap! interval-id #(js/setInterval (fn []
                                          (if (>= @volume max-vol)
                                            (do
                                              (js/clearInterval @interval-id)
                                              (reset! interval-id 0))
                                            (aset file "volume" (swap! volume (fn [e] (+ e inc-size)))))) rate))))

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
   db/default-db))

(re-frame/reg-event-db
 ::start-game
 (fn  [db _]
   (js/console.log "Initializing railcar...")
   (>evt [:go-to-step  :missed-train])
   (assoc db :started? true)))


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
       (do (>evt [:play-one-shot audio-file])
           db)
       (do (>evt [:play-loop audio-file])
           db)))))

(re-frame/reg-event-db
 :stop-audio
 (fn [db [_ _]]
   (let [audio-files [(-> db :audio :one-shot) (-> db :audio :loop-a)]]
     (doseq [f audio-files]
       (when (and (not (nil? f)) ;; when let probably
                  (> (.-currentTime (f :src)) 0)) (fade-out-audio {:file (f :src)
                                                                   :file-vol (f :volume)
                                                                   :rate (f :fade-out-time)
                                                                   :dec-size (f :fade-out-size)})))

     db)))

(re-frame/reg-event-db
 :stop-loop-a
 (fn [db [_ _]]
   (let [loop-a (-> db :audio :loop-a)]
     (when (and (not (nil? loop-a)) ;; when let probably
                (> (.-currentTime (loop-a :src)) 0))
       (fade-out-audio {:file (loop-a :src)
                        :file-vol (loop-a :volume)
                        :rate (loop-a :fade-out-time)
                        :dec-size (loop-a :fade-out-size)})))
   db))

(re-frame/reg-event-db
 :play-loop
 (fn [db [_ audio]]
   ;; check if there is a loop playing, and if so, fade it out.
   (let [audio-file   (audio :src)
         fade-speed   (audio :fade-in-time)
         max-vol      (audio :volume)
         fade-in-rate (audio :fade-in-rate)]
     (aset audio-file "loop" true)
     (if (contains? audio :fade-in-time)
       (fade-in-audio {:file     audio-file
                       :max-vol  max-vol
                       :rate     fade-speed
                       :inc-size fade-in-rate})
       ;; (.play audio-file)
       (.play audio-file))
     (assoc-in db [:audio :loop-a] audio))))

(re-frame/reg-event-db
 :play-one-shot
 (fn [db [_ one-shot]]
   (if (contains? one-shot :fade-in-time)
     (fade-in-audio {:file (one-shot :src)
                     :max-vol (one-shot :volume)
                     :rate (one-shot :fade-in-time)
                     :inc-size (one-shot :fade-in-rate)})

     (let [audio (one-shot :src)]
       (set! (.-volume audio) (one-shot :volume))
       (.play audio)))
   (assoc-in db [:audio :one-shot] one-shot)))
