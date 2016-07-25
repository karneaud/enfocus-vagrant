(ns scripts.client
    (:require
      [ajax.core :refer [GET POST]]
      [enfocus.core :as ef]
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
(def questions ())
(def current-question (atom {}))
(def coords ())
(def dom-template ())
(def questioned 0)

(defn set-question
  []
  (reset! current-question (nth questions questioned))
  (inc questioned))

(defn get-questions
  []
  (GET "/data/questions.json"
    {:response-format :json :keywords? true
      :handler (fn [response]
        (def questions (shuffle response))
        (set-question))
    }
  ))

(defn set-question-answers
  [n data]

    ;;(.log js/console "n" (values(ef/at n ["#answers"])) "data " (str data))
    (ef/at n ["#answers > .answer:first-child"]
      (em/clone-for [q data]
        ".text" (ef/content (:text q))
        ;; (ef/set-attr :value (:id q))
      ))
  )

(defn set-question-text
  [n text]
    (ef/at n [".question .text"] (ef/content text)))

(defn view
  [n data]
     (set-question-text n (:text data))
    (set-question-answers n (:answers data)))

(get-questions)

(.addEventListener (. js/document (getElementById "game-board")) "load" (fn [e]
    (.log js/console "loaded")
    (let [svg (.getSVGDocument (. js/document (getElementById "game-board")))]
         (let [circles (array-seq (.querySelectorAll svg "#numbers circle"))]
           (def coords (for [circle circles]
               {:x (.-baseVal.value (.-cx circle)) :y (.-baseVal.value (.-cy circle))})
           ))
     )
  ))

(defaction init []
  (ef/at ["#questions-panel"]
   (bind-view current-question view)))
;;************************************************
;; onload
;;************************************************

(set! (.-onload js/window)
      #(do
         (repl-connect)
         (init)))
