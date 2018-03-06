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


    (println "curr cmd is " curr-cmd)
    ;; DO THIS AS AN EVENT AFTER ON KEY PRESS ENTER.
    (when-not (empty? curr-cmd)
      ;; loop over the all the possible events for the current command, run them.
      ;;
      (doseq [e (-> curr-cmd :events)]
        (do
            (println "e is " e)
            (re/dispatch e)))

      #_(re/dispatch (-> curr-cmd :events first))) ;; when-let this?

    [:div.display
     [:div (get curr-step :text)]
     [:div.py3 (get curr-cmd :text)]]))

(defn prompt
  "Prompt console."
  []
  (fn []
    (let [prompt @(re/subscribe [::subs/prompt])
          handle-change #(re/dispatch [::e/change-prompt
                                       (-> % .-target .-value)])
          handle-enter #(when (= (.-key %) "Enter")
                          (re/dispatch [::e/enter-prompt prompt]))]
      [:div.prompt
       [:div  {:style {:margin-top "-1px" }} ">"]
       [:input {:placeholder "Enter a command..."
                :value prompt
                :on-change handle-change
                :on-key-press  handle-enter}]])))
