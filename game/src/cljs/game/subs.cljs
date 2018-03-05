(ns game.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

;; -- top level subs --

;; FIXME - current-step should be broken into two things:
;; a) the data structure of "current-step" - the possible text,
;; but also possible commands, possible media etc, and
;; b) just what the ui displays: ie, "current-step-text"
;; might just display "You are in a room" -- this way, it can
;; easily be clearable by other elements on the screen.

(reg-sub ::active-panel    (fn [db _] (:active-panel db)))
(reg-sub ::prompt          (fn [db _] (:prompt db)))
(reg-sub ::current-room    (fn [db _] (-> db :current-room)))
(reg-sub ::current-command (fn [db _] (:current-command db)))
(reg-sub ::current-step
         (fn [db _]
           (let [idx (-> db :current-step)]
             (-> db :current-room :steps (get idx) :text))))

