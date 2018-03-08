(ns game.views.components
  (:require [re-frame.core :as re]
            [reagent.core :as r]
            [game.subs :as subs]
            [game.events :as e]))



(defn display
  []
  (let [prompt      @(re/subscribe [::subs/prompt])
        history     @(re/subscribe [::subs/history])
        curr-text   @(re/subscribe [::subs/current-text])]

    [:div.display
     (for [s history]
       [:div {:style {:color "grey"} :key (random-uuid)} s])
     [:div.py1 curr-text]]))


(defn prompt
  "Prompt console."
  []
  (let [prompt        @(re/subscribe [::subs/prompt])
        handle-change #(re/dispatch [::e/change-prompt (-> % .-target .-value)])
        handle-enter  #(when (= (.-key %) "Enter")
                         (re/dispatch [::e/enter-prompt prompt]))]
    [:div.prompt
     [:div  {:style {:margin-top "-1px" }} ">"]
     [:input {:placeholder "Enter a command..."
              :value prompt
              :on-change handle-change
              :on-key-press  handle-enter}]]))
