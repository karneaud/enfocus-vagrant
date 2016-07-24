(ns scripts.client
    (:require
      [ajax.core :refer [GET POST]]
      [enfocus.core :as ef]
      [enfocus.bind :refer [bind-view]]
      [clojure.browser.repl :as repl])
    (:use-macros [enfocus.macros :only [deftemplate defsnippet defaction]]))

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
  (.log js/console "current-question: " (nth questions (inc questioned)))
  (reset! current-question (nth questions questioned))
  (inc questioned))

(defn get-questions
  []
  (GET "/data/questions.json"
    {:response-format :json
      :handler (fn [response]
        (def questions (shuffle response))
        (set-question))
    }
  ))

(defn answer-view
  [val]

  )

(defn answers-view
  [data]

  )

(defn set-question-text
  [n data]
    (.log js/console  "data" (str (:text data)) n)
    (ef/at n [".question .text"] (ef/content (:text data)) )
  )

(defn view
  [data]

  )

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
   (bind-view current-question set-question-text)))
;;************************************************
;; onload
;;************************************************

(set! (.-onload js/window)
      #(do
         (repl-connect)
         (init)))
