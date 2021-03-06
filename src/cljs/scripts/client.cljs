(ns scripts.client
    (:require
      [ajax.core :refer [GET POST]]
      [enfocus.core :as ef]
      [enfocus.events :as ev]
      [enfocus.bind :refer [bind-view]]
      [clojure.browser.repl :as repl])
    (:require-macros [enfocus.macros :as em])
    (:use-macros [enfocus.macros :only [defaction]]))

;;************************************************
;; Dev stuff
;;************************************************
(def dev-mode true)

(defn repl-connect []
 (when dev-mode
   (repl/connect "http://localhost:9000/repl")))

;;************************************************
;; Retrieving data from dom
;;************************************************
(def state (atom {:questions ()
                  :current-question {}
                  :coords ()
                  }))

(def dom-template ())
(def debug true)

(defn set-question
  []
  (swap! state (fn [{:keys [questions] :as state}]
                 (-> state
                     (assoc :current-question (nth (shuffle questions) 0))
                     ))))

(defn get-questions
  []
  (GET "/data/questions.json"
    {:response-format :json :keywords? true
     :handler (fn [response]
                (swap! state assoc :questions response)
                (set-question))}))
(defn wrong-answer
  [choice]
  (when (js/confirm (str choice " Was the wrong answer. Click to continue!")) (set-question))
  )

(defn correct-answer
  [choice]
  (swap! state assoc :questions (drop 1 (:questions @state)))
  (when (js/confirm "Correct answer!. Click to continue!") (set-question)))

(defn check-is-answer
  [event]
  (let [choice (.-value (.-currentTarget.parentElement event)) question (:current-question @state)]
    (if (= choice (:answer question))
      (correct-answer choice)
      (wrong-answer choice))
  ))
;;This request takes way too long.  Not sure why but this should be
;;fast and it is not.  2-3 seconds for this request on a fast machine.
;;if you were using raw html we could used compiled and we would
;;not need a request
;;https://ckirkendall.github.io/enfocus-site/#doc-template
(em/defsnippet question-list "/" ["#answers > .answer:first-child"]
  [data]
  ".answer" (em/clone-for [q data]
    ".text" (ef/do->
                   (ef/html-content (:text q)))
     ef/this-node (ev/listen-live :click ".text" check-is-answer)
     ef/this-node (ef/set-attr :value (:id q))))


(defn set-question-answers [n data]
  ;;(js/console.log "STATE:" (pr-str@state))
  (ef/at n "#answers" (ef/content (question-list data))))

(defn set-question-text [n text]
  (ef/at n [".question .text"] (ef/content text)))


(defn view [n data]
  (set-question-text n (:text data))
  (set-question-answers n (:answers data)))

;; no need to listen on load because this isn't called until the
;; page loads
(defn load-coords []
  ;;(.log js/console "loaded")
  (let [svg (.getSVGDocument (. js/document (getElementById "game-board")))]
    (swap! state merge
           (ef/from svg
             :coords "#numbers circle"
             (fn [circle]
               {:x (.-baseVal.value (.-cx circle))
                :y (.-baseVal.value (.-cy circle))})))))

(defn init []
  (get-questions)
  (load-coords)
  (ef/at ["#questions-panel"]
         (bind-view state view :current-question)))

;;************************************************
;; onload
;;************************************************

(set! (.-onload js/window)
      #(do
         ;; (js/console.log "LOADING TEMPLATES")
         (em/wait-for-load
           ;;(js/console.log "DONE LOADING TEMPLATES")
                           (init))
                           (repl-connect)))
