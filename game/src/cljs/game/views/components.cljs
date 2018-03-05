(ns game.views.components
  (:require [re-frame.core :as re]
            [reagent.core :as r]
            [game.subs :as subs]
            [game.events :as events]))

(defn display
  []
  (let [room            @(re/subscribe [::subs/current-room])
        prompt          @(re/subscribe [::subs/prompt])
        curr-cmd        @(re/subscribe [::subs/current-command])
        curr-step       @(re/subscribe [::subs/current-step])]
    [:div.display
     [:div curr-step]
     [:div.py3 curr-cmd]]))

(defn prompt
  "Prompt console."
  []
  (fn []
    (let [prompt @(re/subscribe [::subs/prompt])
          handle-change #(re/dispatch [::events/change-prompt
                                       (-> % .-target .-value)])
          handle-enter #(when (= (.-key %) "Enter")
                          (re/dispatch [::events/enter-prompt prompt]))]
      [:div.prompt
       [:div  {:style {:margin-top "-1px" }} ">"]
       [:input {:placeholder "Enter a command..."
                :value prompt
                :on-change handle-change
                :on-key-press  handle-enter}]])))
