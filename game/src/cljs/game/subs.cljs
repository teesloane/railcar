(ns game.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))


;; game subs

;; get story
(reg-sub
 ::gst
 (fn [db _]
   (-> db :current-room :missed-train)))

(reg-sub
  ::prompt
  (fn [db _]
    (:prompt db)))
