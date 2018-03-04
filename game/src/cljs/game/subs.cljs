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

;; get story text
(reg-sub
 ::gst
 (fn [db _]
   (-> db :current-room :story :missed-train :text)))
