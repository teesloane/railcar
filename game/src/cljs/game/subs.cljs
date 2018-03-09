(ns game.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]
            [game.util :as u]))

;; -- top level subs --

;; FIXME - current-step should be broken into two things:
;; a) the data structure of "current-step" - the possible text,
;; but also possible commands, possible media etc, and
;; b) just what the ui displays: ie, "current-step-text"
;; might just display "You are in a room" -- this way, it can
;; easily be clearable by other elements on the screen.

(reg-sub ::active-panel    (fn [db _] (:active-panel db)))
(reg-sub ::prompt          (fn [db _] (:prompt db)))
(reg-sub ::history         (fn [db _] (-> db :history)))
(reg-sub ::current-room    (fn [db _] (-> db :current-room)))
(reg-sub ::current-command (fn [db _] (:current-command db)))
(reg-sub ::current-text    (fn [db _] (:current-text db)))
(reg-sub ::current-step    (fn [db _] (:current-step db)))


;; Computed Subscriptions --

;; Gets a lit of possible pompts and displays them
;; allows the user to know what they can do.
(reg-sub
 ::possible-prompts
 (fn [db _]
   (keys (u/get-curr-step db :commands))))

