(ns game.views.components
  (:require [re-frame.core :as re]
            [reagent.core :as r]
            [game.subs :as subs]
            [game.events :as e]))

(defn display
  []
  (let [room        @(re/subscribe [::subs/current-room])
        prompt      @(re/subscribe [::subs/prompt])
        curr-cmd    @(re/subscribe [::subs/current-command])
        curr-step   @(re/subscribe [::subs/current-step])]

    [:div.display
     [:div (get curr-step :text "default text. Fix me someday!")]
     [:div.py3 (get curr-cmd :text "default text2 Fix me somday!")]]))

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
